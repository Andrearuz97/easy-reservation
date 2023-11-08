package Capstone.easyreservation.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Capstone.easyreservation.entity.Room;
import Capstone.easyreservation.exception.NotFoundException;
import Capstone.easyreservation.repository.RoomRepository;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;

	public List<Room> getAllRoomsByHotel(Long hotelId) {
		return roomRepository.findByHotel_Id(hotelId);
	}

	public Room getRoomById(Long id) {
		return roomRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Room with ID " + id + " not found"));
	}

	public Room saveRoom(Room room) {
		return roomRepository.save(room);
	}

	public void deleteRoom(Long id) {
		if (!roomRepository.existsById(id)) {
			throw new NotFoundException("Room with ID " + id + " not found");
		}
		roomRepository.deleteById(id);
	}
}
