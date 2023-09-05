package Capstone.easyreservation.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Capstone.easyreservation.entity.Hotel;
import Capstone.easyreservation.repository.HotelRepository;

@Service
public class HotelService {
	@Autowired
	private HotelRepository hotelRepository;

	public List<Hotel> findAllHotels() {
		return hotelRepository.findAll();
	}

}
