/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ioc.dammdev.SportSpotServer.controller;

import ioc.dammdev.SportSpotServer.dto.LoginRequest;
import ioc.dammdev.SportSpotServer.dto.LoginResponse;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.UserRepository;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *Controlador REST per gestionar l'autenticació d'usuaris.
 * En aquesta fase de l'Sprint 1, el controlador realitza una 
 * simulació (Mock) de l'autenticació sense connectar amb la Base de Dades.
 * @author Gess
 */

@RestController
@RequestMapping("/api")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository; // Connectem amb la BD
    
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
        
        // 2. CERCA real a la base de dades
        
        Optional<User> userOpt = userRepository.findByName(peticio.getUser());
        
        // 3. VALIDACIÓ DE CREDENCIALS
        if (userOpt.isPresent()) {
            User userBD = userOpt.get();
            //Comprovem contrasenya
            if (userBD.getPassword().equals(peticio.getPassword())){
                //Generem token aleatori
                String token = UUID.randomUUID().toString().substring(0,8);
                //Retornem resposta
                return new LoginResponse(true,"Sessió iniciada correctament",200,token,userBD.getRole(),"Usuari: "+ userBD.getName());
            }
        }
        
        // 4. SI NO EXISTEIX USUARI o PW INCORRECTE
                    return new LoginResponse(false,"Contrasenya o usuari incorrecte",-1,"","","");
    }
    
    
    
}
