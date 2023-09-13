package Capstone.easyreservation.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Capstone.easyreservation.entity.Reservation;
import Capstone.easyreservation.entity.Room;
import Capstone.easyreservation.entity.Utente;
import Capstone.easyreservation.payloads.ReservationPayload;
import Capstone.easyreservation.repository.UtenteRepository;
import Capstone.easyreservation.services.ReservationService;
import Capstone.easyreservation.services.RoomService;

@RestController
@RequestMapping("/prenotazioni")
public class ReservationController {

	@Autowired
	private ReservationService prenotazioneService;

	@Autowired
	private UtenteRepository utenteRepository;

	@Autowired
	private RoomService roomService;

	@GetMapping
	public List<Reservation> getAllReservations() {
		return prenotazioneService.getAllReservations();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
		Optional<Reservation> reservation = prenotazioneService.getReservationById(id);
		return reservation.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
	}

	@GetMapping("/user/{userId}")
	public List<Reservation> getReservationsByUserId(@PathVariable UUID userId) {
		return prenotazioneService.getReservationsByUserId(userId);
	}



	@PutMapping("/{id}")
	public ResponseEntity<Reservation> updateReservation(@PathVariable Long id,
			@RequestBody ReservationPayload reservationPayload) {
		Optional<Reservation> existingReservation = prenotazioneService.getReservationById(id);
		if (existingReservation.isPresent()) {
			Reservation reservation = existingReservation.get();

			// Verifica che l'utente attualmente autenticato sia l'utente associato alla
			// prenotazione
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String email = auth.getName();
			Optional<Utente> currentUser = utenteRepository.findByEmail(email);

			if (currentUser.isEmpty() || !currentUser.get().equals(reservation.getUtente())) {
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN); // or another appropriate HTTP status
			}

			reservation.setDataCheckIn(reservationPayload.getDataCheckIn());
			reservation.setDataCheckOut(reservationPayload.getDataCheckOut());

			Optional<Room> room = roomService.getRoomById(reservationPayload.getStanzaId());
			room.ifPresent(reservation::setStanza);

			Reservation updatedReservation = prenotazioneService.saveReservation(reservation);
			return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
		if (prenotazioneService.deleteReservation(id)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/prenota")
	public ResponseEntity<Reservation> prenotaStanza(@RequestBody ReservationPayload reservationPayload) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		Optional<Utente> currentUser = utenteRepository.findByEmail(email);

		if (currentUser.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Reservation reservation = new Reservation();
		reservation.setDataCheckIn(reservationPayload.getDataCheckIn());
		reservation.setDataCheckOut(reservationPayload.getDataCheckOut());
		reservation.setUtente(currentUser.get());

		Room stanza = roomService.getRoomById(reservationPayload.getStanzaId())
				.orElseThrow(() -> new RuntimeException("Stanza non trovata"));
		reservation.setStanza(stanza);

		if (prenotazioneService.isRoomAvailable(stanza, reservation.getDataCheckIn(), reservation.getDataCheckOut())) {
			Reservation savedReservation = prenotazioneService.saveReservation(reservation);
			return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}
	}
}
