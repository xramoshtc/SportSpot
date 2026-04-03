package ioc.dammdev.SportSpotServer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ioc.dammdev.SportSpotServer.model.User;
import ioc.dammdev.SportSpotServer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * Proves d'integració per a la capa UserController.
 * Utilitza MockMvc per simular peticions HTTP sense aixecar el servidor real.
 * Es fa servir @MockBean per simular el comportament de la capa de servei.
 * * @author Gess Montalbán
 */
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private User adminUser;
    private final String VALID_TOKEN = "test-token-123";
    
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        adminUser = new User(1L, "admin", "1234", "admin@test.com", "ADMIN",true);
    }
/**
     * Verifica que un usuari autenticat pot recuperar el seu propi perfil.
     * S'espera un codi 200 OK i que la contrasenya estigui ocultada.
     * @author Gess Montalbán
     */
    @Test
    void whenGetMyProfileWithValidToken_thenReturnsUserJson() throws Exception {
        // GIVEN
        String token = "valid-session-token";
        User sessionUser = new User(1L, "Gess", "password123", "gess@test.com", "USER", true);

        // Simulem que la sessió és vàlida i el servei ens retorna l'usuari associat al token
        when(userService.isValidSession(token)).thenReturn(true);
        when(userService.getUserByToken(token)).thenReturn(sessionUser);

        // WHEN & THEN
        mockMvc.perform(get("/api/users/me")
                .header("Session-Token", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gess"))
                .andExpect(jsonPath("$.email").value("gess@test.com"))
                // El controlador hauria de retornar la password ofuscada/oculta
                .andExpect(jsonPath("$.password").value("********"));
    }

    /**
     * Verifica que si s'intenta accedir al perfil sense un token vàlid,
     * el sistema retorna un 401 Unauthorized.
     * @author Gess Montalbán
     */
    @Test
    void whenGetMyProfileWithInvalidToken_thenReturns401Unauthorized() throws Exception {
        // GIVEN
        String invalidToken = "expired-or-fake-token";
        when(userService.isValidSession(invalidToken)).thenReturn(false);

        // WHEN & THEN
        mockMvc.perform(get("/api/users/me")
                .header("Session-Token", invalidToken))
                .andExpect(status().isUnauthorized());
    }
    /**
     * Verifica que un administrador amb un token vàlid pot eliminar un usuari.
     * S'espera un codi de resposta 204 (No Content).
     */
    @Test
    void whenDeleteUserAsAdmin_thenReturns204() throws Exception {
        // Simulem les validacions de seguretat del servei
        when(userService.isValidToken(VALID_TOKEN)).thenReturn(true);
        when(userService.isValidSession(VALID_TOKEN)).thenReturn(true);
        when(userService.isAdmin(VALID_TOKEN)).thenReturn(true);
        
        // Simulem que l'usuari a esborrar existeix
        when(userService.deleteUserByName("joanet")).thenReturn(true);

        mockMvc.perform(delete("/api/users/joanet")
                .header("Session-Token", VALID_TOKEN))
                .andExpect(status().isNoContent());
    }
    /**
     * Verifica que un client amb un token vàlid pot eliminar el seu usuari.
     * S'espera un codi de resposta 204 (No Content).
     */
    @Test
    void whenDeleteClientWithValidToken_thenReturns204() throws Exception {
        // Simulem les validacions de seguretat del servei
        when(userService.isValidToken(VALID_TOKEN)).thenReturn(true);
        when(userService.isValidSession(VALID_TOKEN)).thenReturn(true);
        when(userService.isAdmin(VALID_TOKEN)).thenReturn(false);
        when(userService.isTokenOfName(VALID_TOKEN, "joanet")).thenReturn(true);
        
        // Simulem que l'usuari a esborrar existeix
        when(userService.deleteUserByName("joanet")).thenReturn(true);

        mockMvc.perform(delete("/api/users/joanet")
                .header("Session-Token", VALID_TOKEN))
                .andExpect(status().isNoContent());
    } 

    /**
     * Verifica que si s'intenta eliminar un usuari sense el token a la capçalera,
     * el sistema denega l'accés amb un codi 403 (Forbidden).
     */
    @Test
    void whenDeleteUserWithoutToken_thenReturns403() throws Exception {
        mockMvc.perform(delete("/api/users/joanet"))
                .andExpect(status().isForbidden());
    }

    /**
     * Verifica que si un usuari no existeix, el sistema retorna un 404 (Not Found).
     */
    @Test
    void whenDeleteNonExistentUser_thenReturns404() throws Exception {
        when(userService.isValidToken(VALID_TOKEN)).thenReturn(true);
        when(userService.isValidSession(VALID_TOKEN)).thenReturn(true);
        when(userService.isAdmin(VALID_TOKEN)).thenReturn(true);
        
        // Simulem que el servei no troba l'usuari
        when(userService.deleteUserByName("inexistent")).thenReturn(false);

        mockMvc.perform(delete("/api/users/inexistent")
                .header("Session-Token", VALID_TOKEN))
                .andExpect(status().isNotFound());
    }

    /**
     * Comprova que la petició GET per llistar usuaris retorna la llista en format JSON.
     * Verifica que l'estructura del JSON coincideixi amb l'esperada.
     */
    @Test
    void whenGetAllUsers_thenReturnsJsonList() throws Exception {
        
        when(userService.isValidToken(VALID_TOKEN)).thenReturn(true);
        when(userService.isValidSession(VALID_TOKEN)).thenReturn(true);
        when(userService.isAdmin(VALID_TOKEN)).thenReturn(true);
        when(userService.getAllUsers()).thenReturn(Arrays.asList(adminUser));

        mockMvc.perform(get("/api/users")
                .header("Session-Token", VALID_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("admin"))
                .andExpect(jsonPath("$[0].role").value("ADMIN"));
    }
    /**
     * Testeja que l'API retorna un 404 Not Found si s'intenta esborrar 
     * un usuari que no existeix a la base de dades.
     */
    @Test
    public void givenNonExistentUser_whenDeleteUser_thenReturns404NotFound() throws Exception {
        // GIVEN
        String name = "unknown";
        
        
        when(userService.isAdmin(VALID_TOKEN)).thenReturn(true);
        when(userService.isValidSession(VALID_TOKEN)).thenReturn(true);
        when(userService.isValidToken(VALID_TOKEN)).thenReturn(true);
        when(userService.deleteUserByName(name)).thenReturn(false);

        // WHEN & THEN
        mockMvc.perform(delete("/api/users/{name}", name)
                .header("Session-Token", VALID_TOKEN))
                .andExpect(status().isNotFound());
    }

    /**
     * Verifica que les peticions sense permisos d'administrador són rebutjades 
     * amb un codi 403 Forbidden.
     */
    @Test
    public void givenStandardUserToken_whenDeleteUser_thenReturns403Forbidden() throws Exception {
        // GIVEN
        String token = "user-token";
        when(userService.isAdmin(token)).thenReturn(false);

        // WHEN & THEN
        mockMvc.perform(delete("/api/users/anybody")
                .header("Session-Token", token))
                .andExpect(status().isForbidden());
    }
    
    /**
     * Testeja que un administrador pot actualitzar un usuari enviant un JSON.
     * S'espera un codi 200 OK i el JSON de l'usuari actualitzat.
     */
    @Test
    public void givenAdminToken_whenPutUser_thenReturns200Ok() throws Exception {
        // GIVEN
        String name = "joanet";
        String token = "admin-token";
        User updatedUser = new User(1L, name, "pass", "new@email.com", "USER",true);

        when(userService.isAdmin(token)).thenReturn(true);
        when(userService.updateUser(eq(name), any(User.class))).thenReturn(updatedUser);

        // WHEN & THEN
        mockMvc.perform(put("/api/users/{name}", name)
                .header("Session-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@email.com"));
    }

    /**
     * Verifica que si el servei no troba l'usuari, el controlador respon 
     * amb un codi 404 Not Found.
     */
    @Test
    public void givenNonExistentUser_whenPutUser_thenReturns404NotFound() throws Exception {
        // GIVEN
        String name = "ghost";
        String token = "admin-token";
        
        when(userService.isAdmin(anyString())).thenReturn(true);
        when(userService.updateUser(eq(name), any(User.class)))
                .thenThrow(new RuntimeException("Not found"));

        // WHEN & THEN
        mockMvc.perform(put("/api/users/{name}", name)
                .header("Session-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    /**
     * Verifica que un usuari sense permisos d'administrador no pot 
     * realitzar actualitzacions (403 Forbidden).
     */
    @Test
    public void givenUserToken_whenPutUser_thenReturns403Forbidden() throws Exception {
        // GIVEN
        String token = "user-token";
        when(userService.isAdmin(token)).thenReturn(false);

        // WHEN & THEN
        mockMvc.perform(put("/api/users/anybody")
                .header("Session-Token", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());
    }
}