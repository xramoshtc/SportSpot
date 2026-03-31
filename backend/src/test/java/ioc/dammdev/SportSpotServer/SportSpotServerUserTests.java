package ioc.dammdev.SportSpotServer;

import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.UserRepository;
import ioc.dammdev.SportSpotServer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

/**
 * Proves d'integració per al UserController.
 * Es focalitza en els mètodes de llistat i creació d'usuaris.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SportSpotServerUserTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;

    private String adminToken;

@BeforeEach
public void setup() {
    userService.clearSessions();
    userRepository.deleteAll(); 

    User admin = new User(null, "admin", "1234", "ADMIN", "admin@test.com");
    //userService.registerUser(admin);
    userRepository.saveAndFlush(admin);
 

    Optional<User> dbAdmin = userService.getUserByName("admin");
    assertTrue(dbAdmin.isPresent(), "L'usuari no s'ha desat en la BD");
    adminToken = userService.createSession(dbAdmin, "1234");

    assertNotNull(adminToken, "Token null o no vàlid");
}

    /**
     * Test per al mètode GET /api/users (Llistar usuaris).
     * Verifica que un usuari loguejat pot obtenir la llista.
     */
    @Test
    public void testGetAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Token", adminToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                "/api/users",
                HttpMethod.GET,
                entity,
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

//    /**
//     * Test per al mètode POST /api/users (Crear usuari).
//     * Verifica que un ADMIN pot crear un usuari correctament.
//     */
//    @Test
//    public void testCreateUserSuccess() {
//        User newUser = new User(null, "NouUsuari", "Pass123", "USER", "nou@test.com");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Session-Token", adminToken);
//        HttpEntity<User> entity = new HttpEntity<>(newUser, headers);
//
//        ResponseEntity<User> response = restTemplate.postForEntity("/api/users", entity, User.class);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("NouUsuari", response.getBody().getName());
//        assertNotNull(response.getBody().getId());
//    }
//
//    /**
//     * Test de seguretat per al mètode POST /api/users.
//     * Verifica que si no s'envia token, la resposta és 403 (Prohibit) o 401 (No autoritzat).
//     */
//    @Test
//    public void testCreateUserUnauthorized() {
//        User newUser = new User(null, "NoAuth", "Pass123", "USER", "noauth@test.com");
//
//        // Petició sense la capçalera Session-Token
//        ResponseEntity<User> response = restTemplate.postForEntity("/api/users", newUser, User.class);
//
//        // Segons la lògica del Canvas, si el token és null o no és admin, retorna FORBIDDEN
//        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
//    }
}