package ioc.dammdev.SportSpotServer.testclient;

import ioc.dammdev.SportSpotServer.dto.BookingRequest;
import ioc.dammdev.SportSpotServer.dto.BookingResponse;
import ioc.dammdev.SportSpotServer.dto.CourtDTO;
import ioc.dammdev.SportSpotServer.dto.LoginRequest;
import ioc.dammdev.SportSpotServer.dto.LoginResponse;
import ioc.dammdev.SportSpotServer.dto.LogoutRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Client senzill per provar els endpoints del SportController en viu.
 * Inclou el flux complet: Login -> Operacions -> Logout.
 * @author Gess Montalbán
 */

public class SportClient {
     
   String baseUrl = "http://localhost:";
 // String baseUrl = "http://10.2.3.145:";


    private  RestTemplate restTemplate = new RestTemplate();

    public SportClient(String port) {
        this.baseUrl = baseUrl + port + "/api";
        this.restTemplate = new RestTemplate();
    }

    /**
     * Realitza el login per obtenir un token de sessió.
     */
    public String login(String name, String password) {
        LoginRequest loginRequest = new LoginRequest(name, password);
        
        try {
            LoginResponse response = restTemplate.postForObject(
                    baseUrl + "/login",
                    loginRequest,
                    LoginResponse.class
            );
            
            
            if (response.getResultCode()== 200 && response != null) {
                return (String) response.getSessionToken();
            }
        } catch (Exception e) {
            System.err.println("Error en el login: " + e.getMessage());
        }
        return null;
    }

    /**
     * Tanca la sessió de l'usuari al servidor.
     */
    public boolean logout(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Token", token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        LogoutRequest logoutReq = new LogoutRequest(token);

        try {
            LoginResponse response = restTemplate.postForObject(
                    baseUrl + "/logout",
                    logoutReq,
                    LoginResponse.class
            );
            return response.getResultCode() == 200;
        } catch (Exception e) {
            System.err.println("Error en el logout: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obté la llista de totes les pistes disponibles.
     */
    public List<CourtDTO> getCourts() {
        ResponseEntity<List<CourtDTO>> response = restTemplate.exchange(
                baseUrl + "/courts",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CourtDTO>>() {}
        );
        return response.getBody();
    }

    /**
     * Realitza una reserva en una pista específica.
     */
    public BookingResponse createBooking(Long courtId, String dateTime, int duration, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        BookingRequest request = new BookingRequest();
        request.setCourtId(courtId);
        request.setDateTime(dateTime);
        request.setDurationMinutes(duration);

        HttpEntity<BookingRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<BookingResponse> response = restTemplate.postForEntity(
                    baseUrl + "/bookings",
                    entity,
                    BookingResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error creant reserva: " + e.getMessage());
            return null;
        }
    }

    /**
     * Consulta les reserves de l'usuari actual mitjançant el seu token.
     */
    public List<BookingResponse> getMyBookings(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Token", token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<BookingResponse>> response = restTemplate.exchange(
                    baseUrl + "/bookings/my",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<BookingResponse>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error obtenint reserves: " + e.getMessage());
            return null;
        }
    }

    /**
     * Exemple d'execució del flux complet.
     */
    public static void main(String[] args) {
        SportClient client = new SportClient("8080");
        
        System.out.println("=== INICIANT FLUX DE PROVA E2E ===");

        // 1. LOGIN
        System.out.println("\n1. Intentant login amb 'joanet'...");
        String myToken = client.login("joanet", "5678");
        
        if (myToken == null) {
            System.err.println("No s'ha pogut iniciar sessió. Aturant prova.");
            return;
        }
        System.out.println("Login correcte! Token: " + myToken);

        // 2. CONSULTAR PISTES
        System.out.println("\n2. Llistat de pistes disponibles:");
        List<CourtDTO> courts = client.getCourts();
        if (courts != null) {
            courts.forEach(c -> System.out.println(" - [" + c.getId() + "] " + c.getName() + " (" + c.getType() +"  , Ciutat:  " + c.getLocation()+")"));
        }

        // 3. CREAR RESERVA
        if (courts != null && !courts.isEmpty()) {
            Long firstCourtId = courts.get(0).getId();
            System.out.println("\n3. Reservant la pista ID " + firstCourtId + "...");
            BookingResponse res = client.createBooking(firstCourtId, "2026-06-15T18:00:00", 60, myToken);
            if (res != null) {
                System.out.println("Reserva confirmada! ID Reserva: " + res.getId());
            }
        }

        // 4. CONSULTAR LES MEVES RESERVES
        System.out.println("\n4. Comprovant les meves reserves actuals...");
        List<BookingResponse> myBookings = client.getMyBookings(myToken);
        if (myBookings != null) {
            System.out.println("Tens " + myBookings.size() + " reserva/es activa/es.");
        }

        // 5. LOGOUT
        System.out.println("\n5. Tancant sessió...");
        boolean logoutOk = client.logout(myToken);
        if (logoutOk) {
            System.out.println("Logout finalitzat correctament. Sessió destruïda.");
        } else {
            System.err.println("Error en el logout.");
        }
        
        System.out.println("\n=== FLUX FINALITZAT AMB ÈXIT ===");
    }
}