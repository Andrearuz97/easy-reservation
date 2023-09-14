package Capstone.easyreservation.payloads;

import java.util.Date;
import java.util.UUID;

public class ReservationPayload {
	private Date dataCheckIn;
	private Date dataCheckOut;
	private UUID utenteId;
	private Long stanzaId;

	public Date getDataCheckIn() {
		return dataCheckIn;
	}

	public void setDataCheckIn(Date dataCheckIn) {
		this.dataCheckIn = dataCheckIn;
	}

	public Date getDataCheckOut() {
		return dataCheckOut;
	}

	public void setDataCheckOut(Date dataCheckOut) {
		this.dataCheckOut = dataCheckOut;
	}

	public UUID getUtenteId() {
		return utenteId;
	}

	public void setUtenteId(UUID utenteId) {
		this.utenteId = utenteId;
	}

	public Long getStanzaId() {
		return stanzaId;
	}

	public void setStanzaId(Long stanzaId) {
		this.stanzaId = stanzaId;
	}
}
