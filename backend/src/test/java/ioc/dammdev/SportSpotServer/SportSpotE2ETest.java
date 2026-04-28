package ioc.dammdev.SportSpotServer;

import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.BookingRepository;
import ioc.dammdev.SportSpotServer.repository.CourtRepository;
import ioc.dammdev.SportSpotServer.repository.UserRepository;
import ioc.dammdev.SportSpotServer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Proves d'extrem a extrem (E2E) per a tot el sistema SportSpot.
 * Fusiona la gestió d'usuaris amb el flux de pistes i reserves.
 * @author Gess Montalbán
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SportSpotE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    private String adminToken;
    private String userToken;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api";
        
        // Netegem tota la base de dades
        bookingRepository.deleteAll();
        courtRepository.deleteAll();
        userRepository.deleteAll();
        userService.clearSessions();

        // 1. Creem un admin real
        User admin = new User(null, "AdminGess", passwordEncoder.encode("1234"), "admin@test.com", "ADMIN", true);
        userRepository.save(admin);
        adminToken = userService.createSession(Optional.of(admin));

        // 2. Creem un usuari real
        User user = new User(null, "UserGess", passwordEncoder.encode("1234"), "user@test.com", "USER", true);
        userRepository.save(user);
        userToken = userService.createSession(Optional.of(user));
    }

    // --- TESTS D'USUARI ADMIN  ---

    @Test
    void whenAdminRequestsAllUsers_thenReturnsSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Token", adminToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "/users", HttpMethod.GET, entity, User[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    void whenAdminDeletesUser_thenUserIsRemovedFromDatabase() {
        // GIVEN: Un usuari a esborrar
        User victim = new User(null, "victim", "123", "v@test.com", "USER", true);
        userRepository.save(victim);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Token", adminToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // WHEN: Es demana l'esborrat per HTTP
        ResponseEntity<Void> response = restTemplate.exchange(baseUrl + "/users/victim", HttpMethod.DELETE, entity, Void.class);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.findByName("victim")).isEmpty();
    }

     // --- TESTS D'USUARI CLIENT  ---
    @Test
    void whenClientUsesInvalidToken_thenReturns401() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Token", "TOKEN-FALS");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(baseUrl + "/users", HttpMethod.GET, entity, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    // --- TESTS DE FLUX ESPORTIU (PISTES I RESERVES) ---

    @Test
    void fullApplicationFlowTest() {
        // 1. ADMIN CREA PISTA
        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.set("Session-Token", adminToken);
        Court newCourt = new Court(null, "Pista Central", "Pàdel", 25.0, 12, "Valencia");
        HttpEntity<Court> courtRequest = new HttpEntity<>(newCourt, adminHeaders);
        
        ResponseEntity<Map> courtResponse = restTemplate.postForEntity(baseUrl + "/courts", courtRequest, Map.class);
        assertEquals(HttpStatus.CREATED, courtResponse.getStatusCode());
        Integer courtId = (Integer) courtResponse.getBody().get("id");

        // 2. USUARI FA RESERVA
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.set("Session-Token", userToken);
        Map<String, Object> bookingBody = Map.of(
            "courtId", courtId,
            "dateTime", "2026-12-01T10:00:00",
            "durationMinutes", 60
        );
        HttpEntity<Map<String, Object>> bookingReq = new HttpEntity<>(bookingBody, userHeaders);
        
        ResponseEntity<Map> bookingRes = restTemplate.postForEntity(baseUrl + "/bookings", bookingReq, Map.class);
        
        assertEquals(HttpStatus.CREATED, bookingRes.getStatusCode());
        assertEquals("UserGess", bookingRes.getBody().get("userName"));
    }
}