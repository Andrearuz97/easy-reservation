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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String numeroStanza;

	@Enumerated(EnumType.STRING)
	private TipoStanza tipo;

	private Double prezzo;

	private String imageUrl;

	@ManyToOne
	@JoinColumn(name = "hotel_id")
	private Hotel hotel;
}
