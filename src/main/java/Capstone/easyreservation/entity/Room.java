package Capstone.easyreservation.entity;

import jakarta.persistence.Entity;
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
	private String tipo; // es. Singola, Doppia, Suite
	private Double prezzo;

	@ManyToOne
	@JoinColumn(name = "hotel_id")
	private Hotel hotel;
}