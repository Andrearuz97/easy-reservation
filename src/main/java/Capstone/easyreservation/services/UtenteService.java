package Capstone.easyreservation.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

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

	@Autowired
	UtenteRepository ur;



	// --------------------------------------------------------user save

	public Utente saveUser(NuovoUtentePayload body) {

		ur.findByEmail(body.getEmail()).ifPresent(User -> {
			throw new BadRequestException("Email " + body.getEmail() + " è già stata utilizzata");
		});
		// Calcola l'età dell'utente
		LocalDate oggi = LocalDate.now();
		Period periodo = Period.between(body.getDataDiNascita(), oggi);
		int eta = periodo.getYears();

		// Verifica se l'utente è maggiorenne
		if (eta < 18) {
			throw new BadRequestException("Devi essere maggiorenne per registrarti.");
		}

		Utente newUser = Utente.builder().name(body.getName()).surname(body.getSurname()).email(body.getEmail())
				.password(body.getPassword()).role(UserRole.USER).telefono(body.getTelefono()).citta(body.getCitta())
				.indirizzo(body.getIndirizzo()).cap(body.getCap()).dataDiNascita(body.getDataDiNascita()).build();

		return ur.save(newUser);
	}


	// --------------------------------------------------------get all users
	public List<Utente> getUsers() {
		return ur.findAll();
	}

	// --------------------------------------------------------get user by id
	public Utente findById(UUID idUser) {
		return ur.findById(idUser).orElseThrow(() -> new NotFoundException(idUser));
	}

	// --------------------------------------------------------get user by email
	public Utente findByEmail(String email) {
		return ur.findByEmail(email).orElseThrow(() -> new NotFoundException(email));
	}

	// --------------------------------------------------------modify user by id
	public Utente findByIdAndUpdate(UUID id, NuovoUtentePayload body) throws NotFoundException {

		Utente foundUser = this.findById(id);

		foundUser.setName(body.getName());
		foundUser.setSurname(body.getSurname());
		foundUser.setEmail(body.getEmail());
		foundUser.setTelefono(body.getTelefono());
		foundUser.setCitta(body.getCitta());
		foundUser.setIndirizzo(body.getIndirizzo());
		foundUser.setCap(body.getCap());
		foundUser.setDataDiNascita(body.getDataDiNascita());


		return ur.save(foundUser);
	}


	// --------------------------------------------------------delete user by id
	public void findByIdAndDelete(UUID id) throws NotFoundException {
		Utente found = this.findById(id);
		ur.delete(found);
	}

}