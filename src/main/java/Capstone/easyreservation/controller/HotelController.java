package Capstone.easyreservation.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    private final HotelService hotelService;
    private static final Logger log = LoggerFactory.getLogger(HotelController.class);

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<HotelPayload>> getAllHotels() {
        log.info("Richiesta di ottenere tutti gli hotel");
        List<HotelPayload> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelPayload> getHotelById(@PathVariable Long id) {
        log.info("Richiesta di ottenere l'hotel con ID: {}", id);
        HotelPayload hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<HotelPayload>> getAllHotelsPaged(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Richiesta di ottenere gli hotel paginati: pagina {}, dimensione {}", page, size);
        Page<HotelPayload> hotelsPage = hotelService.getAllHotels(page, size);
        return ResponseEntity.ok(hotelsPage);
    }

    @GetMapping("/search")
    public ResponseEntity<List<HotelPayload>> searchHotels(@RequestParam(required = false) String query) {
        log.info("Ricerca hotel con query: {}", query);
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(List.of());
        }
        
        List<HotelPayload> results = hotelService.searchHotels(query, query);
        return results.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(results);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<List<HotelPayload>> autocompleteHotels(@RequestParam String query) {
        log.info("Autocompletamento ricerca hotel con query: {}", query);
        if (query.length() < 3) {
            return ResponseEntity.badRequest().build();
        }
        
        List<HotelPayload> hotels = hotelService.autocompleteHotelSearch(query);
        return ResponseEntity.ok(hotels);
    }

    @PostMapping
    public ResponseEntity<HotelPayload> createHotel(@RequestBody HotelPayload hotelPayload) {
        log.info("Richiesta di creare un nuovo hotel");
        HotelPayload newHotel = hotelService.saveHotel(hotelPayload);
        return new ResponseEntity<>(newHotel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelPayload> updateHotel(@PathVariable Long id, @RequestBody HotelPayload hotelPayload) {
        log.info("Richiesta di aggiornare l'hotel con ID: {}", id);
        hotelPayload.setId(id);
        HotelPayload updatedHotel = hotelService.saveHotel(hotelPayload);
        return ResponseEntity.ok(updatedHotel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        log.info("Richiesta di eliminare l'hotel con ID: {}", id);
        hotelService.deleteHotel(id);
        return ResponseEntity.ok().build();
    }
}
