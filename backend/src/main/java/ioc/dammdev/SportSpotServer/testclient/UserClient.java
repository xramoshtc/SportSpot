package ioc.dammdev.SportSpotServer.testclient;

import ioc.dammdev.SportSpotServer.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Arrays;

/**
 * Client HTTP per interactuar amb l'API de UserController.
 * S'utilitza per a proves d'integració d'extrem a extrem (E2E).
 * @author Gess Montalbán
 */
public class UserClient {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public UserClient(String port) {
        this.baseUrl = "http://localhost:" + port + "/api/users";
        this.restTemplate = new RestTemplate();
    }

    /**
     * Realitza una petició GET per obtenir tots els usuaris.
     * @param token Token de sessió per a l'autenticació.
     * @return ResponseEntity amb la llista d'usuaris.
     */
    public ResponseEntity<User[]> getAllUsers(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Token", token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        return restTemplate.exchange(baseUrl, HttpMethod.GET, request, User[].class);
    }

    /**
     * Realitza una petició DELETE per esborrar un usuari pel seu nom.
     * @param name Nom de l'usuari a esborrar.
     * @param token Token de sessió (requereix privilegis d'ADMIN).
     * @return ResponseEntity amb el resultat de l'operació.
     */
    public ResponseEntity<Void> deleteUser(String name, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Token", token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        return restTemplate.exchange(baseUrl + "/" + name, HttpMethod.DELETE, request, Void.class);
    }
}