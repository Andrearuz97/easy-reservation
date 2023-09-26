package Capstone.easyreservation.payloads;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class ReservationPayload {
	private Date dataCheckIn;
	private Date dataCheckOut;
	private UUID utenteId;
	private Long stanzaId;
}
