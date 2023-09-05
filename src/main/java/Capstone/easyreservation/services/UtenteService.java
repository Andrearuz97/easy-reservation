package Capstone.easyreservation.services;

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

		Utente newUser = Utente.builder().name(body.getName()).surname(body.getSurname()).email(body.getEmail())
				.password(body.getPassword()).role(UserRole.USER).build();

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

		return ur.save(foundUser);
	}

	// --------------------------------------------------------delete user by id
	public void findByIdAndDelete(UUID id) throws NotFoundException {
		Utente found = this.findById(id);
		ur.delete(found);
	}

}