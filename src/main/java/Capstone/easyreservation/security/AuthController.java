package Capstone.easyreservation.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import Capstone.easyreservation.entity.Utente;
import Capstone.easyreservation.exception.UnauthorizedException;
import Capstone.easyreservation.payloads.LoginSuccessfullPayload;
import Capstone.easyreservation.payloads.NuovoUtentePayload;
import Capstone.easyreservation.payloads.UtenteLoginPayload;
import Capstone.easyreservation.services.UtenteService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	UtenteService utenteService;

	@Autowired
	JWTTools jwtTools;

	@Autowired
	PasswordEncoder bcrypt;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public Utente saveUtente(@RequestBody NuovoUtentePayload body) {

		body.setPassword(bcrypt.encode(body.getPassword()));
		Utente created = utenteService.save(body);
		return created;
	}

	@PostMapping("/login")
	public LoginSuccessfullPayload login(@RequestBody UtenteLoginPayload body) {
		try {
			Utente utente = utenteService.findByEmail(body.getEmail());

			if (bcrypt.matches(body.getPassword(), utente.getPassword())) {
				String token = jwtTools.createToken(utente);
				return new LoginSuccessfullPayload(token);
			} else {
				throw new UnauthorizedException("Credenziali non valide");
			}
		} catch (ChangeSetPersister.NotFoundException e) {
			throw new UnauthorizedException("Utente non trovato con email: " + body.getEmail());
		}
	}


	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		System.out.println("Logout effettuato con successo");
		return ResponseEntity.ok("Logout effettuato con successo");

	}
}