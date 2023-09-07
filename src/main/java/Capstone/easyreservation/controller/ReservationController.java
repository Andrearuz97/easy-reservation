package Capstone.easyreservation.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Capstone.easyreservation.entity.Reservation;
import Capstone.easyreservation.services.ReservationService;


@RestController
@RequestMapping("/prenotazioni")
public class ReservationController {

	@Autowired
	private ReservationService prenotazioneService;
	@GetMapping
	public List<Reservation> getAllReservations() {
		return prenotazioneService.getAllReservations();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
		Optional<Reservation> reservation = prenotazioneService.getReservationById(id);
		if (reservation.isPresent()) {
			return new ResponseEntity<>(reservation.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Reservation> updateReservation(@PathVariable Long id,
			@RequestBody Reservation reservationDetails) {
		Optional<Reservation> reservation = prenotazioneService.updateReservation(id, reservationDetails);
		if (reservation.isPresent()) {
			return new ResponseEntity<>(reservation.get(), HttpStatus.OK);
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
	public ResponseEntity<Reservation> prenotaStanza(@RequestBody Reservation reservation) {
		// Verifica la disponibilità della stanza
		if (prenotazioneService.isRoomAvailable(reservation.getStanza(), reservation.getDataCheckIn(),
				reservation.getDataCheckOut())) {
			// Se disponibile, prenota la stanza
			Reservation savedReservation = prenotazioneService.saveReservation(reservation);
			return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
		} else {
			// Se non disponibile, restituisci un messaggio di errore
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}
	}

	}


