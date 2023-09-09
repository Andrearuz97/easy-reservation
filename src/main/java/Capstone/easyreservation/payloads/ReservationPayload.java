package Capstone.easyreservation.payloads;

import java.util.Date;

public class ReservationPayload {
	private Date dataCheckIn;
	private Date dataCheckOut;
	private Long utenteId; // user ID
	private Long stanzaId; // room ID

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

	public Long getUtenteId() {
		return utenteId;
	}

	public void setUtenteId(Long utenteId) {
		this.utenteId = utenteId;
	}

	public Long getStanzaId() {
		return stanzaId;
	}

	public void setStanzaId(Long stanzaId) {
		this.stanzaId = stanzaId;
	}
}
