package Capstone.easyreservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Capstone.easyreservation.entity.Hotel;
import Capstone.easyreservation.services.HotelService;

@RestController
@RequestMapping("/hotels")
public class HotelController {
	@Autowired
	private HotelService hotelService;

	@GetMapping
	public ResponseEntity<List<Hotel>> getAllHotels() {
		return ResponseEntity.ok(hotelService.findAllHotels());
	}

}
