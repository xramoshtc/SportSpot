package ioc.dammdev.SportSpotServer.testclient;

import ioc.dammdev.SportSpotServer.dto.LoginRequest;
import ioc.dammdev.SportSpotServer.dto.LoginResponse;
import ioc.dammdev.SportSpotServer.dto.LogoutRequest;
import ioc.dammdev.SportSpotServer.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class UserClient1 {

    public static void main(String[] args) {
        String urlLogin = "http://localhost:8080/api/login";
        String urlLogout = "http://localhost:8080/api/logout";
        String urlUsers = "http://localhost:8080/api/users";
      urlLogin = "http://10.2.3.145:8080/api/login";
        urlLogout = "http://10.2.3.145:8080/api/logout";
        urlUsers = "http://10.2.3.145:8080/api/users";

        RestTemplate restTemplate = new RestTemplate();

        // --- 1. Login ---
        LoginRequest loginReq = new LoginRequest("admin", "1234");

        try {
            LoginResponse loginResp = restTemplate.postForObject(urlLogin, loginReq, LoginResponse.class);

            if (loginResp != null && loginResp.getResultCode() == 200) {
                String token = loginResp.getSessionToken();
                System.out.println("Login correcte!");
                System.out.println("Session Token: " + token);
                System.out.println("Rol: " + loginResp.getRole());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Session-Token", token);

                // --- 2. Crear un nou usuari ---
                User nouUsuari = new User("usuaritest", "pass1234","usuaritest@test.com","USER");
                HttpEntity<User> postEntity = new HttpEntity<>(nouUsuari, headers);

                ResponseEntity<User> postResponse = restTemplate.exchange(
                        urlUsers,
                        HttpMethod.POST,
                        postEntity,
                        User.class
                );

                System.out.println("\n--- CREACIÓ D'USUARI ---");
                if (postResponse.getStatusCode() == HttpStatus.CREATED) {
                    System.out.println("Usuari creat correctament: " + postResponse.getBody());
                } else {
                    System.err.println("Error creant usuari: " + postResponse.getStatusCode());
                }

                // --- 3. Llistar usuaris amb GET ---
                HttpEntity<Void> getEntity = new HttpEntity<>(headers);
                ResponseEntity<List> getResponse = restTemplate.exchange(
                        urlUsers,
                        HttpMethod.GET,
                        getEntity,
                        List.class
                );

                System.out.println("\n--- LLISTA D'USUARIS ---");
                System.out.println("Status: " + getResponse.getStatusCode());
                List<?> users = getResponse.getBody();
                if (users != null) {
                    users.forEach(System.out::println);
                } else {
                    System.out.println("No s'han trobat usuaris.");
                }
                
                // --- 3. Esborrar usuari ---
                
                HttpEntity<Void> delUser = new HttpEntity<>(headers);
                ResponseEntity<Void> deletionResponse = restTemplate.exchange(
                        urlUsers + "/usuaritest",
                        HttpMethod.DELETE,
                        delUser,
                        Void.class
                        );
                
                System.out.println("RESULTAT ESBORRAR: " + deletionResponse.getBody());
                
                // --- 4. MODIFIQUEM USUARI ----
                
                User dadesModificades = new User("nouusuari","novaPass1223", "nou-email",null);
                HttpEntity<User> putRequest = new HttpEntity<>(dadesModificades, headers);
                ResponseEntity<User> modResp = restTemplate.exchange(
                    urlUsers + "/marta",
                    HttpMethod.PUT,
                    putRequest,
                    User.class
                );
                if (modResp.getStatusCode() == HttpStatus.OK)
                    System.out.println("Usuari modificat: " + modResp.getBody().getName());
                // --- 4. Logout ---
                LogoutRequest logoutReq = new LogoutRequest(token);
                LoginResponse logoutResp = restTemplate.postForObject(urlLogout, logoutReq, LoginResponse.class);
                System.out.println("\n--- LOGOUT ---");
                System.out.println("Success: " + logoutResp.isSuccess());
                System.out.println("Codi: " + logoutResp.getResultCode());
                System.out.println("Missatge: " + logoutResp.getMessage());

            } else {
                System.err.println("Login fallit: " + (loginResp != null ? loginResp.getMessage() : "Sense resposta"));
            }

        } catch (Exception e) {
            System.err.println("Error en connectar amb el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}