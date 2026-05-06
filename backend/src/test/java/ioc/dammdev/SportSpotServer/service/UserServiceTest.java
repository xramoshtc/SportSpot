package ioc.dammdev.SportSpotServer.service;

import ioc.dammdev.SportSpotServer.dto.SessionDTO;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

/**
 * Proves unitàries per a UserService utilitzant Mockito per simular la capa de persistència.
 * Cobreix la lògica de negoci per a autenticació, gestió de sessions i operacions CRUD.
 * * @author Gess Montalbán
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    

    @BeforeEach
    void setUp() {
        // Netegem les sessions estàtiques abans de cada test per evitar interferències
        userService.clearSessions();
        testUser = new User(1L, "Gess", "password123", "gess@test.com", "USER",true);
    }

    // --- TESTS DE LOGIN I SESSIONS ---
    
    
     /**
     * Comprova que un usuari pot fer login correctament si existeix i no té una sessió activa.
     */
//    @Test
//    
//    void testTokenExpiration() {
//        String token = userService.createSession(testUser);
//        
//        SessionDTO session = new SessionDTO("Gess", LocalDateTime.now().minusMinutes(1));
//        assertTrue(session.isExpired());
//    }
    /**
     * Comprova que un usuari té la sessió caducada.
     */
    @Test
    void whenLoginSuccessful_thenReturnsUser() {
        when(userRepository.findByName("Gess")).thenReturn(Optional.of(testUser));
        
        
        when(passwordEncoder.matches("password123", testUser.getPassword())).thenReturn(true);

        Optional<User> result = userService.login("Gess","password123");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Gess");
    }

    /**
     * Comprova que el login falla si l'usuari ja té una sessió activa al mapa de sessions.
     */
    @Test
    void whenUserAlreadyLogged_thenLoginFails() {
        when(userRepository.findByName("Gess")).thenReturn(Optional.of(testUser));
        // Creem una sessió prèvia
        userService.createSession(Optional.of(testUser));

        // Intentem tornar a fer login
        Optional<User> result = userService.login("Gess","password123");

        assertThat(result).isEmpty();
    }

    /**
     * Comprova la creació d'una sessió i la generació d'un token de 8 caràcters amb credencials vàlides.
     */
    @Test
    void whenCorrectCredentials_thenCreateSessionReturnsToken() {
        String token = userService.createSession(Optional.of(testUser));

        assertThat(token).isNotNull();
        assertThat(token.length()).isEqualTo(8);
        assertThat(userService.isValidSession(token)).isTrue();
    }

    /**
     * Verifica que el mètode isAdmin retorna true només quan el token pertany a un usuari amb rol ADMIN.
     */
    @Test
    void whenUserIsAdmin_thenIsAdminReturnsTrue() {
        User admin = new User(2L, "adminUser", "admin123", "admin@test.com", "ADMIN",true);
        String token = userService.createSession(Optional.of(admin));
        
        when(userRepository.findByName("adminUser")).thenReturn(Optional.of(admin));

        boolean isAdmin = userService.isAdmin(token);

        assertThat(isAdmin).isTrue();
    }

    // --- TESTS DE REGISTRE ---

    /**
     * Comprova que si es registra un usuari sense rol, el servei li assigna el rol "USER" per defecte.
     */
    @Test
    void whenRegisterUserWithNoRole_thenAssignsDefaultRole() {
        User newUser = new User(null, "newbie", "pass", "new@test.com", null,true);
        when(userRepository.findByName("newbie")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User savedUser = userService.registerUser(newUser);

        assertThat(savedUser.getRole()).isEqualTo("USER");
        verify(userRepository).save(newUser);
    }

    /**
     * Verifica que el registre falla (retorna null) si el nom d'usuari ja existeix a la base de dades.
     */
    @Test
    void whenRegisterExistingUser_thenReturnsNull() {
        when(userRepository.findByName("Gess")).thenReturn(Optional.of(testUser));

        User result = userService.registerUser(testUser);

        assertThat(result).isNull();
        verify(userRepository, never()).save(any());
    }

    // --- TESTS D'ELIMINACIÓ ---

    /**
     * Prova l'eliminació d'un usuari pel seu nom, verificant que es recupera l'ID i es crida al repositori.
     */
    @Test
    void whenDeleteUserByNameExists_thenReturnsTrue() {
        when(userRepository.findByName("Gess")).thenReturn(Optional.of(testUser));

        boolean deleted = userService.deleteUserByName("Gess");

        assertThat(deleted).isTrue();
        verify(userRepository).deleteById(testUser.getId());
    }

    /**
     * Prova l'eliminació d'un usuari per ID quan l'ID existeix al sistema.
     */
    @Test
    void whenDeleteUserByIdExists_thenReturnsTrue() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean deleted = userService.deleteUser(1L);

        assertThat(deleted).isTrue();
        verify(userRepository).deleteById(1L);
    }

    /**
     * Verifica que si s'intenta eliminar un ID que no existeix, el mètode retorna false i no crida al delete del repositori.
     */
    @Test
    void whenDeleteUserByIdNotExists_thenReturnsFalse() {
        when(userRepository.existsById(99L)).thenReturn(false);

        boolean deleted = userService.deleteUser(99L);

        assertThat(deleted).isFalse();
        verify(userRepository, never()).deleteById(anyLong());
    }

    // --- TESTS DE CONSULTA ---

    /**
     * Comprova que el mètode de llistar usuaris retorna la llista proporcionada pel repositori.
     */
    @Test
    void whenGetAllUsers_thenReturnsList() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<User> users = userService.getAllUsers();

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getName()).isEqualTo("Gess");
    }
    
    /**
     * Verifica que quan l'usuari existeix, el servei recupera el seu ID 
     * i sol·licita l'esborrat correctament al repositori.
     * * @see UserService#deleteUserByName(String)
     */
    @Test
    public void givenExistingUser_whenDeleteUserByName_thenReturnsTrue() {
        // GIVEN
        String name = "joanet";
        User existingUser = new User(75L, name, "pass", "test@test.com", "USER",true);
        
        when(userRepository.findByName(name)).thenReturn(Optional.of(existingUser));
        doNothing().when(userRepository).deleteById(75L);

        // WHEN
        boolean result = userService.deleteUserByName(name);

        // THEN
        assertThat(result).isTrue();
        verify(userRepository).deleteById(75L);
    }
    /**
     * Verifica que si l'usuari no existeix, el servei retorna false 
     * i no intenta realitzar cap operació d'esborrat.
     */
    @Test
    public void givenNonExistentUser_whenDeleteUserByName_thenReturnsFalse() {
        // GIVEN
        String name = "ghostUser";
        when(userRepository.findByName(name)).thenReturn(Optional.empty());

        // WHEN
        boolean result = userService.deleteUserByName(name);

        // THEN
        assertThat(result).isFalse();
        verify(userRepository, never()).deleteById(anyLong());
    }
    
    /**
     * Verifica que quan l'usuari existeix, el servei actualitza els camps 
     * correctament i retorna l'objecte modificat.
     */
    @Test
    public void givenExistingUser_whenUpdateUser_thenReturnsUpdatedUser() {
        // GIVEN
        User existingUser = new User(1L, "joanet", "oldPass", "old@test.com", "USER",true);
        User newInfo = new User(null, "joanet", "newPass", "new@test.com", "ADMIN",true);

        when(userRepository.findByName("joanet")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        // WHEN
        User result = userService.updateUser("joanet", newInfo);

        // THEN
        assertThat(result.getEmail()).isEqualTo("new@test.com");
        assertThat(result.getRole()).isEqualTo("ADMIN");
        verify(userRepository).save(existingUser);
    }

    /**
     * Verifica que el servei llança una excepció quan s'intenta actualitzar 
     * un usuari que no existeix al repositori.
     */
    @Test
    public void givenNonExistentUser_whenUpdateUser_thenThrowsException() {
        // GIVEN
        String name = "unknownUser";
        when(userRepository.findByName(name)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(name, new User());
        });
    }
    
}