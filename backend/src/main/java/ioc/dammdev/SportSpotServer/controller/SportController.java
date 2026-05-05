package ioc.dammdev.SportSpotServer.controller;

import ioc.dammdev.SportSpotServer.dto.BookingRequest;
import ioc.dammdev.SportSpotServer.dto.BookingResponse;
import ioc.dammdev.SportSpotServer.dto.CourtDTO;
import ioc.dammdev.SportSpotServer.model.Booking;
import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.service.BookingService;
import ioc.dammdev.SportSpotServer.service.CourtService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Controlador principal per a l'activitat esportiva (Pistes i Reserves).
 * Centralitza els endpoints de gestió de pistes i reserves.
 * Inclou gestió d'excepcions de negoci per retornar missatges d'error al client.
 * @author Gess Montalbán
 */
@RestController
@RequestMapping("/api")
public class SportController {

    @Autowired
    private CourtService courtService;

    @Autowired
    private BookingService bookingService;

    // --- ENDPOINTS DE PISTES ---

    @GetMapping("/courts")
    public ResponseEntity<List<CourtDTO>> getAllCourts() {
        List<CourtDTO> courts = courtService.getAllCourts().stream()
                .map(this::mapToCourtDTO)
                .toList();
        return ResponseEntity.ok(courts);
    }

    @PostMapping("/courts")
    public ResponseEntity<CourtDTO> createCourt(@RequestHeader("Session-Token") String token, 
                                               @RequestBody Court court) {
        Court newCourt = courtService.createCourt(court, token);
        if (newCourt == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToCourtDTO(newCourt));
    }
    
    @PutMapping("/courts/{id}")
    public ResponseEntity<CourtDTO> updateCourt(@RequestHeader("Session-Token") String token, 
                                               @PathVariable Long id, 
                                               @RequestBody Court courtData) {
        Court updated = courtService.updateCourt(id, courtData, token);
        if (updated == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(mapToCourtDTO(updated));
    }

    @DeleteMapping("/courts/{id}")
    public ResponseEntity<Void> deleteCourt(@RequestHeader("Session-Token") String token, 
                                           @PathVariable Long id) {
        boolean deleted = courtService.deleteCourt(id, token);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    
    @GetMapping("/courts/{id}/bookings")
    public ResponseEntity<List<BookingResponse>> getCourtBookingsByDate(@RequestHeader("Session-Token") String token,
                                                                    @PathVariable Long id,
                                                                    @RequestParam(name = "date", required = false)
                                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
                                                                    ){
        
        List<Booking> existingBookings = bookingService.getBookingsByCourt(token, id);
        if (existingBookings == null || existingBookings.isEmpty())
           return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        
        List<BookingResponse> filteredBookings = existingBookings.stream()
                       .filter(b -> {
                            if (date == null) return true;
                            return date.equals(b.getDateTime());
                            })
                        .map(b -> {
                            BookingResponse res = this.mapToBookingResponse(b);
                            res.setUserName(null); // Privacitat: No mostrem qui ha reservat a altres usuaris
                            return res;
                                 })
                        .toList();
        return ResponseEntity.ok(filteredBookings); 
    }


    // --- ENDPOINTS DE RESERVES ---

    /**
     * Crea una nova reserva. Captura errors de negoci (solapament, horari) i retorna 400 Bad Request.
     */
    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestHeader("Session-Token") String token,
                                                         @RequestBody BookingRequest request) {
        try {
            // Convertim el RequestDTO a Entitat temporal per al Service
            Booking bookingTemplate = new Booking();
            bookingTemplate.setDateTime(LocalDateTime.parse(request.getDateTime()));
            bookingTemplate.setDurationHours(request.getDurationHours());

            Booking savedBooking = bookingService.createBooking(request.getCourtId(), bookingTemplate, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToBookingResponse(savedBooking));
            
        } catch (RuntimeException e) {
            // Retornem el missatge d'error de negoci (ex: "Pista ocupada") directament al client
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error intern al processar la reserva.");
        }
    }

    @GetMapping("/bookings/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings(@RequestHeader("Session-Token") String token) {
        List<Booking> bookings = bookingService.getMyBookings(token);
        if (bookings == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(bookings.stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@RequestHeader("Session-Token") String token, 
                                             @PathVariable Long id) {
        boolean deleted = bookingService.deleteBooking(id, token);
        if (!deleted) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Actualitza una reserva. També gestiona errors de disponibilitat.
     */
    @PutMapping("/bookings/{id}")
    public ResponseEntity<?> updateBooking(@RequestHeader("Session-Token") String token,
                                                         @PathVariable Long id,
                                                         @RequestBody BookingRequest request) {
        try {
            Booking updated = bookingService.updateBooking(id, request, token);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(mapToBookingResponse(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // --- MÈTODES PRIVATS DE MAPPING ---

    private CourtDTO mapToCourtDTO(Court court) {
        CourtDTO dto = new CourtDTO();
        dto.setId(court.getId());
        dto.setName(court.getName());
        dto.setType(court.getType());
        dto.setLocation(court.getLocation());
        dto.setPricePerHour(court.getPricePerHour());
        dto.setCapacity(court.getCapacity());
        return dto;
    }

    private BookingResponse mapToBookingResponse(Booking b) {
        BookingResponse res = new BookingResponse();
        res.setId(b.getId());
        res.setDateTime(b.getDateTime());
        res.setEndTime(b.getEndTime()); // Afegim el camp calculat a la resposta
        res.setDurationHours(b.getDurationHours());
        res.setUserName(b.getUser().getName());
        res.setCourtName(b.getCourt().getName());
        res.setLocation(b.getCourt().getLocation());
        return res;
    }
}