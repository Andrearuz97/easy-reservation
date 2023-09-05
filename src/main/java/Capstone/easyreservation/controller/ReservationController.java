package Capstone.easyreservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Capstone.easyreservation.services.ReservationService;

@RestController
@RequestMapping("/prenotazioni")
public class ReservationController {
	@Autowired
	private ReservationService prenotazioneService;

}
