package Capstone.easyreservation.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelPayload {
	private Long id;
	private String nome;
	private String indirizzo;
	private String descrizione;
	private Integer stelle;
	private String imageUrl;
}
