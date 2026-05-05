package ioc.dammdev.SportSpotServer.service;

import ioc.dammdev.SportSpotServer.model.Booking;
import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaris per a la gestió de reserves (BookingService).
 * Refactoritzat per fer servir el patró Builder en l'entitat Booking.
 * @author Gess Montalbán
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class BookingServiceTests {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CourtService courtService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingService bookingService;

    private User testUser;
    private Court testCourt;
    private Booking testBooking;
    private final String TOKEN = "token-valid-123";

    @BeforeEach
    void setUp() {
        // Constructors clàssics per User i Court
        testUser = new User(1L, "Gess", "1234", "gess@test.com", "USER", true);
        testCourt = new Court(10L, "Pista Central", "Pàdel", 20.0, 4, "Nord");
        
        // Setup de la reserva utilitzant el BUILDER (Evita errors de tipus/ordre)
        LocalDateTime tenAM = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
        
        testBooking = Booking.builder()
                .dateTime(tenAM)
                .durationHours(1)
                .build();
    }

    @Test
    void whenCreateValidBooking_thenSuccess() {
        when(userService.isValidSession(TOKEN)).thenReturn(true);
        when(userService.getUserByToken(TOKEN)).thenReturn(testUser);
        when(courtService.getCourtById(10L)).thenReturn(Optional.of(testCourt));
        when(bookingRepository.findOverlappingBookings(any(), any(), any())).thenReturn(new ArrayList<>());
        
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> {
            Booking b = i.getArgument(0);
            b.setId(100L);
            return b;
        });

        Booking result = bookingService.createBooking(10L, testBooking, TOKEN);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Gess", result.getUser().getName());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void whenBookingOverlaps_thenThrowsException() {
        when(userService.isValidSession(TOKEN)).thenReturn(true);
        when(userService.getUserByToken(TOKEN)).thenReturn(testUser);
        when(courtService.getCourtById(10L)).thenReturn(Optional.of(testCourt));
        
        // Simulem que ja existeix una reserva (Usem Builder per a la reserva existent)
        Booking existing = Booking.builder().id(99L).build();
        when(bookingRepository.findOverlappingBookings(any(), any(), any()))
                .thenReturn(List.of(existing));

        assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(10L, testBooking, TOKEN);
        });
    }

    @Test
    void whenUserDeletesOwnBooking_thenSuccess() {
        // Creem la reserva existent amb el Builder per seguretat en els tipus
        Booking existing = Booking.builder()
                .id(100L)
                .dateTime(LocalDateTime.now())
                .durationHours(1)
                .user(testUser)
                .court(testCourt)
                .build();

        when(userService.isValidSession(TOKEN)).thenReturn(true);
        when(userService.getUserByToken(TOKEN)).thenReturn(testUser);
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(existing));

        boolean deleted = bookingService.deleteBooking(100L, TOKEN);

        assertTrue(deleted);
        verify(bookingRepository).deleteById(100L);
    }

    @Test
    void whenUserDeletesOtherBooking_thenFails() {
        User otherUser = new User(2L, "Altre", "123", "a@a.com", "USER", true);
        
        // Reserva que pertany a un altre usuari
        Booking othersBooking = Booking.builder()
                .id(100L)
                .user(otherUser)
                .build();
        
        when(userService.isValidSession(TOKEN)).thenReturn(true);
        when(userService.getUserByToken(TOKEN)).thenReturn(testUser);
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(othersBooking));

        boolean deleted = bookingService.deleteBooking(100L, TOKEN);

        assertFalse(deleted);
        verify(bookingRepository, never()).deleteById(anyLong());
    }

    @Test
    void getMyBookings_ShouldReturnList_WhenValid() {
        when(userService.isValidSession(TOKEN)).thenReturn(true);
        when(userService.getUserByToken(TOKEN)).thenReturn(testUser);
        
        // Usem Builder per crear la llista de retorn
        Booking b = Booking.builder().id(1L).user(testUser).build();
        when(bookingRepository.findByUser(testUser)).thenReturn(List.of(b));

        List<Booking> result = bookingService.getMyBookings(TOKEN);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}