package Capstone.easyreservation.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import Capstone.easyreservation.entity.Utente;
import Capstone.easyreservation.payloads.NuovoUtentePayload;
import Capstone.easyreservation.services.UtenteService;

@RestController
@RequestMapping("/users")
public class UtenteController {

	@Autowired
	private UtenteService us;

	@GetMapping("")
	public List<NuovoUtentePayload> getUsers() {
		List<Utente> users = us.getUsers();
		return convertToPayloadList(users);
	}

	@GetMapping("/{idUser}")
	public NuovoUtentePayload findById(@PathVariable UUID idUser) {
		Utente user = us.findById(idUser);
		return convertToPayload(user);
	}

	@GetMapping("/by-email/{email}")
	public NuovoUtentePayload findByEmail(@PathVariable String email) {
		Utente user = us.findByEmail(email);
		return convertToPayload(user);
	}

	@PutMapping("/{idUser}")
	public NuovoUtentePayload updateUser(@PathVariable UUID idUser, @RequestBody NuovoUtentePayload body) {
		Utente updatedUser = us.findByIdAndUpdate(idUser, body);
		return convertToPayload(updatedUser);
	}

	@DeleteMapping("/{idUser}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable UUID idUser) {
		us.findByIdAndDelete(idUser);
	}

	private NuovoUtentePayload convertToPayload(Utente user) {
		return new NuovoUtentePayload(user.getIdUser(), user.getName(), user.getSurname(), user.getEmail(),
				user.getPassword(), user.getTelefono(), user.getCitta(), user.getCap(), user.getIndirizzo(),
				user.getDataDiNascita(), user.getRole());

	}



	private List<NuovoUtentePayload> convertToPayloadList(List<Utente> users) {
		return users.stream().map(this::convertToPayload).collect(Collectors.toList());
	}

}
