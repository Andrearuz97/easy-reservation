package Capstone.easyreservation.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import Capstone.easyreservation.entity.Utente;
import Capstone.easyreservation.exception.UnauthorizedException;
import Capstone.easyreservation.services.UtenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
	@Autowired
	private JWTTools jwtTools;

	@Autowired
	private UtenteService utenteService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new UnauthorizedException("Per favore passa il token nell'authorization header");
		}

		String token = authHeader.substring(7);
		jwtTools.verifyToken(token);
		String id = jwtTools.extractSubject(token);
		Utente currentUser = null;
		try {
			currentUser = utenteService.findById(UUID.fromString(id));
		} catch (NotFoundException e) {
			throw new UnauthorizedException("Utente non trovato");
		}
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(currentUser, null,
				currentUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
		filterChain.doFilter(request, response);
	}
}
