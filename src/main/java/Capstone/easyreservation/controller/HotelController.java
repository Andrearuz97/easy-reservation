package Capstone.easyreservation.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping("/search")
	public ResponseEntity<List<HotelPayload>> searchHotels(
			@RequestParam(required = false) String query) {

		List<HotelPayload> results;

		if (query != null && !query.trim().isEmpty()) {
			results = hotelService.searchHotels(query, query);
		} else {
			results = Collections.emptyList();
		}

		if (results.isEmpty()) {
			return ResponseEntity.notFound().build();
	    } else {
			return ResponseEntity.ok(results);
	    }
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