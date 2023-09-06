package Capstone.easyreservation.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Capstone.easyreservation.entity.Room;
import Capstone.easyreservation.repository.HotelRepository;
import Capstone.easyreservation.repository.RoomRepository;

@Service
public class RoomService {
	@Autowired
	private RoomRepository stanzaRepository;
	@Autowired
	private HotelRepository hotelRepository;

	public List<Room> getAllRoomsByHotel(Long hotelId) {
		return stanzaRepository.findByHotel_Id(hotelId);

	}

	// Ottieni una stanza specifica
	public Optional<Room> getRoomById(Long id) {
		return stanzaRepository.findById(id);
	}

	public Room saveRoom(Room room) {
		return stanzaRepository.save(room);
	}





	// Elimina una stanza
	public void deleteRoom(Long id) {
		stanzaRepository.deleteById(id);
	}
}
