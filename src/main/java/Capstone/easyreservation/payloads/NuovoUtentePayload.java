package Capstone.easyreservation.payloads;

import Capstone.easyreservation.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NuovoUtentePayload {

	private String username;
	private String nome;
	private String cognome;
	private String email;
	private String password;
	private UserRole ruolo;

}