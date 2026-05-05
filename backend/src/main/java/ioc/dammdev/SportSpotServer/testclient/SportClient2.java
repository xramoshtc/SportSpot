package ioc.dammdev.SportSpotServer.testclient;

import ioc.dammdev.SportSpotServer.dto.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

/**
 * Client de proves avançat per a la TEA4 amb test de solapament.
 * @author Gess Montalbán
 */
public class SportClient2 {

 String baseUrl = "https://localhost:";
 // String baseUrl = "https://10.2.3.145:";
    private RestTemplate restTemplate;

    public SportClient2(String ip, String port) {
        this.baseUrl = "https://" + ip + ":" + port + "/api";
        this.restTemplate = new RestTemplate();
    }

    public String login(String name, String password) {
        LoginRequest loginRequest = new LoginRequest(name, password);
        try {
            ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                    baseUrl + "/login", loginRequest, LoginResponse.class);
            return response.getBody().getSessionToken();
        } catch (Exception e) { return null; }
    }

    /**
     * Mètode per crear reserves enviant el token al header.
     * @author Gess Montalbán
     */
    public ResponseEntity<BookingResponse> createBooking(Long courtId, String dateTime, int hours, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Token", token);
        
        BookingRequest req = new BookingRequest();
        req.setCourtId(courtId);
        req.setDateTime(dateTime);
        req.setDurationHours(hours);

        HttpEntity<BookingRequest> entity = new HttpEntity<>(req, headers);
        try {
            return restTemplate.postForEntity(baseUrl + "/bookings", entity, BookingResponse.class);
        } catch (Exception e) {
            System.err.println("[-] Error en reserva: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SportClient2 client = new SportClient2("10.2.3.145", "8443");
        System.out.println("=== TEST VALIDACIONS SPRINT 3 (TEA4) ===");

        String userToken = client.login("joanet", "5678");
        if (userToken == null) return;

        // --- PROVA DE SOLAPAMENT ---
        System.out.println("\n--- Provant validació de solapament de reserves ---");
        Long courtId = 1L;
        String horaComuna = "2026-06-20T10:00:00";

        // 1. Intent correcte
        System.out.println("[1] Creant reserva inicial (10:00h - 12:00h)...");
        ResponseEntity<BookingResponse> res1 = client.createBooking(courtId, horaComuna, 2, userToken);
        if (res1 != null && res1.getStatusCode() == HttpStatus.OK) {
            System.out.println("[+] Primera reserva OK.");
        }

        // 2. Intent de solapament (Mateixa pista, mateixa hora)
        System.out.println("[2] Intentant solapar reserva (11:00h - 12:00h)...");
        ResponseEntity<BookingResponse> res2 = client.createBooking(courtId, "2026-06-20T11:00:00", 1, userToken);
        
        if (res2 == null) {
            System.out.println("[OK] El servidor ha rebutjat el solapament (Error 400/500 esperat).");
        } else {
            System.err.println("[FALLADA] El servidor ha permès una reserva duplicada!");
        }

        System.out.println("\n=== FI DE LES PROVES ===");
    }
}