package Capstone.easyreservation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Capstone.easyreservation.repository.ReservationRepository;

@Service
public class ReservationService {
	@Autowired
	private ReservationRepository prenotazioneRepository;

}
