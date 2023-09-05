package Capstone.easyreservation.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NuovoUtentePayload {

	private String name;
	private String surname;
	private String email;
	private String password;

}