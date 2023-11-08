package Capstone.easyreservation.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Capstone.easyreservation.entity.Reservation;
import Capstone.easyreservation.entity.Room;
import Capstone.easyreservation.repository.ReservationRepository;

@Service
public class ReservationService {

	private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

	@Autowired
	private ReservationRepository prenotazioneRepository;
	@Autowired
	private RoomService roomService;

	public Double calculateTotalPrice(Room stanza, LocalDate checkIn, LocalDate checkOut) {
		long daysBetween = ChronoUnit.DAYS.between(checkIn, checkOut);
		return daysBetween * stanza.getPrezzo();
	}

	public boolean isRoomAvailable(Room stanza, LocalDate startDate, LocalDate endDate) {
		return isRoomAvailable(stanza, startDate, endDate, null);
	}

	public boolean isRoomAvailable(Room stanza, LocalDate startDate, LocalDate endDate, Long currentReservationId) {
		List<Reservation> existingReservations;

		if (currentReservationId != null) {
			existingReservations = prenotazioneRepository
					.findByStanzaAndDataCheckInLessThanEqualAndDataCheckOutGreaterThanEqualAndIdNot(stanza, endDate,
							startDate, currentReservationId);
		} else {
			existingReservations = prenotazioneRepository
					.findByStanzaAndDataCheckInLessThanEqualAndDataCheckOutGreaterThanEqual(stanza, endDate, startDate);
		}

		return existingReservations.isEmpty();
	}

	public Reservation saveReservation(Reservation reservation) {
		Room room = roomService.getRoomById(reservation.getStanza().getId());
		reservation.setStanza(room);

		Reservation savedReservation = prenotazioneRepository.save(reservation);
		log.info("Reservation saved with id: {}", savedReservation.getId());
		return savedReservation;
	}



	public List<Reservation> getAllReservations() {
		return prenotazioneRepository.findAll();
	}

	public List<Reservation> getReservationsByUserId(UUID userId) {
		return prenotazioneRepository.findByUtenteIdUser(userId);
	}

	public Optional<Reservation> getReservationById(Long id) {
		return prenotazioneRepository.findById(id);
	}

	public Optional<Reservation> updateReservation(Long id, Reservation reservationDetails) {
		if (prenotazioneRepository.existsById(id)) {
			reservationDetails.setId(id);
			Reservation updatedReservation = prenotazioneRepository.save(reservationDetails);
			log.info("Reservation updated with id: {}", id);
			return Optional.of(updatedReservation);
		} else {
			log.warn("Attempted to update a reservation that does not exist with id: {}", id);
			return Optional.empty();
		}
	}

	public boolean deleteReservation(Long id) {
		if (prenotazioneRepository.existsById(id)) {
			prenotazioneRepository.deleteById(id);
			log.info("Reservation deleted with id: {}", id);
			return true;
		} else {
			log.warn("Attempted to delete a reservation that does not exist with id: {}", id);
			return false;
		}
	}

	}

