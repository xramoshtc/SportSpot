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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.test.context.ActiveProfiles;

/**
 * Tests unitaris per a la gestió de reserves.
 * Verifiquem que el flux entre token, usuari i pista és correcte.
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
    private final String TOKEN = "token-valit-123";

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "Gess", "1234", "gess@test.com", "USER", true);
        testCourt = new Court(10L, "Pista Central", "Pàdel", 20.0, 4, "Nord");
        testBooking = new Booking(null, LocalDateTime.now(), 60, null, null);
    }

    /**
     * Test principal: Crear una reserva amb dades vàlides.
     */
    @Test
    void whenCreateValidBooking_thenSuccess() {
        // GIVEN: Configurem els mocks per retornar el que esperem
        when(userService.isValidSession(TOKEN)).thenReturn(true);
        when(userService.getUserByToken(TOKEN)).thenReturn(testUser);
        when(courtService.getCourtById(10L)).thenReturn(Optional.of(testCourt));
        
        // Simulem el guardat retornant la mateixa reserva amb un ID inventat
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> {
            Booking b = i.getArgument(0);
            b.setId(100L);
            return b;
        });

        // WHEN: Executem l'acció
        Booking result = bookingService.createBooking(10L, testBooking, TOKEN);

        // THEN: Verifiquem resultats
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Gess", result.getUser().getName());
        assertEquals("Pista Central", result.getCourt().getName());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    /**
     * Test de seguretat: No es pot reservar sense sessió.
     */
    @Test
    void whenCreateBookingWithoutSession_thenReturnsNull() {
        when(userService.isValidSession("token-fals")).thenReturn(false);

        Booking result = bookingService.createBooking(10L, testBooking, "token-fals");

        assertNull(result);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    /**
     * Test de cancel·lació: Només el propietari pot esborrar.
     */
    @Test
    void whenUserDeletesOwnBooking_thenSuccess() {
        // GIVEN
        Booking existingBooking = new Booking(100L, LocalDateTime.now(), 60, testUser, testCourt);
        when(userService.isValidSession(TOKEN)).thenReturn(true);
        when(userService.getUserByToken(TOKEN)).thenReturn(testUser);
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(existingBooking));

        // WHEN
        boolean deleted = bookingService.deleteBooking(100L, TOKEN);

        // THEN
        assertTrue(deleted);
        verify(bookingRepository).deleteById(100L);
    }
    
    /**
     * Test que verifica que es retorna una llista de reserves quan la sessió 
     * és vàlida i la pista existeix. Comprova la integració interna amb el repositori.
     */
    @Test
    void getBookingsByCourt_ShouldReturnList_WhenSessionAndCourtAreValid() {
        // GIVEN
        String token = "valid-token";
        Long courtId = 1L;
        when(userService.isValidSession(token)).thenReturn(true);
        when(courtService.getCourtById(courtId)).thenReturn(Optional.of(new Court()));
        when(bookingRepository.findByCourtId(courtId)).thenReturn(List.of(new Booking(), new Booking()));

        // WHEN
        List<Booking> result = bookingService.getBookingsByCourt(token, courtId);

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookingRepository, times(1)).findByCourtId(courtId);
    }

    /**
     * Test que verifica que el sistema bloqueja l'accés a les reserves 
     * si el token de sessió proporcionat no és vàlid o ha caducat.
     */
    @Test
    void getBookingsByCourt_ShouldReturnNull_WhenSessionIsInvalid() {
        // GIVEN
        String token = "invalid-token";
        when(userService.isValidSession(token)).thenReturn(false);

        // WHEN
        List<Booking> result = bookingService.getBookingsByCourt(token, 1L);

        // THEN
        assertNull(result);
        verify(bookingRepository, never()).findByCourtId(anyLong());
    }

    /**
     * Test que verifica que es retorna null quan s'intenta consultar 
     * les reserves d'una pista que no existeix a la base de dades.
     */
    @Test
    void getBookingsByCourt_ShouldReturnNull_WhenCourtNotFound() {
        // GIVEN
        String token = "valid-token";
        when(userService.isValidSession(token)).thenReturn(true);
        when(courtService.getCourtById(anyLong())).thenReturn(Optional.empty());

        // WHEN
        List<Booking> result = bookingService.getBookingsByCourt(token, 99L);

        // THEN
        assertNull(result);
    }
    
    
}