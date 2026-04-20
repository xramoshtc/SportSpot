package ioc.dammdev.SportSpotServer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ioc.dammdev.SportSpotServer.dto.BookingRequest;
import ioc.dammdev.SportSpotServer.model.Booking;
import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.service.BookingService;
import ioc.dammdev.SportSpotServer.service.CourtService;
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
import java.util.List;

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
@Test
    void getCourtBookingsByDate_ShouldReturnFilteredList_WhenDateProvided() throws Exception {
        // GIVEN
        Long courtId = 1L;
        String token = "test-token";
        LocalDateTime filterDate = LocalDateTime.of(2024, 5, 20, 10, 0);
        
        // Creem un usuari fent servir el constructor de client que has definit
        User client = new User("Gess", "password123", "gess@email.com");
        client.setId(100L); // Simulem que ja té un ID de la base de dades

        // Creem una pista (usant el constructor buit i setters)
        Court court = new Court();
        court.setName("Pista Central");
        court.setLocation("Zona Nord");

        // Simulem les reserves
        Booking bMatch = new Booking();
        bMatch.setDateTime(filterDate);
        bMatch.setUser(client); 
        bMatch.setCourt(court);

        Booking bNoMatch = new Booking();
        bNoMatch.setDateTime(filterDate.plusDays(1));
        bNoMatch.setUser(client);
        bNoMatch.setCourt(court);

        when(bookingService.getBookingsByCourt(token, courtId)).thenReturn(List.of(bMatch, bNoMatch));

        // WHEN & THEN
        mockMvc.perform(get("/api/courts/{id}/bookings", courtId)
                .header("Session-Token", token)
                .param("date", "2024-05-20T10:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
               
    }

 /**
 * Test que verifica que quan no s'envia el paràmetre 'date', 
 * l'endpoint retorna totes les reserves de la pista.
 * Corregit: S'instancien User i Court per evitar NullPointerException al mapping.
 */
@Test
void getCourtBookings_ShouldReturnAllBookings_WhenNoDateProvided() throws Exception {
    // 1. GIVEN: Preparem les dades mínimes que el mapping necessita
    User mockUser = new User("Joan", "pass123", "joan@test.com");
    
    Court mockCourt = new Court();
    mockCourt.setName("Pista Central");
    mockCourt.setLocation("Pavelló Blau");

    Booking b1 = new Booking();
    b1.setUser(mockUser);
    b1.setCourt(mockCourt);
    b1.setDateTime(LocalDateTime.now());

    Booking b2 = new Booking();
    b2.setUser(mockUser);
    b2.setCourt(mockCourt);
    b2.setDateTime(LocalDateTime.now().plusHours(1));

    // Configurem el Mock del servei perquè retorni aquestes reserves "omplertes"
    when(bookingService.getBookingsByCourt(anyString(), anyLong()))
            .thenReturn(List.of(b1, b2));

    // 2. WHEN & THEN
    mockMvc.perform(get("/api/courts/1/bookings")
            .header("Session-Token", "valid-token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            // Podem verificar que el mapping ha funcionat llegint el JSON de sortida
            .andExpect(jsonPath("$[0].courtName").value("Pista Central"));
}

    /**
     * Test de control d'errors que verifica que el sistema retorna un codi HTTP 204 (No Content) 
     * si s'accedeix a una pista que no té cap reserva programada.
     */
    @Test
    void getCourtBookings_ShouldReturn204_WhenListIsEmpty() throws Exception {
        // GIVEN
        when(bookingService.getBookingsByCourt(anyString(), anyLong())).thenReturn(List.of());

        // WHEN & THEN
        mockMvc.perform(get("/api/courts/1/bookings")
                .header("Session-Token", "token"))
                .andExpect(status().isNoContent());
    }
}