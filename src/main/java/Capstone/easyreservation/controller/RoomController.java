package Capstone.easyreservation.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Capstone.easyreservation.entity.Hotel;
import Capstone.easyreservation.entity.Room;
import Capstone.easyreservation.exception.NotFoundException;
import Capstone.easyreservation.payloads.RoomPayload;
import Capstone.easyreservation.repository.HotelRepository;
import Capstone.easyreservation.services.RoomService;

@RestController
@RequestMapping("/stanze")
public class RoomController {

	private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
	private RoomService roomService;

    @Autowired
    private HotelRepository hotelRepository;

    @GetMapping("/hotel/{hotelId}")
    public List<Room> getAllRoomsByHotel(@PathVariable Long hotelId) {
		logger.info("Fetching all rooms for hotel with ID: {}", hotelId);
		return roomService.getAllRoomsByHotel(hotelId);
    }

    @GetMapping("/hotel/{hotelId}/{roomId}")
    public Room getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
		logger.info("Fetching room with ID: {} for hotel ID: {}", roomId, hotelId);
		return roomService.getRoomById(roomId);
    }

    @PostMapping("/hotel/{hotelId}")
    public Room createRoom(@PathVariable Long hotelId, @RequestBody RoomPayload roomPayload) {
		Hotel hotel = hotelRepository.findById(hotelId)
				.orElseThrow(() -> new NotFoundException("Hotel with ID " + hotelId + " not found"));

        Room room = new Room();
        room.setNumeroStanza(roomPayload.getNumeroStanza());
        room.setTipo(roomPayload.getTipo());
        room.setPrezzo(roomPayload.getPrezzo());
        room.setHotel(hotel);
		room.setImageUrl(roomPayload.getImageUrl());

		Room savedRoom = roomService.saveRoom(room);
		logger.info("Room created with ID: {}", savedRoom.getId());
		return savedRoom;
    }

    @PutMapping("/hotel/{hotelId}/{roomId}")
    public Room updateRoom(@PathVariable Long hotelId, @PathVariable Long roomId, @RequestBody RoomPayload roomPayload) {
		Room existingRoom = roomService.getRoomById(roomId);

        existingRoom.setNumeroStanza(roomPayload.getNumeroStanza());
        existingRoom.setTipo(roomPayload.getTipo());
        existingRoom.setPrezzo(roomPayload.getPrezzo());
		existingRoom.setImageUrl(roomPayload.getImageUrl());

		Room updatedRoom = roomService.saveRoom(existingRoom);
		logger.info("Room with ID: {} updated", roomId);
		return updatedRoom;
    }

    @DeleteMapping("/hotel/{hotelId}/{roomId}")
	public ResponseEntity<String> deleteRoom(@PathVariable Long hotelId, @PathVariable Long roomId) {
		logger.info("Deleting room with ID: {} for hotel ID: {}", roomId, hotelId);
		roomService.deleteRoom(roomId);
		return ResponseEntity.ok("Room deleted successfully");
    }
}
