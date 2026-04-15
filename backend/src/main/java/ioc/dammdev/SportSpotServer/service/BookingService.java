package ioc.dammdev.SportSpotServer.service;

import ioc.dammdev.SportSpotServer.model.Booking;
import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servei per a la gestió de reserves.
 * Connecta usuaris i pistes validant la sessió activa.
 * @author Gess Montalbán
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
     * @param courtId ID de la pista.
     * @param booking Obtecte amb la data i durada.
     * @param token Token de l'usuari que fa la reserva.
     * @return La reserva guardada o null si falla algun requisit.
     */
    public Booking createBooking(Long courtId, Booking booking, String token) {
        // 1. Validem sessió i recuperem l'usuari real del HashMap
        if (!userService.isValidSession(token)) {
            return null;
        }
        User user = userService.getUserByToken(token);

        // 2. Verifiquem que la pista existeixi
        Optional<Court> court = courtService.getCourtById(courtId);
        if (court.isEmpty()) {
            return null;
        }

        // 3. Assignem l'usuari i la pista a la reserva
        booking.setUser(user);
        booking.setCourt(court.get());

        // 4. Guardem (En l'Sprint 3 afegirem la validació de "pista ocupada")
        return bookingRepository.save(booking);
    }

    /**
     * Retorna les reserves de l'usuari loguejat.
     */
    public List<Booking> getMyBookings(String token) {
        if (!userService.isValidSession(token)) return null;
        User user = userService.getUserByToken(token);
        return bookingRepository.findByUser(user);
    }

    /**
     * Retorna totes les reserves d'una pista per veure disponibilitat.
     */
    public List<Booking> getBookingsByCourt(Long courtId) {
        return bookingRepository.findByCourtId(courtId);
    }

    /**
     * Elimina una reserva.
     */
    public boolean deleteBooking(Long id, String token) {
        if (!userService.isValidSession(token)) return false;
        
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) return false;

        // Seguretat: Només l'amo de la reserva o un ADMIN pot esborrar
        User currentUser = userService.getUserByToken(token);
        if (booking.get().getUser().getId().equals(currentUser.getId()) || userService.isAdmin(token)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}