package Capstone.easyreservation.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import Capstone.easyreservation.entity.Utente;
import Capstone.easyreservation.payloads.NuovoUtentePayload;
import Capstone.easyreservation.services.UtenteService;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

	@Autowired
	UtenteService utenteService;

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public Utente saveUtente(@RequestBody NuovoUtentePayload body) {
		Utente createdUser = utenteService.save(body);
		return createdUser;
	}

	@GetMapping("/{userId}")
	public ResponseEntity<Utente> findById(@PathVariable UUID userId) {
		try {
			Utente user = utenteService.findById(userId);
			return ResponseEntity.ok(user);
		} catch (ChangeSetPersister.NotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{userId}")
	public ResponseEntity<Utente> updateUser(@PathVariable UUID userId, @RequestBody NuovoUtentePayload body) {
		try {
			Utente updatedUser = utenteService.findByIdAndUpdate(userId, body);
			return ResponseEntity.ok(updatedUser);
		} catch (ChangeSetPersister.NotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
		try {
			utenteService.findByIdAndDelete(userId);
			return ResponseEntity.noContent().build();
		} catch (ChangeSetPersister.NotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}