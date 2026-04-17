/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.repository;



import ioc.dammdev.SportSpotServer.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositori per a l'entitat Event.
 * Proporciona l'accés a dades per al manteniment complet (CRUD).
 * @author Gess Montalbán
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Cerca tots els esdeveniments on participa un usuari concret.
     * Spring genera la consulta automàticament navegant per la taula intermèdia.
     * @param userId L'ID de l'usuari participant.
     * @return Llista d'esdeveniments on l'usuari està inscrit.
     */
    List<Event> findByParticipants_Id(Long userId);

    /**
     * Cerca esdeveniments organitzats per un usuari específic.
     * @param userId L'ID de l'usuari organitzador.
     * @return Llista d'esdeveniments creats per l'usuari.
     */
    List<Event> findByOrganizer_Id(Long userId);
    
    /**
     * Cerca esdeveniments que tindran lloc en una pista concreta.
     *  @param courtId L'ID de la pista.
     * @return Llista d'esdeveniments programats en aquella instal·lació.
     */
    List<Event> findByCourt_Id(Long courtId);
}