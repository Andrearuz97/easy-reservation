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

import Capstone.easyreservation.entity.Hotel;
import Capstone.easyreservation.entity.Room;
import Capstone.easyreservation.repository.HotelRepository;
import Capstone.easyreservation.services.RoomService;

@RestController
@RequestMapping("/stanze")
public class RoomController {

    @Autowired
    private RoomService stanzaService;

    @Autowired
    private HotelRepository hotelRepository;

    // Ottieni tutte le stanze di un hotel
    @GetMapping("/hotel/{hotelId}")
    public List<Room> getAllRoomsByHotel(@PathVariable Long hotelId) {
        return stanzaService.getAllRoomsByHotel(hotelId);
    }

    // Ottieni una stanza specifica di un hotel
    @GetMapping("/hotel/{hotelId}/{roomId}")
    public Room getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        Room room = stanzaService.getRoomById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        
        // Controllo se la stanza appartiene all'hotel specificato
        if (!room.getHotel().getId().equals(hotelId)) {
            throw new RuntimeException("Mismatched hotel and room ID");
        }
        
        return room;
    }

    // Crea una stanza per un hotel
    @PostMapping("/hotel/{hotelId}")
    public Room createRoom(@PathVariable Long hotelId, @RequestBody Room room) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new RuntimeException("Hotel not found"));
        room.setHotel(hotel);
        return stanzaService.saveRoom(room);
    }

    // Aggiorna una stanza specifica di un hotel
    @PutMapping("/hotel/{hotelId}/{roomId}")
    public Room updateRoom(@PathVariable Long hotelId, @PathVariable Long roomId, @RequestBody Room room) {
        Room existingRoom = stanzaService.getRoomById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        
        // Controllo se la stanza appartiene all'hotel specificato
        if (!existingRoom.getHotel().getId().equals(hotelId)) {
            throw new RuntimeException("Mismatched hotel and room ID");
        }
        
        room.setId(roomId);
        return stanzaService.saveRoom(room);
    }

    // Elimina una stanza specifica di un hotel
    @DeleteMapping("/hotel/{hotelId}/{roomId}")
    public void deleteRoom(@PathVariable Long hotelId, @PathVariable Long roomId) {
        Room existingRoom = stanzaService.getRoomById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        
        // Controllo se la stanza appartiene all'hotel specificato
        if (!existingRoom.getHotel().getId().equals(hotelId)) {
            throw new RuntimeException("Mismatched hotel and room ID");
        }

        stanzaService.deleteRoom(roomId);
    }
}
