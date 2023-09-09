package Capstone.easyreservation.entity;

import Capstone.easyreservation.enums.TipoStanza;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String numeroStanza;
	@Enumerated(EnumType.STRING)
	private TipoStanza tipo;
	private Double prezzo;

	@ManyToOne
	@JoinColumn(name = "hotel_id")
	private Hotel hotel;



	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroStanza() {
		return numeroStanza;
	}

	public void setNumeroStanza(String numeroStanza) {
		this.numeroStanza = numeroStanza;
	}

	public TipoStanza getTipo() {
		return tipo;
	}

	public void setTipo(TipoStanza tipo) {
		this.tipo = tipo;
	}

	public Double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Double prezzo) {
		this.prezzo = prezzo;
	}

}