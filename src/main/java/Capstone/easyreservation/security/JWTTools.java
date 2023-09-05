package Capstone.easyreservation.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import Capstone.easyreservation.entity.Utente;
import Capstone.easyreservation.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTTools {
	@Value("${spring.jwt.secret}")
	private String secret;

	public String createToken(Utente u) {
		return Jwts.builder().setSubject(u.getId().toString()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
				.signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();
	}

	public void verifyToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
		} catch (Exception e) {
			throw new UnauthorizedException("Il token non è valido! Effettua nuovamente il login");
		}
	}

	public String extractSubject(String token) {
		return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build().parseClaimsJws(token)
				.getBody().getSubject();
	}
}