package Capstone.easyreservation.controller;

import java.time.LocalDate;
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
import Capstone.easyreservation.enums.UserRole;
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

		// Controlla se le date di check-in o check-out sono mancanti
		if (reservationPayload.getDataCheckIn() == null || reservationPayload.getDataCheckOut() == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		LocalDate today = LocalDate.now();
		LocalDate checkInDate = reservationPayload.getDataCheckIn();
		LocalDate checkOutDate = reservationPayload.getDataCheckOut();

		if (checkInDate.isBefore(today)) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		if (id == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		Optional<Reservation> existingReservation = prenotazioneService.getReservationById(id);
		if (existingReservation.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		Reservation reservation = existingReservation.get();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		Optional<Utente> currentUser = utenteRepository.findByEmail(email);

		if (currentUser.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		Utente utente = currentUser.get();
		if (!utente.equals(reservation.getUtente()) && !utente.getRole().equals(UserRole.ADMIN)) {
			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		}

		reservation.setDataCheckIn(reservationPayload.getDataCheckIn());
		reservation.setDataCheckOut(reservationPayload.getDataCheckOut());

		if (reservationPayload.getStanzaId() == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

		Optional<Room> room = roomService.getRoomById(reservationPayload.getStanzaId());
		if (room.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		if (!prenotazioneService.isRoomAvailable(room.get(), reservation.getDataCheckIn(),
				reservation.getDataCheckOut(), id)) {
			return new ResponseEntity<>(null, HttpStatus.CONFLICT);
		}

		reservation.setStanza(room.get());
		Reservation updatedReservation = prenotazioneService.saveReservation(reservation);
		return new ResponseEntity<>(updatedReservation, HttpStatus.OK);
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
	public ResponseEntity<?> prenotaStanza(@RequestBody ReservationPayload reservationPayload) {

		// Verifica se le date sono presenti nel payload
		if (reservationPayload.getDataCheckIn() == null || reservationPayload.getDataCheckOut() == null) {
			return new ResponseEntity<>("Le date di check-in o check-out sono mancanti", HttpStatus.BAD_REQUEST);
		}

		LocalDate today = LocalDate.now();
		LocalDate checkInDate = reservationPayload.getDataCheckIn();

		LocalDate checkOutDate = reservationPayload.getDataCheckOut();

		// Controllo sulla validità delle date
		if (checkInDate.isBefore(today)) {
			return new ResponseEntity<>("Data di check-in antecedente alla data odierna", HttpStatus.BAD_REQUEST);
		}
		if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
			return new ResponseEntity<>("Data di check-out non valida rispetto al check-in", HttpStatus.BAD_REQUEST);
		}

		// Recupera informazioni sull'utente corrente
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		Optional<Utente> currentUser = utenteRepository.findByEmail(email);
		if (currentUser.isEmpty()) {
			return new ResponseEntity<>("Utente non autorizzato", HttpStatus.FORBIDDEN);
		}

		Utente utente;
		if (currentUser.get().getRole().equals(UserRole.ADMIN) && reservationPayload.getUtenteId() != null) {
			Optional<Utente> designatedUser = utenteRepository.findById(reservationPayload.getUtenteId());
			if (designatedUser.isEmpty()) {
				return new ResponseEntity<>("Utente specificato non trovato", HttpStatus.NOT_FOUND);
			}
			utente = designatedUser.get();
		} else {
			utente = currentUser.get();
		}

		// Creazione della prenotazione
		Reservation reservation = new Reservation();
		reservation.setDataCheckIn(reservationPayload.getDataCheckIn());
		reservation.setDataCheckOut(reservationPayload.getDataCheckOut());
		reservation.setUtente(utente);

		if (reservationPayload.getStanzaId() == null) {
			return new ResponseEntity<>("ID della stanza mancante", HttpStatus.BAD_REQUEST);
		}

		Optional<Room> room = roomService.getRoomById(reservationPayload.getStanzaId());
		if (room.isEmpty()) {
			return new ResponseEntity<>("Stanza non trovata", HttpStatus.NOT_FOUND);
		}

		if (!prenotazioneService.isRoomAvailable(room.get(), reservation.getDataCheckIn(),
				reservation.getDataCheckOut())) {
			return new ResponseEntity<>("La stanza non è disponibile per le date selezionate", HttpStatus.CONFLICT);
		}

		reservation.setStanza(room.get());
		Reservation savedReservation = prenotazioneService.saveReservation(reservation);
		return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
	}
}
