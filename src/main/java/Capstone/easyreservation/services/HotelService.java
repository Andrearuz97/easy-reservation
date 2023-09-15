package Capstone.easyreservation.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Capstone.easyreservation.entity.Hotel;
import Capstone.easyreservation.payloads.HotelPayload;
import Capstone.easyreservation.repository.HotelRepository;
@Service
public class HotelService {

	@Autowired
	private HotelRepository hotelRepository;

	public List<HotelPayload> getAllHotels() {
		// Converti da List<Hotel> a List<HotelPayload>
		return hotelRepository.findAll().stream().map(this::convertToPayload).collect(Collectors.toList());
	}

	public HotelPayload getHotelById(Long id) {
		Hotel hotel = hotelRepository.findById(id).orElse(null);
		return convertToPayload(hotel);
	}

	public List<HotelPayload> searchHotelsByNome(String nome) {
		List<Hotel> hotels = hotelRepository.findByNomeContainingIgnoreCase(nome);
		return hotels.stream().map(this::convertToPayload).collect(Collectors.toList());
	}

	public HotelPayload saveHotel(HotelPayload hotelPayload) {
		Hotel hotel = convertToEntity(hotelPayload);
		hotel = hotelRepository.save(hotel);
		return convertToPayload(hotel);
	}

	public void deleteHotel(Long id) {
		hotelRepository.deleteById(id);
	}

	// Funzioni helper per la conversione tra entità e payload
	private HotelPayload convertToPayload(Hotel hotel) {
		HotelPayload payload = new HotelPayload();
		payload.setId(hotel.getId());
		payload.setNome(hotel.getNome());
		payload.setIndirizzo(hotel.getIndirizzo());
		payload.setDescrizione(hotel.getDescrizione());
		payload.setStelle(hotel.getStelle());
		payload.setImageUrl(hotel.getImageUrl());
		return payload;
	}

	private Hotel convertToEntity(HotelPayload payload) {
		Hotel hotel = new Hotel();
		hotel.setId(payload.getId());
		hotel.setNome(payload.getNome());
		hotel.setIndirizzo(payload.getIndirizzo());
		hotel.setDescrizione(payload.getDescrizione());
		hotel.setStelle(payload.getStelle());
		hotel.setImageUrl(payload.getImageUrl());
		return hotel;
	}
}
