/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.testclient;


import ioc.dammdev.SportSpotServer.dto.LoginRequest;
import ioc.dammdev.SportSpotServer.dto.LoginResponse;
import org.springframework.web.client.RestTemplate;

/**
 * Un client senzill per provar la connexió amb el servidor.
 */
public class LoginClient {

    public static void main(String[] args) {
        // 1. Configuració de l'URL del teu servidor local
        String url = "http://10.2.3.145:8080/api/login";
        
        // 2. Creem l'eina que farà la petició HTTP
        RestTemplate restTemplate = new RestTemplate();

        // 3. Preparem les dades de prova (LoginRequest)
        LoginRequest peticio = new LoginRequest();
        peticio.setUser("admino");
        peticio.setPassword("1234");

        System.out.println("Enviant petició de login al servidor...");

        try {
            // 4. Fem la petició POST i rebem la resposta automàticament com a LoginResponse
            LoginResponse resposta = restTemplate.postForObject(url, peticio, LoginResponse.class);

            // 5. Mostrem el resultat per consola
            if (resposta != null) {
                System.out.println("--- RESPOSTA DEL SERVIDOR ---");
                System.out.println("Codi: " + resposta.getResultCode());
                System.out.println("Missatge: " + resposta.getMessage());
                
                if (resposta.getResultCode() == 200) {
                    System.out.println("ID Sessió: " + resposta.getSessionToken());
                    System.out.println("Rol usuari: " + resposta.getRole());
                }
            }
        } catch (Exception e) {
            System.err.println("Error en connectar amb el servidor: " + e.getMessage());
            System.err.println("Assegura't que el servidor SportSpot està en marxa!");
        }
    }
}