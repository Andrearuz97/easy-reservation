package Capstone.easyreservation.payloads;

import java.time.LocalDate;
import java.util.UUID;

import Capstone.easyreservation.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NuovoUtentePayload {
	private UUID idUser;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String telefono;
	private String citta;
	private String cap;
	private String indirizzo;
	private LocalDate dataDiNascita;
	private UserRole role;

}