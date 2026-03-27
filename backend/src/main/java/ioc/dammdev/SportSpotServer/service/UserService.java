package ioc.dammdev.SportSpotServer.service;

import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

/**
 * Capa de servei per gestionar la lògica de negoci dels usuaris.
 * Separa el controlador de la base de dades per complir amb l'arquitectura en capes.
 * @author Gess Montalbán
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
      // Guardarem en memòria l'usuari i token de sessió
    // Clau: Token (String) | Valor: Nom d'usuari (String)
    private static final java.util.Map<String, String> sessionsActives = new java.util.HashMap<>();
    

    /**
     * Gestiona el procés de login. 
     * @param username: nom d'usuari
     * @return LoginResponse: respossta que s'envia al client
     * @author Gess Montalbán
     */
    public Optional<User> login(String username) {
        Optional<User> user = userRepository.findByName(username);
        
          User userBD = user.get();
          
        // Comprovem que no hi ha una sessió activa
        if (!isLogged(userBD.getName()))  { 
            
                return user;
        } else 
               return Optional.empty();
            
    }
      /**
     * Gestiona el procés de logout.
     * @param token: id de la sessió
     * @return boolean: respossta que s'envia al client
     * @author Gess Montalban
     */
    public boolean logout(String token){
        if (sessionsActives.containsKey(token)){
            closeSession(token);
           return true;
        }
        else
            return false;
    }
    
    
    
    public String createSession(Optional<User> user, String password){
        
        // Comprovem si l'usuari existeix i si la contrasenya coincideix
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            String token = UUID.randomUUID().toString().substring(0, 8);
            sessionsActives.put(token, user.get().getName());
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
    
    public boolean isValidSession(String token){
        return sessionsActives.containsKey(token);
                              
        
    }
    
    public boolean isLogged(String userName){
        if (sessionsActives.containsValue(userName))
            return true;
        else 
            return false;
    }
            
    public void closeSession(String token){
        sessionsActives.remove(token);
    }
    
    public void clearSessions(){
        sessionsActives.clear();
    }
    /**
     * Registra un usuari nou. 
     * Podem afegir validacions de negoci (ex: si el nom ja està agafat).
     */
    public User registerUser(User user) {
        // Lògica de negoci extra aquí
        return userRepository.save(user);
    }
}