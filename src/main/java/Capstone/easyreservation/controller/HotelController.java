package Capstone.easyreservation.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Capstone.easyreservation.entity.Hotel;
import Capstone.easyreservation.services.HotelService;

@RestController
@RequestMapping("/hotel")
public class HotelController {

	@Autowired
	private HotelService hotelService;

	@GetMapping
	public List<Hotel> getAllHotels() {
		return hotelService.getAllHotels();
	}

	@GetMapping("/{id}")
	public Optional<Hotel> getHotelById(@PathVariable Long id) {
		return hotelService.getHotelById(id);
	}

	@PostMapping
	public Hotel createHotel(@RequestBody Hotel hotel) {
		System.out.println(hotel);
		return hotelService.saveHotel(hotel);
	}

	@PutMapping("/{id}")
	public Hotel updateHotel(@PathVariable Long id, @RequestBody Hotel hotel) {

		hotel.setId(id);
		return hotelService.saveHotel(hotel);
	}

	@DeleteMapping("/{id}")
	public void deleteHotel(@PathVariable Long id) {
		hotelService.deleteHotel(id);
	}
}