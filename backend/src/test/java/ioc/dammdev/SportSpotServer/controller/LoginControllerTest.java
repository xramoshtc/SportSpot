package ioc.dammdev.SportSpotServer.controller;

import ioc.dammdev.SportSpotServer.dto.LoginRequest;
import ioc.dammdev.SportSpotServer.dto.LoginResponse;
import ioc.dammdev.SportSpotServer.dto.LogoutRequest;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.UserRepository;
import ioc.dammdev.SportSpotServer.service.UserService;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

/**
 * Classe de proves d'integració per a l'aplicació SportSpotServer.
 * Utilitza un entorn de servidor sobre un port aleatori i base de dades H2.
 * * @author Gess Montalbán
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class LoginControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
   

    /**
     * Verifica que el context de l'aplicació s'arrenca correctament.
     * @author Gess Montalbán
     */
    @Test
    void contextLoads() {
    }
    
    @BeforeEach
    public void cleanup(){
        userService.clearSessions();
        System.out.println("Usuaris a la BD: " + userRepository.count()); // <--- AFEGEIX AIXÒ
    }

    /**
     * Valida l'accés d'un administrador amb credencials vàlides.
     * @author Gess Montalbán
     */
    @Test
    public void testLoginAdminCorrecte() {
        LoginRequest request = new LoginRequest("admin", "1234");
        LoginResponse response = restTemplate.postForObject("/api/login", request, LoginResponse.class);
        
        assertThat(response.getResultCode()).isEqualTo(200);
        assertThat(response.getRole()).isEqualTo("ADMIN");
        assertNotNull(response.getSessionToken());
    }

    /**
     * Valida la denegació d'accés a un administrador amb contrasenya errònia.
     * @author Gess Montalbán
     */
    @Test
    public void testLoginAdminError() {
        LoginRequest request = new LoginRequest("admin", "33334");
        LoginResponse response = restTemplate.postForObject("/api/login", request, LoginResponse.class);
        
        assertThat(response.getResultCode()).isEqualTo(-1);
    }

    /**
     * Valida l'accés d'un usuari tipus Client amb credencials vàlides.
     * @author Gess Montalbán
     */
    @Test
    public void testLoginClientCorrecte() {
        LoginRequest request = new LoginRequest("joanet", "5678");
        LoginResponse response = restTemplate.postForObject("/api/login", request, LoginResponse.class);
        
        assertThat(response.getResultCode()).isEqualTo(200);
        assertThat(response.getRole()).isEqualTo("CLIENT");
    }

    /**
     * Valida la denegació d'accés per a un usuari Client amb dades incorrectes.
     * @author Gess Montalbán
     */
    @Test
    public void testLoginClientError() {
        LoginRequest request = new LoginRequest("joanet", "33334");
        LoginResponse response = restTemplate.postForObject("/api/login", request, LoginResponse.class);
        
        assertThat(response.getResultCode()).isEqualTo(-1);
    }

    /**
     * Valida el flux d'obtenció de token i tancament de sessió amb èxit.
     * @author Gess Montalbán
     */
    @Test
    void testLogoutSuccess() {
        LoginRequest loginReq = new LoginRequest("admin", "1234");
        LoginResponse respLogin = restTemplate.postForObject("/api/login", loginReq, LoginResponse.class);
       
        LogoutRequest logoutReq = new LogoutRequest(respLogin.getSessionToken());
        LoginResponse respLogout = restTemplate.postForObject("/api/logout", logoutReq, LoginResponse.class);
        System.out.println(respLogout.isSuccess());
         System.out.println("LOGIN SUCCESS: " + respLogin.isSuccess());
        assertTrue(respLogout.isSuccess());
        System.out.println(respLogin.getSessionToken());
        assertEquals(200, respLogout.getResultCode());
    }

    /**
     * Valida que el sistema retorna error en intentar un logout sense token vàlid.
     * @author Gess Montalbán
     */
    @Test
    void testLogoutError() {
        LoginRequest loginReq = new LoginRequest("joanet", "56789");
        LoginResponse respLogin = restTemplate.postForObject("/api/login", loginReq, LoginResponse.class);
        
        LogoutRequest logoutReq = new LogoutRequest(respLogin.getSessionToken());
        LoginResponse respLogout = restTemplate.postForObject("/api/logout", logoutReq, LoginResponse.class);
        
        assertFalse(respLogout.isSuccess());
        assertEquals(-1, respLogout.getResultCode());
    }

    /**
     * Valida la persistència de dades a la base de dades H2 mitjançant el repositori.
     * @author Gess Montalbán
     */
    @Test
    void testUserRepositoryPersistence() {
        User testUser = new User();
        testUser.setName("test_user");
        testUser.setPassword("1234");
        testUser.setRole("CLIENT");

        userRepository.save(testUser);
        Optional<User> found = userRepository.findByName("test_user");

        assertTrue(found.isPresent());
        assertEquals("CLIENT", found.get().getRole());
    }
}