package ioc.dammdev.SportSpotServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import ioc.dammdev.SportSpotServer.controller.EventController;
import ioc.dammdev.SportSpotServer.dto.EventRequest;
import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.model.Event;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'integració per al EventController.
 * Verifica el correcte funcionament dels endpoints d'esdeveniments.
 * @author Gess Montalbán
 */
@ExtendWith(MockitoExtension.class)
class EventE2ETest {

    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String TOKEN = "test-session-token";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    /**
     * Verifica que es poden llistar tots els esdeveniments.
     */
    @Test
    void whenGetAllEvents_thenReturns200() throws Exception {
        when(eventService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Verifica la creació d'un esdeveniment via POST.
     */
    @Test
    void whenCreateEvent_thenReturns201() throws Exception {
        EventRequest request = new EventRequest("Torneig Pàdel", 1L, "2026-06-10T18:00:00");
        User organizer = new User(1L, "Gess", "pass", "gess@test.com", "USER", true);
        
        // Ajustat al constructor: id, name, type, pricePerHour, capacity, location
        Court court = new Court(1L, "Central", "Pàdel", 20.0, 4, "Zona Nord");
        
        Event createdEvent = new Event("Torneig Pàdel", LocalDateTime.now(), organizer, court);
        createdEvent.setId(100L);

        when(eventService.createEvent(any(EventRequest.class), eq(TOKEN))).thenReturn(createdEvent);

        mockMvc.perform(post("/api/events")
                .header("Session-Token", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.title").value("Torneig Pàdel"));
    }

    /**
     * Verifica que si l'usuari no té permís per editar, retorna 403 Forbidden.
     */
    @Test
    void whenUpdateEventUnauthorized_thenReturns403() throws Exception {
        EventRequest request = new EventRequest("Títol Nou", 1L, "2026-06-10T18:00:00");

        when(eventService.updateEvent(anyLong(), any(EventRequest.class), eq(TOKEN))).thenReturn(null);

        mockMvc.perform(put("/api/events/100")
                .header("Session-Token", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    /**
     * Verifica que un usuari pot unir-se a un esdeveniment.
     */
    @Test
    void whenJoinEvent_thenReturns200() throws Exception {
        when(eventService.joinEvent(eq(100L), eq(TOKEN))).thenReturn(true);

        mockMvc.perform(post("/api/events/100/join")
                .header("Session-Token", TOKEN))
                .andExpect(status().isOk());
    }

    /**
     * Verifica que si s'intenta unir a un esdeveniment ple, retorna 400 Bad Request.
     */
    @Test
    void whenJoinFullEvent_thenReturns400() throws Exception {
        when(eventService.joinEvent(eq(100L), eq(TOKEN))).thenReturn(false);

        mockMvc.perform(post("/api/events/100/join")
                .header("Session-Token", TOKEN))
                .andExpect(status().isBadRequest());
    }

    /**
     * Verifica l'eliminació o l'acció d'abandonar un esdeveniment.
     */
    @Test
    void whenDeleteOrLeaveEvent_thenReturns204() throws Exception {
        when(eventService.deleteOrLeaveEvent(eq(100L), eq(TOKEN))).thenReturn(true);

        mockMvc.perform(delete("/api/events/100")
                .header("Session-Token", TOKEN))
                .andExpect(status().isNoContent());
    }

    /**
     * Verifica que si l'esdeveniment no existeix al intentar esborrar, retorna 404 Not Found.
     */
    @Test
    void whenDeleteNonExistentEvent_thenReturns404() throws Exception {
        when(eventService.deleteOrLeaveEvent(eq(999L), eq(TOKEN))).thenReturn(false);

        mockMvc.perform(delete("/api/events/999")
                .header("Session-Token", TOKEN))
                .andExpect(status().isNotFound());
    }
}