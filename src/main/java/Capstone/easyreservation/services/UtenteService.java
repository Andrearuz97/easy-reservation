package Capstone.easyreservation.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import Capstone.easyreservation.entity.Utente;
import Capstone.easyreservation.exception.BadRequestException;
import Capstone.easyreservation.payloads.NuovoUtentePayload;
import Capstone.easyreservation.repository.UtenteRepository;

@Service
public class UtenteService {

	@Autowired
	UtenteRepository utenteRepository;

	public Utente save(NuovoUtentePayload body) {
		utenteRepository.findByEmail(body.getEmail()).ifPresent(utente -> {
			throw new BadRequestException("L'email " + body.getEmail() + " è gia stata utilizzata");
		});
		Utente newUtente = new Utente(body.getUsername(), body.getNome(), body.getCognome(), body.getEmail(),
				body.getPassword(), body.getRuolo());
		return utenteRepository.save(newUtente);
	}

	public List<Utente> getUsers() {
		return utenteRepository.findAll();
	}

	public Utente findById(UUID id) throws ChangeSetPersister.NotFoundException {
		return utenteRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
	}

	public Utente findByIdAndUpdate(UUID id, NuovoUtentePayload body) throws ChangeSetPersister.NotFoundException {
		Utente found = this.findById(id);
		found.setNome(body.getNome());
		found.setCognome(body.getCognome());
		found.setEmail(body.getEmail());
		return utenteRepository.save(found);
	}

	public void findByIdAndDelete(UUID id) throws ChangeSetPersister.NotFoundException {
		Utente found = this.findById(id);
		utenteRepository.delete(found);
	}

	public Utente findByEmail(String email) throws ChangeSetPersister.NotFoundException {
		return utenteRepository.findByEmail(email).orElseThrow(ChangeSetPersister.NotFoundException::new);
	}
}
