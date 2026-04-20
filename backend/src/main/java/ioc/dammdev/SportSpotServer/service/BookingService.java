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
    @Transactional
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
        // 3. Comprovem si hi ha conflicte de reserves
    

    // 4. Assignem l'usuari i la pista a la reserva
        booking.setUser(user);
        booking.setCourt(court.get());

        // 5. Guardem (En l'Sprint 3 afegirem la validació de "pista ocupada")
        return bookingRepository.save(booking);
    }

    /**
     * Retorna les reserves de l'usuari loguejat.
     * @param token :token de sessió
     * @return Llista de reserves
     */
    public List<Booking> getMyBookings(String token) {
        if (!userService.isValidSession(token)) return null;
        User user = userService.getUserByToken(token);
        return bookingRepository.findByUser(user);
    }

    /**
     * Retorna totes les reserves d'una pista per veure disponibilitat.
     * @param courtId : id de la pista reservada
     * @return Llista de reserves
     */
    public List<Booking> getBookingsByCourt(String token, Long courtId) {
        // 1. Validem sessió i recuperem l'usuari real del HashMap
        if (!userService.isValidSession(token)) {
            return null;
        }

        // 2. Verifiquem que la pista existeixi
        Optional<Court> court = courtService.getCourtById(courtId);
        if (court.isPresent())
            return bookingRepository.findByCourtId(courtId);
        return null;
    }

    /**
     * Elimina una reserva.
     * @param id : codi de la reserva
     * @param token : codii de la sessió
     * @return true si s'ha eliminat, false si no
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
    
    /**
     * Actualitza una reserva existent.
     * Permet modificar la data, l'hora i la durada d'una reserva. 
     * Valida que qui fa la petició sigui el propietari o un administrador.
     * @param id L'identificador de la reserva a modificar.
     * @param newData Dades de la nova reserva (courtId, dateTime, duration).
     * @param token Token de sessió per verificar la identitat i permisos.
     * @return La reserva actualitzada o null si no existeix o no té permisos.
     */
    public Booking updateBooking(Long id, BookingRequest newData, String token) {
        User currentUser = userService.getUserByToken(token);
        if (currentUser == null) return null;

        return bookingRepository.findById(id).map(booking -> {
            // Verificació de propietat o rol ADMIN
            if (booking.getUser().getId().equals(currentUser.getId()) || 
                currentUser.getRole().equals("ADMIN")) {
                
                // Actualitzem els camps (podríem afegir validació de disponibilitat aquí)
                booking.setDateTime(LocalDateTime.parse(newData.getDateTime()));
                booking.setDurationMinutes(newData.getDurationMinutes());
                
                return bookingRepository.save(booking);
            }
            return null;
        }).orElse(null);
    }
}