package Capstone.easyreservation.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

	@Autowired
	private ReservationService prenotazioneService;

	@Autowired
	private UtenteRepository utenteRepository;

	@Autowired
	private RoomService roomService;

	@GetMapping
	public ResponseEntity<List<Reservation>> getAllReservations() {
		try {
			List<Reservation> allReservations = prenotazioneService.getAllReservations();
			log.info("Recuperate tutte le prenotazioni. Numero di prenotazioni: {}", allReservations.size());
			return new ResponseEntity<>(allReservations, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Errore nel recuperare tutte le prenotazioni: {}", e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
		try {
			Optional<Reservation> reservation = prenotazioneService.getReservationById(id);
			if (reservation.isPresent()) {
				log.info("Recuperata prenotazione con ID: {}", id);
				return new ResponseEntity<>(reservation.get(), HttpStatus.OK);
			} else {
				log.warn("Nessuna prenotazione trovata con ID: {}", id);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Errore nel recuperare la prenotazione con id {}: {}", id, e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable UUID userId) {
		try {
			List<Reservation> userReservations = prenotazioneService.getReservationsByUserId(userId);
			log.info("Recuperate prenotazioni per l'utente con ID: {}. Numero di prenotazioni: {}", userId,
					userReservations.size());
			return new ResponseEntity<>(userReservations, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Errore nel recuperare le prenotazioni per l'utente con id {}: {}", userId, e.toString());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateReservation(@PathVariable Long id,
			@RequestBody ReservationPayload reservationPayload) {
		try {
			// Controlla se le date di check-in o check-out sono mancanti
			if (reservationPayload.getDataCheckIn() == null || reservationPayload.getDataCheckOut() == null) {
				log.warn(
						"Date di check-in o check-out mancanti nella richiesta di aggiornamento prenotazione con ID: {}",
						id);
				return ResponseEntity.badRequest().body("Date di check-in o check-out mancanti.");
			}

			LocalDate today = LocalDate.now();
			LocalDate checkInDate = reservationPayload.getDataCheckIn();
			LocalDate checkOutDate = reservationPayload.getDataCheckOut();

			if (checkInDate.isBefore(today) || checkOutDate.isBefore(checkInDate)
					|| checkOutDate.isEqual(checkInDate)) {
				log.warn("Date di check-in o check-out non valide per la prenotazione con ID: {}", id);
				return ResponseEntity.badRequest().body("Date di check-in o check-out non valide.");
			}

			if (id == null) {
				log.warn("ID prenotazione mancante nella richiesta di aggiornamento.");
				return ResponseEntity.badRequest().body("ID prenotazione mancante.");
			}

			Optional<Reservation> existingReservation = prenotazioneService.getReservationById(id);
			if (existingReservation.isEmpty()) {
				log.error("Nessuna prenotazione trovata con ID: {}", id);
				return ResponseEntity.notFound().build();
			}

			Reservation reservation = existingReservation.get();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String email = auth.getName();
			Optional<Utente> currentUser = utenteRepository.findByEmail(email);

			if (currentUser.isEmpty()) {
				log.error("Utente attualmente autenticato non trovato con email: {}", email);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Utente non trovato.");
			}

			Utente utente = currentUser.get();
			if (!utente.equals(reservation.getUtente()) && !utente.getRole().equals(UserRole.ADMIN)) {
				log.error("Utente con email: {} non autorizzato a modificare la prenotazione con ID: {}", email, id);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Utente non autorizzato.");
			}

			reservation.setDataCheckIn(reservationPayload.getDataCheckIn());
			reservation.setDataCheckOut(reservationPayload.getDataCheckOut());

			if (reservationPayload.getStanzaId() == null) {
				log.warn("ID stanza mancante nella richiesta di aggiornamento prenotazione con ID: {}", id);
				return ResponseEntity.badRequest().body("ID stanza mancante.");
			}

			Optional<Room> room = Optional.ofNullable(roomService.getRoomById(reservationPayload.getStanzaId()));
			if (room.isEmpty()) {
			    log.error("Nessuna stanza trovata con ID: {}", reservationPayload.getStanzaId());
			    return ResponseEntity.notFound().build();
			}
			if (!prenotazioneService.isRoomAvailable(room.get(), reservation.getDataCheckIn(),
					reservation.getDataCheckOut(), id)) {
				log.warn("Stanza con ID: {} non disponibile per le date selezionate nella prenotazione con ID: {}",
						reservationPayload.getStanzaId(), id);
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body("Stanza non disponibile per le date selezionate.");
			}

			double totalPrice = prenotazioneService.calculateTotalPrice(room.get(), reservation.getDataCheckIn(),
					reservation.getDataCheckOut());
			reservation.setTotalPrice(totalPrice);

			reservation.setStanza(room.get());
			Reservation updatedReservation = prenotazioneService.saveReservation(reservation);
			log.info("Prenotazione con ID: {} aggiornata correttamente.", id);
			return ResponseEntity.ok(updatedReservation);
		} catch (Exception e) {
			log.error("Errore nell'aggiornamento della prenotazione con id {}: {}", id, e.toString());
			return ResponseEntity.internalServerError()
					.body("Si è verificato un errore durante l'aggiornamento della prenotazione.");
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
		try {
			boolean isDeleted = prenotazioneService.deleteReservation(id);
			if (isDeleted) {
				log.info("Prenotazione con ID: {} eliminata con successo.", id);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				log.warn("Nessuna prenotazione trovata con ID: {} per l'eliminazione.", id);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Errore durante l'eliminazione della prenotazione con ID: {}", id, e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/prenota")
	public ResponseEntity<?> prenotaStanza(@RequestBody ReservationPayload reservationPayload) {
		try {
			// Verifica se le date sono presenti nel payload
			if (reservationPayload.getDataCheckIn() == null || reservationPayload.getDataCheckOut() == null) {
				log.warn("Le date di check-in o check-out sono mancanti");
				return new ResponseEntity<>("Le date di check-in o check-out sono mancanti", HttpStatus.BAD_REQUEST);
			}

			LocalDate today = LocalDate.now();
			LocalDate checkInDate = reservationPayload.getDataCheckIn();
			LocalDate checkOutDate = reservationPayload.getDataCheckOut();

			// Controllo sulla validità delle date
			if (checkInDate.isBefore(today)) {
				log.warn("Data di check-in antecedente alla data odierna");
				return new ResponseEntity<>("Data di check-in antecedente alla data odierna", HttpStatus.BAD_REQUEST);
			}

			if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
				log.warn("Data di check-out non valida rispetto al check-in");
				return new ResponseEntity<>("Data di check-out non valida rispetto al check-in",
						HttpStatus.BAD_REQUEST);
			}

			// Recupera informazioni sull'utente corrente
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String email = auth.getName();
			Optional<Utente> currentUser = utenteRepository.findByEmail(email);
			if (currentUser.isEmpty()) {
				log.warn("Utente non autorizzato o non trovato");
				return new ResponseEntity<>("Utente non autorizzato", HttpStatus.FORBIDDEN);
			}

			Utente utente;
			if (currentUser.get().getRole().equals(UserRole.ADMIN) && reservationPayload.getUtenteId() != null) {
				Optional<Utente> designatedUser = utenteRepository.findById(reservationPayload.getUtenteId());
				if (designatedUser.isEmpty()) {
					log.warn("Utente specificato non trovato");
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
				log.warn("ID della stanza mancante");
				return new ResponseEntity<>("ID della stanza mancante", HttpStatus.BAD_REQUEST);
			}

			Room room = roomService.getRoomById(reservationPayload.getStanzaId());
			if (room == null) {
				log.warn("Stanza non trovata");
				return new ResponseEntity<>("Stanza non trovata", HttpStatus.NOT_FOUND);
			}

			if (!prenotazioneService.isRoomAvailable(room, reservation.getDataCheckIn(),
					reservation.getDataCheckOut())) {
				log.warn("La stanza non è disponibile per le date selezionate");
				return new ResponseEntity<>("La stanza non è disponibile per le date selezionate", HttpStatus.CONFLICT);
			}

			double totalPrice = prenotazioneService.calculateTotalPrice(room, reservation.getDataCheckIn(),
					reservation.getDataCheckOut());
			reservation.setTotalPrice(totalPrice);
			reservation.setStanza(room);
			Reservation savedReservation = prenotazioneService.saveReservation(reservation);
			return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Errore durante la prenotazione della stanza", e);
			return new ResponseEntity<>("Si è verificato un errore interno", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	}


