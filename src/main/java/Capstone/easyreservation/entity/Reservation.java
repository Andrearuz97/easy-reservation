package Capstone.easyreservation.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate dataCheckIn;
	private LocalDate dataCheckOut;

	@ManyToOne
	@JoinColumn(name = "utente_id")
	private Utente utente;

	@ManyToOne
	@JoinColumn(name = "stanza_id")
	private Room stanza;
}
