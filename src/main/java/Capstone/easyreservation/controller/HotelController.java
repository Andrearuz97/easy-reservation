package Capstone.easyreservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Capstone.easyreservation.payloads.HotelPayload;
import Capstone.easyreservation.services.HotelService;

@RestController
@RequestMapping("/hotel")
public class HotelController {

	@Autowired
	private HotelService hotelService;

	@GetMapping
	public List<HotelPayload> getAllHotels() {
		return hotelService.getAllHotels();
	}

	@GetMapping("/{id}")
	public HotelPayload getHotelById(@PathVariable Long id) {
		return hotelService.getHotelById(id);
	}

	@PostMapping
	public HotelPayload createHotel(@RequestBody HotelPayload hotelPayload) {
		return hotelService.saveHotel(hotelPayload);
	}

	@PutMapping("/{id}")
	public HotelPayload updateHotel(@PathVariable Long id, @RequestBody HotelPayload hotelPayload) {
		hotelPayload.setId(id);
		return hotelService.saveHotel(hotelPayload);
	}

	@DeleteMapping("/{id}")
	public void deleteHotel(@PathVariable Long id) {
		hotelService.deleteHotel(id);
	}
}