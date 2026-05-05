package ioc.dammdev.SportSpotServer.repository;

import ioc.dammdev.SportSpotServer.model.Booking;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.model.Court;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositori per a l'entitat Booking.
 * Permet gestionar les consultes de reserves a la base de dades.
 * @author Gess Montalbán
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Recupera totes les reserves d'un usuari concret.
     * Implementa el requisit de retornar llistes (Iterable).
     * @param user L'objecte usuari propietari de les reserves.
     * @return Llista de reserves de l'usuari.
     */
    List<Booking> findByUser(User user);

    /**
     * Recupera totes les reserves associades a una pista.
     * Serveix per comprovar la disponibilitat d'una instal·lació.
     * @param court L'objecte pista.
     * @return Llista de reserves de la pista.
     */
    List<Booking> findByCourt(Court court);
    
    /**
     * Alternativa per buscar per ID de pista directament.
     * @param courtId ID de la pista.
     * @return Llista de reserves de la pista.
     */
    List<Booking> findByCourtId(Long courtId);
    
    // Mètode per trobar conflictes de reserves
    
    boolean existsByCourtAndDateTime(Court court, LocalDateTime dateTime);
    
    /**
     * Cerca qualsevol reserva existent que coincideixi amb la pista i se solapi 
     * amb l'interval de temps proporcionat.
     * 
     * Lògica JPQL: (IniciNova < FinalBD) AND (FinalNova > IniciBD)
     * On FinalBD es calcula com (b.startTime + b.durationHours)
     * 
     * @param court La pista a consultar.
     * @param start Hora d'inici de la nova reserva.
     * @param end Hora de finalització de la nova reserva (start + durationHours).
     * @return Llista de reserves que col·lideixen.
     */
    @Query("SELECT b FROM Booking b WHERE b.court = :court " +
           "AND :start < b.endTime " +
           "AND :end > b.dateTime")
    List<Booking> findOverlappingBookings(
        @Param("court") Court court, 
        @Param("start") LocalDateTime start, 
        @Param("end") LocalDateTime end
    );

    
}