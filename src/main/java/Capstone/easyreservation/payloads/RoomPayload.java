package Capstone.easyreservation.payloads;

import Capstone.easyreservation.enums.TipoStanza;

public class RoomPayload {
	private Long id; 
	private String numeroStanza;
	private TipoStanza tipo;
	private Double prezzo;
	private Long hotelId;
	private boolean isNew; 
	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
}
