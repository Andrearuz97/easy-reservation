package Capstone.easyreservation.payloads;

import Capstone.easyreservation.enums.TipoStanza;
import lombok.Data;

@Data
public class RoomPayload {
	private Long id; 
	private String numeroStanza;
	private TipoStanza tipo;
	private Double prezzo;
	private Long hotelId;
	private boolean isNew; 
	private String imageUrl;
}
