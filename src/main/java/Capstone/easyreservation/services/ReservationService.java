package Capstone.easyreservation.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
		Room room = roomService.getRoomById(reservation.getStanza().getId())
				.orElseThrow(() -> new RuntimeException("Stanza non trovata"));
		reservation.setStanza(room);

		return prenotazioneRepository.save(reservation);
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
			return Optional.of(prenotazioneRepository.save(reservationDetails));
		}
		return Optional.empty();
	}

	public boolean deleteReservation(Long id) {
		if (prenotazioneRepository.existsById(id)) {
			prenotazioneRepository.deleteById(id);
			return true;
		}
		return false;
	}
}
