/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.controller;

import ioc.dammdev.SportSpotServer.dto.EventRequest;
import ioc.dammdev.SportSpotServer.dto.EventResponse;
import ioc.dammdev.SportSpotServer.model.Event;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.service.EventService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
   private EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<Event> events = eventService.findAll();
        List<EventResponse> response = events.stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @RequestHeader("Session-Token") String token,
            @RequestBody EventRequest request) {
        
        Event newEvent = eventService.createEvent(request, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(newEvent));
    }
 /**
     * Permet a un usuari apuntar-se a un esdeveniment existent.
     * @param id ID de l'esdeveniment al qual unir-se.
     * @param token Token de sessió de l'usuari.
     * @return 200 OK si s'ha pogut unir, 400 en cas contrari (pista plena o error).
     */
    @PostMapping("/{id}/join")
    public ResponseEntity<Void> joinEvent(
            @PathVariable Long id,
            @RequestHeader("Session-Token") String token) {
        
        boolean joined = eventService.joinEvent(id, token);
        if (!joined) {
            // El false pot venir per: usuari no trobat, event no trobat o PISTA PLENA
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
    
      /**
     * Actualitza les dades d'un esdeveniment.
     * Només permès per a l'organitzador o administradors.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @RequestBody EventRequest request,
            @RequestHeader("Session-Token") String token) {
        
        Event updated = eventService.updateEvent(id, request, token);
        if (updated == null) {
            // El null pot significar que no s'ha trobat o que NO TÉ PERMISOS (Forbidden)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(updated);
    }
    
     /**
     * Elimina un esdeveniment o permet a un usuari abandonar-lo.
     * @param id ID de l'esdeveniment.
     * @param token Token de l'usuari.
     * @return 204 No Content si l'operació ha tingut èxit.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrLeaveEvent(
            @PathVariable Long id,
            @RequestHeader("Session-Token") String token) {
        
        boolean success = eventService.deleteOrLeaveEvent(id, token);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }
    /**
     * El nostre Mapper que utilitza el Builder de Lombok
     */
    private EventResponse mapToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .courtName(event.getCourt().getName())
                .organizerName(event.getOrganizer().getName())
                .dateTime(event.getDateTime().toString())
                .currentParticipants(event.getParticipants().size())
                .maxCapacity(event.getCourt().getCapacity())
                .participantNames(event.getParticipants().stream()
                        .map(User::getName)
                        .toList())
                .build();
    }
}
