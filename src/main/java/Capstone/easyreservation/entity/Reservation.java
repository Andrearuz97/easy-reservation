package Capstone.easyreservation.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date dataCheckIn;
	private Date dataCheckOut;

	@ManyToOne
	@JoinColumn(name = "utente_id")
	private Utente utente;

	@ManyToOne
	@JoinColumn(name = "stanza_id")
	private Room stanza;

	
	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Room getStanza() {
		return stanza;
	}

	public void setStanza(Room stanza) {
		this.stanza = stanza;
	}

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

}
