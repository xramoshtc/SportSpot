package ioc.dammdev.SportSpotServer;

import ioc.dammdev.SportSpotServer.dto.LoginRequest;
import ioc.dammdev.SportSpotServer.dto.LoginResponse;
import ioc.dammdev.SportSpotServer.dto.LogoutRequest;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.UserRepository;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // <-- AIXÒ ÉS LA CLAU
class SportSpotServerApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    private UserRepository UserRepository;
	@Test
	void contextLoads() {
	}
        @Test
        public void testLoginAdminCorrecte(){
            LoginRequest request = new LoginRequest("admin","1234");
                      
            LoginResponse response = restTemplate.postForObject("/api/login", request, LoginResponse.class);
            
            assertThat(response.getResultCode() == 200);
            assertThat(response.getRole().equals("ADMIN"));
            
        }
        @Test
        public void testLoginAdminError(){
            LoginRequest request = new LoginRequest("admin","33334");
            
            LoginResponse response = restTemplate.postForObject("/api/login", request, LoginResponse.class);
            
            assertThat(response.getResultCode() == -1);
            assertThat(response.getSessionToken().equals(""));
            
        }
             @Test
        public void testLoginClientCorrecte(){
            LoginRequest request = new LoginRequest("joanet","5678");
                       
            LoginResponse response = restTemplate.postForObject("/api/login", request, LoginResponse.class);
            
            assertThat(response.getResultCode() == 200);
            assertThat(response.getRole().equals("CLIENT"));
            
        }
           @Test
        public void testLoginClientError(){
            LoginRequest request = new LoginRequest("joanet","33334");
            
            LoginResponse response = restTemplate.postForObject("/api/login", request, LoginResponse.class);
            
            assertThat(response.getResultCode() == -1);
            assertThat(response.getSessionToken().equals(""));
            
        }
        
        @Test
        void testLogoutSuccess() {
              
            LoginRequest request = new LoginRequest("joanet","5678");
                       
            LoginResponse respostaLogin = restTemplate.postForObject("/api/login", request, LoginResponse.class);
             assertNotNull(respostaLogin.getSessionToken(), "El token no hauria de ser nul");
             LogoutRequest peticioLogout = new LogoutRequest(respostaLogin.getSessionToken());
                LoginResponse respostaLogout = restTemplate.postForObject("/api/logout", peticioLogout, LoginResponse.class);
                
           
            assertTrue(respostaLogout.isSuccess(), "El logout hauria de tenir èxit amb un token vàlid");
            assertEquals(respostaLogout.getResultCode(), 200);
        }
        @Test
        void testLogoutError() {
              
            LoginRequest request = new LoginRequest("joanet","56789");
                       
            LoginResponse respostaLogin = restTemplate.postForObject("/api/login", request, LoginResponse.class);
             assertNotNull(respostaLogin.getSessionToken(), "El token no hauria de ser nul");
             LogoutRequest peticioLogout = new LogoutRequest(respostaLogin.getSessionToken());
                LoginResponse respostaLogout = restTemplate.postForObject("/api/logout", peticioLogout, LoginResponse.class);
                
           
            assertFalse(respostaLogout.isSuccess(), "El logout hauria de ser erroni sense un token vàlid");
            assertEquals(respostaLogout.getResultCode(), -1);
        }

        
        // --- PROVA D'INTEGRACIÓ (Base de Dades H2) ---
        @Test
        void testUserRepositoryPersistence() {
            // Creem un usuari per la prova
            User testUser = new User();
            testUser.setName("test_user"); // Revisa si al teu model és 'name' o 'username'
            testUser.setPassword("1234");
            testUser.setRole("CLIENT");

            // El guardem i el busquem
            UserRepository.save(testUser);
            Optional<User> found = UserRepository.findByName("test_user");

            // Verifiquem que la BD H2 ha fet la seva feina
            assertTrue(found.isPresent(), "L'usuari s'hauria d'haver guardat i trobat");
            assertEquals("CLIENT", found.get().getRole());
        }
}
      



