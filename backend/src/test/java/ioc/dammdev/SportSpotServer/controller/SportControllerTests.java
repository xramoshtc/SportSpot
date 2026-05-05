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
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'integració MockMvc per al SportController.
 * Resolució definitiva d'errors d'inferència de tipus i NPE.
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
    private final String TOKEN = "test-token-segur";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sportController).build();
    }

    @Test
    void whenCreateBooking_thenReturns201() throws Exception {
        BookingRequest request = new BookingRequest();
        request.setCourtId(1L);
        request.setDateTime("2026-05-20T10:00:00");
        request.setDurationHours(1);
        
        User mockUser = new User(1L, "Gess", "pass", "gess@test.com", "USER", true);
        Court mockCourt = new Court(1L, "Pista 1", "Pàdel", 20.0, 4,"Nord");

        Booking savedBooking = Booking.builder()
                .id(100L)
                .dateTime(LocalDateTime.parse(request.getDateTime()))
                .durationHours(1)
                .user(mockUser) // Simplificat per al JSON de tornada si cal
                .court(mockCourt)
                .build();

      
        when(bookingService.createBooking(anyLong(), any(Booking.class), anyString()))
                .thenReturn(savedBooking);

        mockMvc.perform(post("/api/bookings")
                .header("Session-Token", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    void updateBooking_ReturnsUpdatedBooking() throws Exception {
        BookingRequest request = new BookingRequest();
        request.setCourtId(1L);
        request.setDateTime("2026-06-15T12:00:00");
        request.setDurationHours(2);

        Booking updatedBooking = buildMockBooking(1L, 2);

        // FIX: Especificant tipus exactes en els matchers
        when(bookingService.updateBooking(anyLong(), any(BookingRequest.class), anyString()))
                .thenReturn(updatedBooking);

        mockMvc.perform(put("/api/bookings/1")
                .header("Session-Token", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.durationHours").value(2));
    }

    @Test
    void getCourtBookingsByDate_ShouldReturnFilteredList_WhenDateProvided() throws Exception {
        Long courtId = 1L;
        
        Booking bMatch = Booking.builder()
            .id(50L)
            .dateTime(LocalDateTime.parse("2024-05-20T10:00:00")) // 👈 CLAVE
            .durationHours(1)
            .user(new User(1L, "Test", "pass", "t@test.com", "USER", true))
            .court(new Court(1L, "Pista 1", "Pàdel", 20.0, 4, "Barcelona"))
            .endTime(LocalDateTime.parse("2024-05-20T11:00:00"))
            .build();

        
        when(bookingService.getBookingsByCourt(anyString(), eq(courtId)))
                .thenReturn(List.of(bMatch));

        mockMvc.perform(get("/api/courts/{id}/bookings", courtId)
                .header("Session-Token", TOKEN)
                .param("date", "2024-05-20T10:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void whenDeleteBooking_thenReturns204() throws Exception {
        // Simple matcher per a tipus primitius
        when(bookingService.deleteBooking(anyLong(), anyString())).thenReturn(true);

        mockMvc.perform(delete("/api/bookings/100")
                .header("Session-Token", TOKEN))
                .andExpect(status().isNoContent());
    }
    
    
    // Mètode "helper" per construir un Booking complet
    private Booking buildMockBooking(Long id, int duration) {
    User user = new User(1L, "Test User", "pass", "test@test.com", "USER", true);

    Court court = new Court(1L, "Pista 1", "Pàdel", 20.0, 4, "Barcelona");

    return Booking.builder()
            .id(id)
            .dateTime(LocalDateTime.parse("2026-06-15T12:00:00"))
            .durationHours(duration)
            .endTime(LocalDateTime.parse("2026-06-15T14:00:00"))
            .user(user)
            .court(court)
            .build();
}
}