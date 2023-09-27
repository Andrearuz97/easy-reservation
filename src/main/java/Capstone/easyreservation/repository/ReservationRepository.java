package Capstone.easyreservation.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Capstone.easyreservation.entity.Reservation;
import Capstone.easyreservation.entity.Room;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByStanzaAndDataCheckInLessThanEqualAndDataCheckOutGreaterThanEqual(Room stanza, Date checkIn,
			Date checkOut);

	List<Reservation> findByUtenteIdUser(UUID idUser);

	List<Reservation> findByStanzaAndDataCheckInLessThanEqualAndDataCheckOutGreaterThanEqualAndIdNot(Room stanza,
			Date dataCheckIn, Date dataCheckOut, Long id);

	List<Reservation> findByStanzaAndDataCheckInLessThanEqualAndDataCheckOutGreaterThanEqual(Room stanza,
			LocalDate dataCheckIn, LocalDate dataCheckOut);

	List<Reservation> findByStanzaAndDataCheckInLessThanEqualAndDataCheckOutGreaterThanEqualAndIdNot(Room stanza,
			LocalDate dataCheckIn, LocalDate dataCheckOut, Long id);

}
