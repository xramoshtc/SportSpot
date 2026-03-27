/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.testclient;


import ioc.dammdev.SportSpotServer.dto.LoginRequest;
import ioc.dammdev.SportSpotServer.dto.LoginResponse;
import ioc.dammdev.SportSpotServer.dto.LogoutRequest;
import org.springframework.web.client.RestTemplate;

/**
 * Un client senzill per provar la connexió amb el servidor amb un client Rest.
 * @author Gess Montalbán
 */
public class LoginClient {

    public static void main(String[] args) {
        // 1. Configuració de l'URL del teu servidor (remot o local)
        String urlIn = "http://10.2.3.145:8080/api/login";
        String urlOut = "http://10.2.3.145:8080/api/logout";
       urlIn = "http://localhost:8080/api/login";
       urlOut = "http://localhost:8080/api/logout";
        
        
        // 2. Creem l'eina que farà la petició HTTP
        RestTemplate restTemplate = new RestTemplate();

        // 3. Preparem les dades de prova (LoginRequest)
        LoginRequest peticio = new LoginRequest("admin","1234");
        

        System.out.println("Enviant petició de login al servidor...");

        try {
            // 4. Fem la petició POST i rebem la resposta automàticament com a LoginResponse
            LoginResponse resposta = restTemplate.postForObject(urlIn, peticio, LoginResponse.class);

            // 5. Mostrem el resultat per consola
            if (resposta != null) {
                System.out.println("--- RESPOSTA DEL SERVIDOR (LOGIN)---");
                System.out.println("Codi: " + resposta.getResultCode());
                System.out.println("Missatge: " + resposta.getMessage());
                
                if (resposta.getResultCode() == 200) {
                    System.out.println("ID Sessió: " + resposta.getSessionToken());
                    System.out.println("Rol usuari: " + resposta.getRole());
                                       
                }
              //   6 . Fem Logout a continuació
                System.out.println("TOKEN: " + resposta.getSessionToken());
                LogoutRequest peticioLogout = new LogoutRequest(resposta.getSessionToken());
                LoginResponse respostaLogout = restTemplate.postForObject(urlOut, peticioLogout, LoginResponse.class);
                System.out.println("--- RESPOSTA DEL SERVIDOR (LOGOUT)---");
                System.out.println("SUCCESS: "+ respostaLogout.isSuccess());
                System.out.println("Codi: " + respostaLogout.getResultCode());
                System.out.println("Missatge: " + respostaLogout.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("Error en connectar amb el servidor: " + e.getMessage());
            System.err.println("Assegura't que el servidor SportSpot està en marxa!");
        }
    }
}