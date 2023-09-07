package Capstone.easyreservation.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Capstone.easyreservation.entity.Reservation;
import Capstone.easyreservation.entity.Room;
import Capstone.easyreservation.repository.ReservationRepository;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository prenotazioneRepository;
	@Autowired
	private RoomService roomService;

	// Metodo per verificare la disponibilità di una stanza.
	public boolean isRoomAvailable(Room stanza, Date startDate, Date endDate) {
		// Qui andiamo a controllare se esistono prenotazioni per la stessa stanza nelle
		// date specificate
		List<Reservation> existingReservations = prenotazioneRepository
				.findByStanzaAndDataCheckInLessThanEqualAndDataCheckOutGreaterThanEqual(stanza, endDate, startDate);
		return existingReservations.isEmpty();
	}

	// Metodo per salvare una prenotazione.
	public Reservation saveReservation(Reservation reservation) {
		// Carica i dettagli della stanza
		Room room = roomService.getRoomById(reservation.getStanza().getId())
				.orElseThrow(() -> new RuntimeException("Stanza non trovata"));
		reservation.setStanza(room);

		return prenotazioneRepository.save(reservation);
	}
	// Metodo per ottenere tutte le prenotazioni.
	public List<Reservation> getAllReservations() {
		return prenotazioneRepository.findAll();
	}

	// Metodo per ottenere una prenotazione specifica tramite ID.
	public Optional<Reservation> getReservationById(Long id) {
		return prenotazioneRepository.findById(id);
	}

	// Metodo per aggiornare una prenotazione.
	public Optional<Reservation> updateReservation(Long id, Reservation reservationDetails) {
		if (prenotazioneRepository.existsById(id)) {
			reservationDetails.setId(id);
			return Optional.of(prenotazioneRepository.save(reservationDetails));
		}
		return Optional.empty();
	}

	// Metodo per eliminare una prenotazione.
	public boolean deleteReservation(Long id) {
		if (prenotazioneRepository.existsById(id)) {
			prenotazioneRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
