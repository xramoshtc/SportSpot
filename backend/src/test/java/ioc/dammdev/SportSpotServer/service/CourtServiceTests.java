package ioc.dammdev.SportSpotServer.service;

import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.repository.CourtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.test.context.ActiveProfiles;

/**
 * Tests unitaris per a la lògica de negoci de CourtService.
 * @author Gess Montalbán
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CourtServiceTests {

    @Mock
    private CourtRepository courtRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CourtService courtService;

    private Court testCourt;
    private final String ADMIN_TOKEN = "admin-token-123";
    private final String USER_TOKEN = "user-token-456";

    @BeforeEach
    void setUp() {
        testCourt = new Court(1L, "Pista 1", "Pàdel", 20.0, 12,"Pavelló Nord");
    }

    /**
     * Verifica que un ADMIN pot crear una pista si no hi ha duplicats.
     */
    @Test
    void whenAdminCreatesValidCourt_thenSuccess() {
        // GIVEN
        when(userService.isValidSession(ADMIN_TOKEN)).thenReturn(true);
        when(userService.isAdmin(ADMIN_TOKEN)).thenReturn(true);
        when(courtRepository.findByName(testCourt.getName())).thenReturn(Optional.empty());
        when(courtRepository.save(any(Court.class))).thenReturn(testCourt);

        // WHEN
        Court result = courtService.createCourt(testCourt, ADMIN_TOKEN);

        // THEN
        assertNotNull(result);
        assertEquals("Pista 1", result.getName());
        verify(courtRepository, times(1)).save(any(Court.class));
    }

    /**
     * Verifica que un usuari sense permisos no pot crear una pista.
     */
    @Test
    void whenNonAdminCreatesCourt_thenReturnsNull() {
        // GIVEN
        when(userService.isValidSession(USER_TOKEN)).thenReturn(true);
        when(userService.isAdmin(USER_TOKEN)).thenReturn(false);

        // WHEN
        Court result = courtService.createCourt(testCourt, USER_TOKEN);

        // THEN
        assertNull(result);
        // Verifiquem que el mètode save NO s'ha cridat mai
        verify(courtRepository, never()).save(any(Court.class));
    }

    /**
     * Verifica que no es pot crear una pista si el nom ja existeix.
     */
    @Test
    void whenCourtNameExists_thenReturnsNull() {
        // GIVEN
        when(userService.isValidSession(ADMIN_TOKEN)).thenReturn(true);
        when(userService.isAdmin(ADMIN_TOKEN)).thenReturn(true);
        when(courtRepository.findByName("Pista 1")).thenReturn(Optional.of(testCourt));

        // WHEN
        Court result = courtService.createCourt(testCourt, ADMIN_TOKEN);

        // THEN
        assertNull(result);
        verify(courtRepository, never()).save(any(Court.class));
    }
}