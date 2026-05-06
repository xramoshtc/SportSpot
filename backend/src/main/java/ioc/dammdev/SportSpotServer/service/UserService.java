package ioc.dammdev.SportSpotServer.service;

import ioc.dammdev.SportSpotServer.dto.SessionDTO;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Capa de servei per gestionar la lògica de negoci dels usuaris.
 * Separa el controlador de la base de dades per complir amb l'arquitectura en capes.
 * @author Gess Montalbán
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
      // Guardarem en memòria l'usuari i token de sessió
    // Clau: Token (String) | Valor: Nom d'usuari (String)
    private static final ConcurrentHashMap<String, SessionDTO> activeSessions = new ConcurrentHashMap<>();
    private final int SESSION_TIMEOUT_MINUTES = 30;
    
    /**
     * Valida les credencials comparant el text pla amb el hash de la BD.
     * @param plainPassword contrasenya en text pla
     * @param hashedPassword hash contrasenya en BD
     * @return boolean resultat de comparació dels hashs de contrasenyes
     * @author Gess Montalbán
     */
    public boolean authenticate(String plainPassword, String hashedPassword) {
       
   
    return passwordEncoder.matches(plainPassword, hashedPassword);
   
}
    /**
     * Gestiona el procés de login. 
     * @param username: nom d'usuari
     * @param password contrasenya introduïda per l'usuari
     * @return LoginResponse: respossta que s'envia al client
     * @author Gess Montalbán
     */
    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByName(username);
       
        if (!user.isPresent())
          return Optional.empty();
        
        User userBD = user.get();
        if (authenticate(password, userBD.getPassword()))              
            return user;
        
        return Optional.empty();
            
    }
      /**
     * Gestiona el procés de logout.
     * @param token: id de la sessió
     * @return boolean: respossta que s'envia al client
     * @author Gess Montalban
     */
    public boolean logout(String token){
        if (activeSessions.containsKey(token)){
            closeSession(token);
           return true;
        }
        else
            return false;
    }
    
    /**
     * Crea una nova sessió d'usuari si les credencials són correctes.
     * Genera un token únic i l'associa al nom d'usuari en memòria.
     *
     * @param user Optional amb l'usuari trobat a la base de dades
     * @return token de sessió si l'autenticació és correcta, null si falla
     * @author Gess Montalbán
     */
    
    public String createSession(Optional<User> user){
        
        // Comprovem si l'usuari existeix i si la contrasenya coincideix
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString().substring(0, 8);
            LocalDateTime expiry = LocalDateTime.now().plusMinutes(SESSION_TIMEOUT_MINUTES);
            activeSessions.put(token, new SessionDTO(user.get().getName(),expiry));
            return token;
        }
         else
            
            return null;        
    }
    /**
     * Comprova si un token rebut és vàlid, no és nul/buit i està actiu al mapa.
     * @param token El token rebut des del frontend.
     * @return true si la sessió és vàlida, false en cas contrari.
     */
    public boolean isValidToken(String token){
        if (token == null || token.trim().isEmpty())
            return false;
        else 
            return true;
                    
    }
        /**
     * Comprova si un token rebut es correspon a l'usués vàlid, no és nul/buit i està actiu al mapa.
     * @param token El token rebut des del frontend.
     * @return true si la sessió és vàlida, false en cas contrari.
     */
    public boolean isTokenOfName(String token, String name){
        
            if (name.equals(activeSessions.get(token).getUsername()))
                    return true;
            return false;
    }    
    
    /**
     * Comprova si una sessió està activa al sistema.
     *
     * @param token el token de sessió
     * @return true si la sessió existeix i és activa, false en cas contrari
     */
    public boolean isValidSession(String token){
        if (!isValidToken(token)) return false;
        SessionDTO session = activeSessions.get(token);
        if (session == null) return false;
        if (session.isExpired()){
            closeSession(token);
            return false;
        }
        return true;
                              
        
    }
    
    
    /**
     * Tanca una sessió eliminant el token del sistema.
     *
     * @param token el token de la sessió a eliminar
     */
    public void closeSession(String token){
        activeSessions.remove(token);
    }
    
    /**
     * Elimina totes les sessions actives del sistema.
     */
    public void clearSessions(){
        activeSessions.clear();
    }
    
    /**
     * Comprova si el token pertany a un usuari amb rol d'administrador.
     *
     * @param token el token de sessió
     * @return true si l'usuari és ADMIN, false en cas contrari o si el token no és vàlid
     */
    public boolean isAdmin(String token){
        if (token == null) return false;
        
        //Busquem usuari associat a la sessió
        SessionDTO user = activeSessions.get(token);
        //Si el token no existeix o ha caducat
        if (user == null) return false;
        // Busquem usuari a la base de dades
        Optional<User> loggedUser = userRepository.findByName(user.getUsername());
        if ( loggedUser.isPresent()){
            if (loggedUser.get().getRole().equals("ADMIN"))
                return true;
        }        
        return false;
        
    }
    /**
     * Registra un usuari nou. 
     * Podem afegir validacions de negoci (ex: si el nom ja està agafat).
     * @param newUser: usuari a resgistrar
     * @return User: nou usuari creat
     * @author Gess Montalbán
     */
    public User registerUser(User newUser) {
        // Lògica de negoci: comprovem si el nom ja està agafat
        Optional<User> existingUser = userRepository.findByName(newUser.getName());
        
        if (existingUser.isPresent()) 
            return null;
        String role = newUser.getRole();
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
         if (role == null || role.trim().isEmpty()){
             newUser.setRole("USER"); //Usuari per defecte
         } else {
             role = role.toUpperCase();
             if (!role.equals("ADMIN") && !role.equals("USER") && !role.equals("CLIENT"))
                 return null;
             newUser.setRole(role);
             newUser.setPassword(encodedPassword);
             
         }         
        return userRepository.save(newUser);
    }
   
      /**
     * Retorna la llista de tots els usuaris registrats a la base de dades.
     *
     * @return llista d'usuaris
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

  
    /**
     * Busca un usuari pel seu nom.
     *
     * @param name nom de l'usuari
     * @return Optional amb l'usuari si existeix, buit si no es troba
     */
    public Optional<User> getUserByName(String name){
        return userRepository.findByName(name);
        
    }
    
    /**
     * Busca un usuari pel seu identificador únic.
     *
     * @param id identificador de l'usuari
     * @return Optional amb l'usuari si existeix, buit si no es troba
     */
    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }
    
    /**
     * Elimina un usuari de la base de dades pel seu ID.
     *
     * @param id identificador de l'usuari
     * @return true si s'ha eliminat correctament, false si no existeix
     */
    public boolean deleteUser(Long id){
        if (userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Elimina un usuari de la base de dades a partir del seu nom.
     *
     * @param name nom de l'usuari
     * @return true si l'usuari s'ha eliminat correctament, false si no existeix
     */
    @Transactional
    public boolean deleteUserByName(String name){
        Optional<User> userBD = userRepository.findByName(name);
        if (userBD.isPresent()){
            Long userId = userBD.get().getId();
            userRepository.deleteById(userId);
            return true;
        }
            
        return false;
    }
    
    /**
     * Actualitza les dades d'un usuari existent a la base de dades.
     * <p>
     * El mètode cerca l'usuari pel seu identificador únic (nom). Si el troba, 
     * actualitza els camps permesos (email, role, password) i persisteix els canvis.
     * </p>
     * * @param name El nom de l'usuari que es vol modificar (identificador).
     * @param userUpdates Objecte que conté les noves dades a aplicar.
     * @return L'objecte {@link User} actualitzat i guardat.
     * @throws RuntimeException Si no es troba cap usuari amb el nom proporcionat.
     * @see UserRepository#findByName(String)
     */
    
    @Transactional
    public User updateUser(String name, User userUpdates){
        // 1. Busquem usuari existent
        User existingUser = userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("usuari no trobat"));
        if(userUpdates.getName() != null)
            existingUser.setName(userUpdates.getName());
        if(userUpdates.getPassword() != null)
            existingUser.setPassword(passwordEncoder.encode(userUpdates.getPassword()));
        if(userUpdates.getEmail() != null)
            existingUser.setEmail(userUpdates.getEmail());
        if(userUpdates.getRole() != null)
            existingUser.setRole(userUpdates.getRole());
        
        return userRepository.save(existingUser);
        
    }

    public User getUserByToken(String token) {
        String name = activeSessions.get(token).getUsername();
        return userRepository.findByName(name).get();
    }
}