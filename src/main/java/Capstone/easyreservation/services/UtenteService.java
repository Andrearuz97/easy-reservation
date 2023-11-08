package Capstone.easyreservation.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Capstone.easyreservation.entity.Utente;
import Capstone.easyreservation.enums.UserRole;
import Capstone.easyreservation.exception.BadRequestException;
import Capstone.easyreservation.exception.NotFoundException;
import Capstone.easyreservation.payloads.NuovoUtentePayload;
import Capstone.easyreservation.repository.UtenteRepository;

@Service
public class UtenteService {

	private static final Logger logger = LoggerFactory.getLogger(UtenteService.class);

	@Autowired
	private UtenteRepository ur;

	// --------------------------------------------------------user save
	public Utente saveUser(NuovoUtentePayload body) {
		ur.findByEmail(body.getEmail()).ifPresent(user -> {
			String errorMessage = String.format("La registrazione non è riuscita: l'email '%s' è già in uso.",
					body.getEmail());
			logger.error(errorMessage);
			throw new BadRequestException(errorMessage);
		});

		// Calcola l'età dell'utente
		LocalDate oggi = LocalDate.now();
		Period periodo = Period.between(body.getDataDiNascita(), oggi);
		int eta = periodo.getYears();

		// Verifica se l'utente è maggiorenne
		if (eta < 18) {
			String errorMessage = "La registrazione non è riuscita: devi essere maggiorenne per registrarti.";
			logger.error(errorMessage);
			throw new BadRequestException(errorMessage);
		}

		// Creazione del nuovo utente
		Utente newUser = Utente.builder().name(body.getName()).surname(body.getSurname()).email(body.getEmail())
				.password(body.getPassword()).role(UserRole.USER).telefono(body.getTelefono()).citta(body.getCitta())
				.indirizzo(body.getIndirizzo()).cap(body.getCap()).dataDiNascita(body.getDataDiNascita()).build();

		Utente savedUser = ur.save(newUser);
		logger.info("Nuovo utente registrato con ID: {}", savedUser.getIdUser());
		return savedUser;
	}

	// --------------------------------------------------------get all users
	public List<Utente> getUsers() {
		List<Utente> users = ur.findAll();
		if (users.isEmpty()) {
			logger.error("Nessun utente trovato nel database.");
			throw new NotFoundException("Nessun utente è stato trovato.");
		}
		logger.info("Trovati tutti gli utenti, totale: {}", users.size());
		return users;
	}

	// --------------------------------------------------------get user by id
	public Utente findById(UUID idUser) {
		return ur.findById(idUser).orElseThrow(() -> {
			String errorMessage = String.format("Utente con ID '%s' non trovato.", idUser);
			logger.error(errorMessage);
			return new NotFoundException(errorMessage);
		});
	}

	// --------------------------------------------------------get user by email
	public Utente findByEmail(String email) {
		return ur.findByEmail(email).orElseThrow(() -> {
			String errorMessage = String.format("Utente con email '%s' non trovato.", email);
			logger.error(errorMessage);
			return new NotFoundException(errorMessage);
		});
	}

	// --------------------------------------------------------modify user by id
	public Utente findByIdAndUpdate(UUID id, NuovoUtentePayload body) {
		Utente foundUser = this.findById(id);

		foundUser.setName(body.getName());
		foundUser.setSurname(body.getSurname());
		foundUser.setEmail(body.getEmail());
		foundUser.setTelefono(body.getTelefono());
		foundUser.setCitta(body.getCitta());
		foundUser.setIndirizzo(body.getIndirizzo());
		foundUser.setCap(body.getCap());
		foundUser.setDataDiNascita(body.getDataDiNascita());

		Utente updatedUser = ur.save(foundUser);
		logger.info("Utente con ID: {} è stato aggiornato", id);
		return updatedUser;
	}

	// --------------------------------------------------------delete user by id
	public void findByIdAndDelete(UUID id) {
		Utente found = this.findById(id);
		ur.delete(found);
		logger.info("Utente con ID: {} è stato eliminato", id);
	}
}
