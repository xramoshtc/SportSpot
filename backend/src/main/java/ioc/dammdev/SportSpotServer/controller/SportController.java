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
 * Centralitza els 6 endpoints definits a la Wiki.
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
    
    /**
     * Actualitza les dades d'una pista existent.
     * Només accessible per a usuaris amb rol ADMIN.
     * @param token El token de sessió de l'administrador.
     * @param id L'identificador de la pista a modificar.
     * @param courtData Objecte amb les noves dades de la pista.
     * @return ResponseEntity amb el CourtDTO actualitzat (200 OK) o 403 Forbidden.
     */
    @PutMapping("/courts/{id}")
    public ResponseEntity<CourtDTO> updateCourt(@RequestHeader("Session-Token") String token, 
                                               @PathVariable Long id, 
                                               @RequestBody Court courtData) {
        Court updated = courtService.updateCourt(id, courtData, token);
        if (updated == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(mapToCourtDTO(updated));
    }

    /**
     * Elimina una pista del sistema de forma permanent.
     * Operació restringida a administradors.
     * @param token El token de sessió de l'administrador.
     * @param id L'identificador de la pista a esborrar.
     * @return 204 No Content si s'ha esborrat, o 403 Forbidden si no té permisos.
     */
    @DeleteMapping("/courts/{id}")
    public ResponseEntity<Void> deleteCourt(@RequestHeader("Session-Token") String token, 
                                           @PathVariable Long id) {
        boolean deleted = courtService.deleteCourt(id, token);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    
       /**
     * Obté la llista de reserves d'una pista. Si s'inclou la data filtrarà per dia.
     * @param token El token de sessió de l'administrador.
     * @param id L'identificador de la pista a consultar.
     * @param date
     * @return ResponseEntity amb la llista de reserves (200 OK), 204 No Content si no hi ha reserves.
     */
    @GetMapping("/courts/{id}/bookings")
    public ResponseEntity<List<BookingResponse>> getCourtBookingsByDate(@RequestHeader("Session-Token") String token,
                                                                    @PathVariable Long id,
                                                                    @RequestParam(name = "date", required = false)
                                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
                                                                    ){
        
        //1. Obtenim les reserves de la pista
        List<Booking> existingBookings = bookingService.getBookingsByCourt(token,id);
        if (existingBookings == null || existingBookings.isEmpty())
           return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        
        //2. Filtrem per data exacta
        List<BookingResponse> filteredBookings = existingBookings.stream()
                                                        .filter(b -> date == null || b.getDateTime().equals(date))
                                                        .map(b -> {
                                                            BookingResponse res = this.mapToBookingResponse(b);
                                                            res.setUserName(null); //No retornem l'usuari de la reserva per privacitat
                                                            return res;
                                                                })
                                                        .toList();
        return ResponseEntity.ok(filteredBookings); 
               
        
        
    }


    // --- ENDPOINTS DE RESERVES ---
    /**
     * Crea una nova reserva d'una pista. Si s'inclou la data filtrarà per dia.
     * @param token El token de sessió de l'administrador.
     * @param request Reserva a desar
     * @return ResponseEntity amb la reserva (200 OK), 204 No Content si no hi ha reserves.
     */
    @PostMapping("/bookings")
    public ResponseEntity<BookingResponse> createBooking(@RequestHeader("Session-Token") String token,
                                                        @RequestBody BookingRequest request) {
        // Convertim el RequestDTO a Entitat temporal per al Service
        Booking bookingTemplate = new Booking();
        bookingTemplate.setDateTime(java.time.LocalDateTime.parse(request.getDateTime()));
        bookingTemplate.setDurationMinutes(request.getDurationMinutes());

        Booking savedBooking = bookingService.createBooking(request.getCourtId(), bookingTemplate, token);
        
        if (savedBooking == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToBookingResponse(savedBooking));
    }

    @GetMapping("/bookings/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings(@RequestHeader("Session-Token") String token) {
        List<Booking> bookings = bookingService.getMyBookings(token);
        if (bookings == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(bookings.stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList()));
    }
    
  

   
    /**
     * Cancel·la una reserva existent.
     * Un client només pot esborrar les seves pròpies reserves.
     * Un administrador pot esborrar qualsevol reserva del sistema.
     * @param token El token de sessió de l'usuari o administrador.
     * @param id L'identificador de la reserva a cancel·lar.
     * @return 204 No Content si s'ha cancel·lat correctament, o 403 Forbidden.
     */
    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@RequestHeader("Session-Token") String token, 
                                             @PathVariable Long id) {
        boolean deleted = bookingService.deleteBooking(id, token);
        if (!deleted) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Modifica una reserva existent.
     * Útil per canviar l'horari o la durada de l'esdeveniment reservat.
     * @param token El token de sessió de l'usuari (Header).
     * @param id L'ID de la reserva a la URL.
     * @param request Objecte JSON amb les noves dades.
     * @return ResponseEntity amb la BookingResponse actualitzada o 403 Forbidden.
     */
    @PutMapping("/bookings/{id}")
    public ResponseEntity<BookingResponse> updateBooking(@RequestHeader("Session-Token") String token,
                                                        @PathVariable Long id,
                                                        @RequestBody BookingRequest request) {
        Booking updated = bookingService.updateBooking(id, request, token);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(mapToBookingResponse(updated));
    }

    // --- MÈTODES PRIVATS DE MAPPING (Per no enviar objectes complets) ---

    private CourtDTO mapToCourtDTO(Court court) {
        CourtDTO dto = new CourtDTO();
        dto.setId(court.getId());
        dto.setName(court.getName());
        dto.setType(court.getType());
        dto.setLocation(court.getLocation());
        dto.setPricePerHour(court.getPricePerHour());
        return dto;
    }

    private BookingResponse mapToBookingResponse(Booking b) {
        BookingResponse res = new BookingResponse();
        res.setId(b.getId());
        res.setDateTime(b.getDateTime());
        res.setDurationMinutes(b.getDurationMinutes());
        res.setUserName(b.getUser().getName());
        res.setCourtName(b.getCourt().getName());
        res.setLocation(b.getCourt().getLocation());
        return res;
    }
}