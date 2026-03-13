/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ioc.dammdev.SportSpotServer.controller;

import ioc.dammdev.SportSpotServer.dto.LoginRequest;
import ioc.dammdev.SportSpotServer.dto.LoginResponse;
import ioc.dammdev.SportSpotServer.model.User;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 *Controlador REST per gestionar l'autenticació d'usuaris.
 * En aquesta fase de l'Sprint 1, el controlador realitza una 
 * simulació (Mock) de l'autenticació sense connectar amb la Base de Dades.
 * @author Gess
 */

@RestController
@RequestMapping("/api")
public class AuthController {
    
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
        
        // 2. Simulació de validació
        if ("admin".equals(peticio.getUser()) && "1234".equals(peticio.getPassword())){
            User userAdmin = new User(1L,"admin", "1234", "admin@sportspot.com","ADMIN");
            String token = UUID.randomUUID().toString().substring(0, 8);
            
            return new LoginResponse(true,"Sessió iniciada",200, token,userAdmin.getRole(),"test admin");
        } else if ("joanet".equals(peticio.getUser()) && "5678".equals(peticio.getPassword())){
            
            User userClient = new User(42L, "joanet","5678","joanet@sportspot.com","CLIENT");
            String token = UUID.randomUUID().toString().substring(0,8);
            return new LoginResponse(true,"Sessió iniciada", 200,token, userClient.getRole(),"test client");
        } else
            // Cas de contrasenya incorrecta
            return new LoginResponse(false,"Contrasenya o usuari incorrecte",-1,"","","");
    }
    
    
    
}
