/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ioc.dammdev.SportSpotServer.controller;

import ioc.dammdev.SportSpotServer.dto.LoginRequest;
import ioc.dammdev.SportSpotServer.dto.LoginResponse;
import ioc.dammdev.SportSpotServer.dto.LogoutRequest;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.UserRepository;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *Controlador REST per gestionar l'autenticació d'usuaris.
 * Gestiona l'autenticació d'usuaris connectant amb la base de dades H2.
 * @author Gess Montalbán
 */

@RestController
@RequestMapping("/api")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository; // Connectem amb la BD
    
    // Guardarem en memòria l'usuari i token de sessió
    // Clau: Token (String) | Valor: Nom d'usuari (String)
    private static final java.util.Map<String, String> sessionsActives = new java.util.HashMap<>();
    
    /**
     * Gestiona la petició de login dels usuaris.
     * @param peticio Objecte {@link LoginRequest} amb les credencials.
     * @return Un objecte {@link LoginResponse} amb el resultat de l'operació.
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest peticio){
        
        // 1. Validació de longitud segons requeriments
        if ( peticio.getUser() == null || peticio.getUser().length() > 10 ||
               peticio.getPassword() == null || peticio.getPassword().length() > 20)
            return new LoginResponse(false, "Login Error: Format de camps no vàlid",-1,"","","");
        
        // 2. CERCA real a la base de dades i si ja està loguejat
        
        Optional<User> userOpt = userRepository.findByName(peticio.getUser());
        
        
        // 3. VALIDACIÓ DE CREDENCIALS
        if (userOpt.isPresent()) {
            User userBD = userOpt.get();
            //Comprovem contrasenya
            if (userBD.getPassword().equals(peticio.getPassword())){
                //Generem token aleatori
                String token = UUID.randomUUID().toString().substring(0,8);
                //Salvem dades de sessió (en memòria)
                sessionsActives.put(token, userBD.getName());
                //Retornem resposta
                return new LoginResponse(true,"Sessió iniciada correctament",200,token,userBD.getRole(),"Usuari: "+ userBD.getName());
            }
        }
        
        // 4. SI NO EXISTEIX USUARI o PW INCORRECTE
                    return new LoginResponse(false,"Contrasenya o usuari incorrecte",-1,"","","");
    }
    /**
 * Gestiona el tancament de sessió de l'usuari.
  * @param peticio: LogoutRequest amb el token de sessio
 * @return LoginResponse amb els camps buits o de tancament.
 * 
 */
@PostMapping("/logout")
public LoginResponse logout(@RequestBody LogoutRequest peticio) {
    
    // Rebem id de sessió amb la petició de logout
    // Retornem un objecte de resposta que indiqui l'èxit del tancament si 
    // success = false (perquè ja no hi ha sessió activa)
    // message = "Sessió tancada" o "token sessió invàlid"
    // code = 200 o -1
    // la resta de camps (token, role, name) es tornen buits ""
    
    if ( peticio.getToken() == null || !sessionsActives.containsKey(peticio.getToken())){
    
        return new LoginResponse(false,"Token no vàlid o sessió ja tancada", -1, "","","");
    
       
    } else {
            //Esborrem registre del login a memòria
        sessionsActives.remove(peticio.getToken());
        return new LoginResponse(true, "Sessió tancada correctament", 200, "", "", "");
               }
}
    
    
    
}
