/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.service;

import ioc.dammdev.SportSpotServer.dto.EventRequest;
import ioc.dammdev.SportSpotServer.model.*;
import ioc.dammdev.SportSpotServer.repository.CourtRepository;
import ioc.dammdev.SportSpotServer.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servei de lògica de negoci per a la gestió d'esdeveniments.
 * Controla la disponibilitat de places, la identitat de l'organitzador
 * i les regles de manteniment complet (CRUD).
 * @author Gess Montalbán
 */
@Service
public class EventService {

    @Autowired 
    private EventRepository eventRepository;
    @Autowired 
    private UserService userService;
    @Autowired 
    private CourtRepository courtRepository;

    /**
     * Retorna tots els esdeveniments del sistema.
     * @return EventList: llista amb tots els esdeveniments
     * 
     */
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    /**
     * Crea un nou esdeveniment.
     * Valida que la pista existeixi i afegeix l'organitzador com a participant.
     * @param request : DTO amb la petició de l'esdeveniment
     * @param token : id de sessió
     */
    public Event createEvent(EventRequest request, String token) {
        User user = userService.getUserByToken(token);
        Optional<Court> courtOpt = courtRepository.findById(request.getCourtId());

        if (user == null || courtOpt.isEmpty()) return null;

        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDateTime(LocalDateTime.parse(request.getDateTime()));
        event.setCourt(courtOpt.get());
        event.setOrganizer(user);
        
        // Per defecte, l'organitzador és el primer participant
        event.addParticipant(user);

        return eventRepository.save(event);
    }

    /**
     * Inscriu l'usuari actual a un esdeveniment.
     * Validació crítica: No permet superar la capacitat màxima de la pista.
     * @param eventId : id de l'esdeveniment
     * @param token : id de la sessió
     * @return Si ha tingut èxit o no la inscripció
     */
    public boolean joinEvent(Long eventId, String token) {
        User user = userService.getUserByToken(token);
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (user == null || eventOpt.isEmpty()) return false;
        Event event = eventOpt.get();

        // 1. Validació de capacitat
        if (!event.addParticipant(user))
            return false;
        eventRepository.save(event);
        return true;
    }

    /**
     * Actualitza un esdeveniment existent (Manteniment).
     * Només l'organitzador o l'admin poden fer canvis.
     * @param id : id de l'esdevenimient
     * @param request : petició de modificació
     * @param token : id de sessió
     * @return L'event modficat
     */
    public Event updateEvent(Long id, EventRequest request, String token) {
        User currentUser = userService.getUserByToken(token);
        Optional<Event> eventOpt = eventRepository.findById(id);

        if (currentUser == null || eventOpt.isEmpty()) return null;
        Event event = eventOpt.get();

        // Seguretat: Només organitzador o Admin
        if (!event.getOrganizer().getId().equals(currentUser.getId()) && !userService.isAdmin(token)) {
            return null;
        }

        // Si es canvia de pista, comprovem que la nova pista tingui prou capacitat
        if (!event.getCourt().getId().equals(request.getCourtId())) {
            Optional<Court> newCourt = courtRepository.findById(request.getCourtId());
            if (newCourt.isPresent() && newCourt.get().getCapacity() >= event.getParticipants().size()) {
                event.setCourt(newCourt.get());
            } else {
                return null; // No hi caben els participants actuals a la nova pista
            }
        }

        event.setTitle(request.getTitle());
        event.setDateTime(LocalDateTime.parse(request.getDateTime()));

        return eventRepository.save(event);
    }

    /**
     * Elimina un esdeveniment. 
     * Si l'usuari és l'organitzador, s'elimina totalment.
     * Si és només un participant, se'l treu de la llista (abandonar).
     * @param id : id de l'esdeveniment
     * @param token : id de sessió
     * @return èxit de l'eliminació
     */
    public boolean deleteOrLeaveEvent(Long id, String token) {
        User user = userService.getUserByToken(token);
        Optional<Event> eventOpt = eventRepository.findById(id);

        if (user == null || eventOpt.isEmpty()) return false;
        Event event = eventOpt.get();

        if (event.getOrganizer().getId().equals(user.getId()) || userService.isAdmin(token)) {
            eventRepository.deleteById(id);
            return true;
        } else {
            // No és organitzador: només abandona l'esdeveniment
            boolean removed = event.getParticipants().removeIf(u -> u.getId().equals(user.getId()));
            if (removed) eventRepository.save(event);
            return removed;
        }
    }
}