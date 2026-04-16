package ioc.dammdev.SportSpotServer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ioc.dammdev.SportSpotServer.dto.BookingRequest;
import ioc.dammdev.SportSpotServer.model.Booking;
import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.service.BookingService;
import ioc.dammdev.SportSpotServer.service.CourtService;
import ioc.dammdev.SportSpotServer.service.UserService;
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
import org.springframework.test.context.ActiveProfiles;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'integració lleugers per al SportController.
 * Verifica que els endpoints responen correctament als verbs HTTP.
 * @author Gess Montalbán
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SportControllerTests {

    private MockMvc mockMvc;

    @Mock
    private CourtService courtService;

    @Mock
    private BookingService bookingService;
    
    
    @InjectMocks
    private SportController sportController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String TOKEN = "test-token";

    @BeforeEach
    void setUp() {
        // Configurem MockMvc per al controlador
        mockMvc = MockMvcBuilders.standaloneSetup(sportController).build();
    }

    /**
     * Verifica que el llistat de pistes retorna un 200 OK i un JSON.
     */
    @Test
    void whenGetAllCourts_thenReturns200() throws Exception {
        when(courtService.getAllCourts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/courts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Verifica que es pot crear una reserva (POST) enviant un JSON i un Token.
     */
    @Test
    void whenCreateBooking_thenReturns201() throws Exception {
        // GIVEN
        BookingRequest request = new BookingRequest();
        request.setCourtId(1L);
        request.setDateTime("2026-05-20T10:00:00");
        request.setDurationMinutes(60);

        // Simulem una reserva guardada amb dades completes per al Mapper
        User mockUser = new User(1L, "Gess", "pass", "gess@test.com", "USER", true);
        Court mockCourt = new Court(1L, "Pista 1", "Pàdel", 20.0, 4,"Nord");
        Booking savedBooking = new Booking(100L, LocalDateTime.parse(request.getDateTime()), 60, mockUser, mockCourt);

        when(bookingService.createBooking(anyLong(), any(Booking.class), anyString()))
                .thenReturn(savedBooking);

        // WHEN & THEN
        mockMvc.perform(post("/api/bookings")
                .header("Session-Token", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.userName").value("Gess"))
                .andExpect(jsonPath("$.courtName").value("Pista 1"));
    }

    /**
     * Verifica que si el servei falla, el controlador retorna un 400 Bad Request.
     */
    @Test
    void whenCreateBookingFails_thenReturns400() throws Exception {
        BookingRequest request = new BookingRequest();
        request.setDateTime("2026-05-20T10:00:00");

        when(bookingService.createBooking(any(), any(Booking.class), anyString()))
                .thenReturn(null);

        mockMvc.perform(post("/api/bookings")
                .header("Session-Token", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Verifica l'esborrat de reserves (DELETE).
     */
    @Test
    void whenDeleteBooking_thenReturns204() throws Exception {
        when(bookingService.deleteBooking(100L, TOKEN)).thenReturn(true);

        mockMvc.perform(delete("/api/bookings/100")
                .header("Session-Token", TOKEN))
                .andExpect(status().isNoContent());
    }
 
    @Test
void updateBooking_ReturnsUpdatedBooking() throws Exception {
    String token = "valid-token";
    BookingRequest request = new BookingRequest(1L, "2026-06-15T10:00:00", 90);
    
    // 1. Creem l'usuari i la pista (dades mínimes per al mapper)
    User mockUser = new User();
    mockUser.setName("Joan");
    
    Court mockCourt = new Court();
    mockCourt.setName("Pista Central");

    // 2. Configurem el Booking que retornarà el mock
    Booking updatedBooking = new Booking();
    updatedBooking.setId(1L);
    updatedBooking.setDateTime(LocalDateTime.now());
    updatedBooking.setDurationMinutes(90);
    updatedBooking.setUser(mockUser);   // <--- IMPORTANT: evitem el NPE
    updatedBooking.setCourt(mockCourt); // <--- Segurament el mapper també ho demana

    // 3. El stubbing
    when(bookingService.updateBooking(any(), any(), any()))
            .thenReturn(updatedBooking);

    // 4. Execució
    mockMvc.perform(put("/api/bookings/1")
            .header("Session-Token", token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
}
}