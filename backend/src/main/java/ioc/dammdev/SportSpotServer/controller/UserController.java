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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
     * Registra un nou usuari a la base de dades sense privilegis admin
     * @param user Objecte {@link User} rebut en el cos de la petició.
     * @return ResponseEntity amb l'usuari creat (201), error de format (400) o conflicte de negoci (409).
     */
    @PostMapping("/newuser")
    public ResponseEntity<User> registerUser(
            @RequestBody User user) {
        
      
        // 1. Validació de format
        if (user.getName() == null || user.getName().trim().isEmpty() || user.getName().length() > 10 ||
                user.getPassword() == null || user.getPassword().trim().isEmpty() || user.getPassword() == null ||
                 user.getPassword().trim().isEmpty() || user.getPassword().length() > 20){
            
            return ResponseEntity.badRequest().build();
        }
        
        //2 . Intentem el registre a través del servei
        user.setRole("CLIENT");
        User savedUser = userService.registerUser(user);
        if (savedUser == null){
            //Retornem 409 Conflict si el nom ja existeix 
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        // 3. Retornem l¡usuari creat amb el seu ID de base de dades
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        
    }
    /**
 * Retorna les dades de l'usuari autenticat actualment.
 * Utilitza el token de la capçalera per identificar l'usuari al HashMap de memòria.
 * @param token Token de sessió rebut a la capçalera.
 * @return ResponseEntity amb l'objecte {@link User} o 401 si la sessió no és vàlida.
 * @author Gess Montalbán
 */
@GetMapping("/me")
public ResponseEntity<User> getMyProfile(
        @RequestHeader(value = "Session-Token", required = false) String token) {
    
    // 1. Verifiquem si el token existeix i la sessió és activa al HashMap
    if (token == null || !userService.isValidSession(token)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    // 2. Recuperem l'usuari directament des de la lògica de sessió del servei
    User currentUser = userService.getUserByToken(token);
    
    if (currentUser != null) {
        // Per seguretat, podríem posar la password a null abans d'enviar-lo
        currentUser.setPassword("********"); 
        return ResponseEntity.ok(currentUser);
    }
    
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
}
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
    
    /**
     * Petició per eliminar un usuari de la base de dades a partir del seu nom.
     * Aquesta operació només és permesa per a usuaris amb rol ADMIN.
     * @param name Nom de l'usuari que es vol eliminar (rebut a la URL).
     * @param token Token d'autenticació rebut a la capçalera "Session-Token".
     * @return ResponseEntity buit amb codi 204 (No Content) si s'ha eliminat, 
     * 404 (Not Found) si l'usuari no existeix, 
     * o 409 (Conflict) si el token no és d'administrador o no és el client a esborrar.
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String name,
            @RequestHeader(value = "Session-Token", required = false) String token) {

        // 1. Control d'accés: Verifiquem si el token és vàlid i pertany a un ADMIN o pertany al mateix client
        if (!userService.isValidToken(token) || !userService.isValidSession(token) || !userService.isAdmin(token)){
            if (!userService.isTokenOfName(token, name))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 2. Intentem l'esborrat a través de la lògica del servei (findByName + deleteById)
        boolean isDeleted = userService.deleteUserByName(name);

        if (isDeleted) {
            // L'estàndard REST dicta que si l'esborrat és exitós i no retornem dades, usem 204
            return ResponseEntity.noContent().build();
        } else {
            // Si el servei no ha trobat l'usuari, retornem 404
            return ResponseEntity.notFound().build();
        }
    }

    
    /**
     * Petició per modificar un usuari mitjançant una petició PUT.
     * Rep el nom de l'usuari per la URL i l'objecte amb els canvis en el cos (JSON).
     * Requereix un token de sessió vàlid amb privilegis d'administrador.
     * @param name Nom de l'usuari passat com a PathVariable.
     * @param userUpdates Dades de l'usuari rebudes al RequestBody.
     * @param token Token de sessió rebut a la capçalera "Session-Token".
     * @return {@link ResponseEntity} amb:
     * 200 (OK): Si l'actualització ha estat satisfactòria.
     * 403 (FORBIDDEN): Si el token no és d'administrador.
     * 404 (NOT FOUND): Si l'usuari a modificar no existeix.
     */
    @PutMapping("/{name}")
    public ResponseEntity<User> update(
            @PathVariable String name,
            @RequestBody User userUpdates,
            @RequestHeader("Session-Token") String token) {

        
        if (!userService.isAdmin(token)){
            if (!userService.isTokenOfName(token, name)) 
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 si no és admin o no és el client
        }
        try {
            User updated = userService.updateUser(name, userUpdates);
            return ResponseEntity.ok(updated);
        } catch (Exception e){
            return ResponseEntity.notFound().build(); // 404 si nom no existeix
        }
    }
    
}
