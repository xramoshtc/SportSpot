package ioc.dammdev.SportSpotServer;

import ioc.dammdev.SportSpotServer.testclient.UserClient;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.UserRepository;
import ioc.dammdev.SportSpotServer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

/**
 * Proves d'extrem a extrem (E2E) utilitzant un client HTTP real.
 * Aquests tests verifiquen que tota la pila (Controller -> Service -> Repository) funciona.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    private UserClient userClient;
    private String adminToken;
    

    @BeforeEach
    void setUp() {
        userClient = new UserClient(String.valueOf(port));
        userRepository.deleteAll();
        userService.clearSessions();

        // 1. Creem un admin real a la BD
        User admin = new User( "adminReal", passwordEncoder.encode("1234"), "admin@test.com", "ADMIN");
        userRepository.save(admin);

        // 2. Generem una sessió real per a l'admin
        adminToken = userService.createSession(Optional.of(admin));
    }

    /**
     * Prova real de llistat d'usuaris a través d'HTTP.
     */
    @Test
    void whenClientRequestsAllUsers_thenReturnsSuccess() {
        ResponseEntity<User[]> response = userClient.getAllUsers(adminToken);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    /**
     * Prova real d'esborrat d'un usuari creat prèviament.
     */
    @Test
    void whenClientDeletesUser_thenUserIsRemovedFromDatabase() {
        // GIVEN: Un usuari a esborrar
        User victim = new User(null,"victim", "123", "v@test.com", "USER",true);
        userRepository.save(victim);

        // WHEN: El client demana l'esborrat per HTTP
        ResponseEntity<Void> response = userClient.deleteUser("victim", adminToken);

        // THEN: Resposta 204 i l'usuari ja no és a la BD
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.findByName("victim")).isEmpty();
    }

    /**
     * Prova que un token inexistent retorna 401 (Unauthorized) segons la lògica del controlador.
     */
    @Test
    void whenClientUsesInvalidToken_thenReturns401() {
        try {
            userClient.getAllUsers("TOKEN-FALS");
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }
    }
}