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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
