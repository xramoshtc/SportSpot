package ioc.dammdev.SportSpotServer.service;

import ioc.dammdev.SportSpotServer.dto.BookingRequest;
import ioc.dammdev.SportSpotServer.model.Booking;
import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.BookingRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servei per a la gestió de reserves.
 * Centralitza la lògica de validació per evitar col·lisions i errors horaris.
 * * @author Gess Montalbán
 */
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CourtService courtService;

    @Autowired
    private UserService userService;

    /**
     * Crea una nova reserva. 
     * Retorna la reserva si és vàlida, o llança RuntimeException amb el motiu de l'error.
     */
    @Transactional
    public Booking createBooking(Long courtId, Booking booking, String token) {
        // 1. Validació de sessió
        if (!userService.isValidSession(token)) {
            throw new RuntimeException("Sessió no vàlida o caducada.");
        }
        User user = userService.getUserByToken(token);

        // 2. Validació de pista
        Optional<Court> courtOptional = courtService.getCourtById(courtId);
        if (courtOptional.isEmpty()) {
            throw new RuntimeException("La pista seleccionada no existeix.");
        }
        Court court = courtOptional.get();

        // 3. Preparació i càlcul
        booking.setUser(user);
        booking.setCourt(court);
        booking.autoCalculateEndTime();

        // 4. Validacions de Negoci (Horaris i Solapaments)
        // Llançaran excepció amb el missatge exacte per al client
        validateBookingBusinessRules(booking);

        return bookingRepository.save(booking);
    }

    /**
     * Agrupa les validacions de negoci per netejar el mètode principal.
     */
    private void validateBookingBusinessRules(Booking booking) {
        // Rang horari
        int startHour = booking.getDateTime().getHour();
        if (startHour < 8 || startHour >= 24) {
            throw new RuntimeException("L'horari de reserves és de 08:00 a 00:00.");
        }

        // Solapament
        List<Booking> overlaps = bookingRepository.findOverlappingBookings(
                booking.getCourt(), 
                booking.getDateTime(), 
                booking.getEndTime()
        );

        if (!overlaps.isEmpty()) {
            throw new RuntimeException("La pista ja està ocupada en aquest horari.");
        }
    }

    public List<Booking> getMyBookings(String token) {
        if (!userService.isValidSession(token)) return null;
        return bookingRepository.findByUser(userService.getUserByToken(token));
    }

    public List<Booking> getBookingsByCourt(String token, Long courtId) {
        if (!userService.isValidSession(token)) return null;
        return bookingRepository.findByCourtId(courtId);
    }

    public boolean deleteBooking(Long id, String token) {
        if (!userService.isValidSession(token)) return false;
        
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) return false;

        User currentUser = userService.getUserByToken(token);
        if (booking.get().getUser().getId().equals(currentUser.getId()) || userService.isAdmin(token)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Booking updateBooking(Long id, BookingRequest newData, String token) {
        if (!userService.isValidSession(token)) return null;
        User currentUser = userService.getUserByToken(token);

        return bookingRepository.findById(id).map(booking -> {
            if (booking.getUser().getId().equals(currentUser.getId()) || userService.isAdmin(token)) {
                
                booking.setDateTime(LocalDateTime.parse(newData.getDateTime()));
                booking.setDurationHours(newData.getDurationHours());
                booking.autoCalculateEndTime();
                
                // Validem la nova franja
                validateBookingBusinessRules(booking);
                
                return bookingRepository.save(booking);
            }
            return null;
        }).orElse(null);
    }
}