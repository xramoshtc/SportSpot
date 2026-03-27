/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ioc.dammdev.SportSpotServer.controller;

import ioc.dammdev.SportSpotServer.dto.LoginRequest;
import ioc.dammdev.SportSpotServer.dto.LoginResponse;
import ioc.dammdev.SportSpotServer.dto.LogoutRequest;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.service.UserService;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *Controlador REST per gestionar l'autenticació d'usuaris.
 * Gestiona l'autenticació d'usuaris connectant amb la base de dades H2.
 * @author Gess Montalbán
 */

@RestController
@RequestMapping("/api")
public class LoginController {
    
    @Autowired
    private UserService userService; // Connectem amb la capa de servei
    
  
    /**
     * Gestiona la petició de login dels usuaris.
     * @param peticio Objecte {@link LoginRequest} amb les credencials.
     * @return Un objecte {@link LoginResponse} amb el resultat de l'operació.
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest peticio){
        
        // 1. Validació de longitud segons requeriments
        if ( peticio.getUser() == null || peticio.getUser().trim().isEmpty() || peticio.getUser().length() > 10 ||
               peticio.getPassword() == null || peticio.getPassword().length() > 20)
            return new LoginResponse(false, "Login Error: Format de camps no vàlid",-1,"","");
        
        // 2. Validem credencials
        Optional<User> userLogin = userService.login(peticio.getUser());
        String token = userService.createSession(userLogin, peticio.getPassword());
        if (token != null)           
            return  new LoginResponse(true,"Login realitzat correctament",200,token,userLogin.get().getRole());
        else
        // 3. Si no existeix o pw incorrecte o ja ha iniciat sessió
            return new LoginResponse(false,"Nom usuari/contrasenya no vàlids o sessió ja iniciada",-1,"","");
        
        
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
    
    boolean logoutResponse =  userService.logout(peticio.getToken());
     
     if (!logoutResponse)
         return new LoginResponse(false,"Sessió invàlida, no es pot tancar la sessió",-1,"","");
     else
         return new LoginResponse(true,"Sessió tancada correctament",200,"","");
     
            }
}
    
    
    

