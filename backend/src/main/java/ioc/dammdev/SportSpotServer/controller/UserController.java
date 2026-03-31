/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.controller;

import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST per al manteniment dels usuaris (CRUD).
 * Gestiona les operacions d'alta, baixa, modificació i consulta.
 * @author Gess Montalbán
 */

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
     /**
     * Retorna la llista de tots els usuaris registrats.
     * @return Llista d'objectes {@link User}.
     * @param token token rebut a la capçalera "Session-Token"
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
        @RequestHeader(value = "Session-Token", required = false) String token){
       
        // Verifiquem usuari admin loguejat
        if (!userService.isValidToken(token) || !userService.isValidSession(token) || !userService.isAdmin(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
       /**
     * Crea un nou usuari a la base de dades
     * @param user Objecte {@link User} rebut en el cos de la petició.
     * @param token
     * @return ResponseEntity amb l'usuari creat (201), error de format (400) o conflicte de negoci (409).
     */
    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody User user,
            @RequestHeader(value = "Session-Token", required = false) String token){
        
        // 1 . Control d'accès (manual). validem si token pertany a un ADMIN.
        if (!userService.isValidToken(token) || !userService.isValidSession(token) || !userService.isAdmin(token)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // 2. Validació de format
        if (user.getName() == null || user.getName().trim().isEmpty() || user.getName().length() > 10 ||
                user.getPassword() == null || user.getPassword().trim().isEmpty() || user.getPassword() == null ||
                 user.getPassword().trim().isEmpty() || user.getPassword().length() > 20){
            
            return ResponseEntity.badRequest().build();
        }
        
        //2 . Intentem el registre a través del servei
        User savedUser = userService.registerUser(user);
        if (savedUser == null){
            //Retornem 409 Conflict si el nom ja existeix o el rol és invàlid
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        // 3. Retornem l¡usuari creat amb el seu ID de base de dades
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        
    }
    
}
