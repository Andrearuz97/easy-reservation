package Capstone.easyreservation.payloads;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class ReservationPayload {
	private LocalDate dataCheckIn;
	private LocalDate dataCheckOut;
	private UUID utenteId;
	private Long stanzaId;
	private Double totalPrice;
}
