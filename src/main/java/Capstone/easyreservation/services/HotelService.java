package Capstone.easyreservation.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import Capstone.easyreservation.entity.Hotel;
import Capstone.easyreservation.exception.NotFoundException;
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
		Hotel hotel = hotelRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Hotel with ID " + id + " not found"));
		return convertToPayload(hotel);
	}


	 public Page<HotelPayload> getAllHotels(int page, int size) {
	        Pageable pageable = PageRequest.of(page, size);
	        Page<Hotel> hotelPage = hotelRepository.findAll(pageable);
	        
	        return hotelPage.map(this::convertToPayload);
	    }

	public List<HotelPayload> searchHotels(String nome, String citta) {
		List<Hotel> hotels;

		if ((nome != null && !nome.trim().isEmpty()) || (citta != null && !citta.trim().isEmpty())) {

			hotels = hotelRepository.findByNomeContainingIgnoreCaseOrCittaContainingIgnoreCase(nome, citta);
		} else {
			hotels = Collections.emptyList();
		}

		return hotels.stream().map(this::convertToPayload).collect(Collectors.toList());
	}

	public List<HotelPayload> autocompleteHotelSearch(String query) {
		return hotelRepository.findTop10ByNomeContainingIgnoreCaseOrCittaContainingIgnoreCase(query, query).stream()
				.map(this::convertToPayload).collect(Collectors.toList());
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
		payload.setCitta(hotel.getCitta());
		payload.setCap(hotel.getCap());
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
		hotel.setCitta(payload.getCitta());
		hotel.setCap(payload.getCap());
		hotel.setIndirizzo(payload.getIndirizzo());
		hotel.setDescrizione(payload.getDescrizione());
		hotel.setStelle(payload.getStelle());
		hotel.setImageUrl(payload.getImageUrl());
		return hotel;
	}
}
