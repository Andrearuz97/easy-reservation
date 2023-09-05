package Capstone.easyreservation.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	UtenteService us;

	@Autowired
	JWTTools jwtTools;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public Utente saveUser(@RequestBody NuovoUtentePayload body) {
		return us.saveUser(body);
	}

	@PostMapping("/login")
	public LoginSuccessfullPayload login(@RequestBody UtenteLoginPayload body) {

		Utente user = us.findByEmail(body.getEmail());

		if (body.getPassword().equals(user.getPassword())) {
			String token = jwtTools.createToken(user);
			return new LoginSuccessfullPayload(token);
		} else {
			throw new UnauthorizedException("Credenziali non valide!");
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		System.out.println("Logout effettuato con successo");
		return ResponseEntity.ok("Logout effettuato con successo");

	}

}