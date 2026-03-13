package ioc.dammdev.SportSpotServer;

import ioc.dammdev.SportSpotServer.dto.LoginRequest;
import ioc.dammdev.SportSpotServer.dto.LoginResponse;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SportSpotServerApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;
	@Test
	void contextLoads() {
	}
        @Test
        public void testLoginAdminCorrecte(){
            LoginRequest request = new LoginRequest();
            request.setUser("admin");
            request.setPassword("1234");
            
            LoginResponse response = restTemplate.postForObject("/api/login", request, LoginResponse.class);
            
            assertThat(response.getResultCode() == 200);
            assertThat(response.getRole().equals("ADMIN"));
            
        }
        @Test
        public void testLoginAdminError(){
            LoginRequest request = new LoginRequest();
            request.setUser("admin");
            request.setPassword("33334");
            
            LoginResponse response = restTemplate.postForObject("/api/login", request, LoginResponse.class);
            
            assertThat(response.getResultCode() == -1);
            assertThat(response.getSessionToken().equals(""));
            
        }
             @Test
        public void testLoginClientCorrecte(){
            LoginRequest request = new LoginRequest();
            request.setUser("joanet");
            request.setPassword("5678");
            
            LoginResponse response = restTemplate.postForObject("/api/login", request, LoginResponse.class);
            
            assertThat(response.getResultCode() == 200);
            assertThat(response.getRole().equals("CLIENT"));
            
        }
           @Test
        public void testLoginClientError(){
            LoginRequest request = new LoginRequest();
            request.setUser("joanet");
            request.setPassword("33334");
            
            LoginResponse response = restTemplate.postForObject("/api/login", request, LoginResponse.class);
            
            assertThat(response.getResultCode() == -1);
            assertThat(response.getSessionToken().equals(""));
            
        }

}
