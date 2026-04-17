package ioc.dammdev.SportSpotServer.service;

import ioc.dammdev.SportSpotServer.dto.EventRequest;
import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.model.Event;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.CourtRepository;
import ioc.dammdev.SportSpotServer.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests d'unitat per al EventService.
 * Utilitza Mockito per simular les dependències i validar la lògica de negoci.
 * @author Gess Montalbán
 */
@ExtendWith(MockitoExtension.class)
class EventServiceTests {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserService userService;

    @Mock
    private CourtRepository courtRepository;

    @InjectMocks
    private EventService eventService;

    private User mockUser;
    private Court mockCourt;
    private final String TOKEN = "valid-token";

    @BeforeEach
    void setUp() {
        mockUser = new User(1L, "Gess", "1234", "gess@test.com", "USER", true);
        
        // Corregit: Court inclou ara el camp capacity (int) i price_per_hour (double)
        // Ordre segons el model actualitzat: id, name, type, price_per_hour, capacity, location
        // O: id, name, type, capacity, price_per_hour, location (ajustat al teu comentari de 'penúltim capacity')
        mockCourt = new Court(1L, "Pista 1", "Pàdel", 20.0, 2, "Nord");
    }

    /**
     * Test de creació d'esdeveniment amb èxit.
     */
    @Test
    void whenCreateEvent_thenReturnsEvent() {
        // GIVEN
        EventRequest request = new EventRequest("Torneig", 1L, "2026-05-20T10:00:00");
        when(userService.getUserByToken(TOKEN)).thenReturn(mockUser);
        when(courtRepository.findById(1L)).thenReturn(Optional.of(mockCourt));
        when(eventRepository.save(any(Event.class))).thenAnswer(i -> i.getArguments()[0]);

        // WHEN
        Event result = eventService.createEvent(request, TOKEN);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Torneig");
        assertThat(result.getParticipants()).contains(mockUser);
        verify(eventRepository).save(any(Event.class));
    }

    /**
     * Test d'inscripció amb èxit.
     */
    @Test
    void whenJoinEvent_thenReturnsTrue() {
        // GIVEN
        Event event = new Event("Partit", LocalDateTime.now(), mockUser, mockCourt);
        User newParticipant = new User(2L, "Joan", "123", "j@test.com", "USER", true);
        
        when(userService.getUserByToken(TOKEN)).thenReturn(newParticipant);
        when(eventRepository.findById(100L)).thenReturn(Optional.of(event));

        // WHEN
        boolean success = eventService.joinEvent(100L, TOKEN);

        // THEN
        assertThat(success).isTrue();
        assertThat(event.getParticipants()).hasSize(2);
        verify(eventRepository).save(event);
    }

    /**
     * Test crític: No permetre unir-se si la pista està plena.
     */
    @Test
    void whenJoinFullEvent_thenReturnsFalse() {
        // GIVEN
        Event event = new Event("Partit Ple", LocalDateTime.now(), mockUser, mockCourt);
        // Afegim participants fins omplir la capacitat (que és 2)
        User p2 = new User(2L, "User2", "123", "u2@test.com", "USER", true);
        event.addParticipant(p2); 
        
        User p3 = new User(3L, "User3", "123", "u3@test.com", "USER", true);
        when(userService.getUserByToken(TOKEN)).thenReturn(p3);
        when(eventRepository.findById(100L)).thenReturn(Optional.of(event));

        // WHEN
        boolean success = eventService.joinEvent(100L, TOKEN);

        // THEN
        assertThat(success).isFalse();
        assertThat(event.getParticipants()).hasSize(2); // No ha augmentat
        verify(eventRepository, never()).save(any());
    }

    /**
     * Test de seguretat: Només l'organitzador pot modificar.
     */
    @Test
    void whenNonOrganizerUpdates_thenReturnsNull() {
        // GIVEN
        Event event = new Event("Original", LocalDateTime.now(), mockUser, mockCourt);
        User intruder = new User(99L, "Intruder", "123", "i@test.com", "USER", true);
        EventRequest request = new EventRequest("Hackejat", 1L, "2026-05-20T10:00:00");

        when(userService.getUserByToken(TOKEN)).thenReturn(intruder);
        when(eventRepository.findById(100L)).thenReturn(Optional.of(event));
        when(userService.isAdmin(TOKEN)).thenReturn(false);

        // WHEN
        Event result = eventService.updateEvent(100L, request, TOKEN);

        // THEN
        assertThat(result).isNull();
        verify(eventRepository, never()).save(any());
    }

    /**
     * Test de "deixar esdeveniment" (no esborrar-lo).
     */
    @Test
    void whenParticipantLeaves_thenEventIsNotDeleted() {
        // GIVEN
        Event event = new Event("Partit", LocalDateTime.now(), mockUser, mockCourt);
        User p2 = new User(2L, "Joan", "123", "j@test.com", "USER", true);
        event.addParticipant(p2);

        when(userService.getUserByToken(TOKEN)).thenReturn(p2);
        when(eventRepository.findById(100L)).thenReturn(Optional.of(event));

        // WHEN
        boolean success = eventService.deleteOrLeaveEvent(100L, TOKEN);

        // THEN
        assertThat(success).isTrue();
        assertThat(event.getParticipants()).doesNotContain(p2);
        verify(eventRepository, never()).deleteById(anyLong()); // No s'ha d'esborrar l'event
        verify(eventRepository).save(event); // S'ha de guardar la llista de participants actualitzada
    }
}