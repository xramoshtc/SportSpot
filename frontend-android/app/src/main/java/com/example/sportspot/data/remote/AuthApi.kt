package com.example.sportspot.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Petició per fer login al servidor.
 *
 * @property user Nom d'usuari o correu.
 * @property password Contrasenya en text pla.
 */
data class LoginRequest(
    val user: String,
    val password: String
)

/**
 * Petició per fer logout al servidor.
 *
 * @property token Token de sessió que s'ha de tancar.
 */
data class LogoutRequest(
    val token: String
)

/**
 * Resposta del servidor després d'un intent de login.
 *
 * @property success Indica si l'operació ha estat correcte.
 * @property message Missatge descriptiu del servidor.
 * @property resultCode Codi numèric amb el resultat (0 = OK, altres = error).
 * @property sessionToken Token de sessió retornat en cas d'èxit.
 * @property role Rol de l'usuari ("USER", "ADMIN").
 * @property permissions Llista o cadena amb permisos de l'usuari.
 */
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val resultCode: Int,
    val sessionToken: String,
    val role: String,
    val permissions: String
)

/**
 * Resposta del servidor a una petició de logout.
 *
 * @property success Indica si l'operació s'ha considerat exitosa.
 * @property message Missatge descriptiu retornat pel servidor
 * @property resultCode Codi numèric que representa l'estat de la resposta (p. ex. -1, 200).
 * @property sessionToken Token de sessió retornat (pot estar buit si la sessió ja està tancada).
 * @property role Rol de l'usuari associat a la sessió (pot estar buit).
 */
data class LogoutResponse(
    val success: Boolean,
    val message: String,
    val resultCode: Int,
    val sessionToken: String,
    val role: String
)


/**
 * TEA3 - Dades necessàries per actualitzar la informació d'un usuari.
 *
 * Aquest objecte s'envia al servidor en una petició PUT per modificar
 * les dades d'un usuari existent.
 *
 * @property name Nou nom de l'usuari.
 * @property password Nova contrasenya de l'usuari.
 * @property email Nou correu electrònic associat a l'usuari.
 * @property role Nou rol assignat a l'usuari (pot ser null).
 */
data class UpdateUserRequest(
    val name: String,
    val password: String,
    val email: String,
    val role: String?
)

/**
 * TEA3 - Resposta del servidor amb les dades d'un usuari.
 *
 * Aquesta resposta es retorna després d'operacions com la consulta,
 * modificació o creació d'un usuari.
 *
 * @property id Identificador únic de l'usuari.
 * @property name Nom de l'usuari.
 * @property password Contrasenya de l'usuari (encriptada).
 * @property email Correu electrònic associat a l'usuari.
 * @property role Rol assignat a l'usuari dins del sistema.
 * @property active Indica si l'usuari està actiu al sistema.
 */
data class UserResponse(
    val id: Long,
    val name: String,
    val password: String,
    val email: String,
    val role: String,
    val active: Boolean
)


/**
 * Interfície Retrofit per les crides d'autenticació.
 *
 * Conté les rutes per fer login i logout. Les funcions són `suspend`
 * perquè s'han d'executar des de coroutines.
 *
 * @author Jesús Ramos
 *
 */
interface AuthApi {

    /**
     * Crida POST per iniciar sessió.
     *
     * @author Jesús Ramos
     *
     * @param request Objecte amb user i password.
     * @return LoginResponse amb informació de la sessió.
     */
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    /**
     * Crida POST per tancar sessió.
     *
     * @author Jesús Ramos
     *
     * @param request Objecte amb el token de sessió a tancar.
     * @return Resposta del servidor deserialitzada a LogoutResponse.
     */
    @POST("api/logout")
    suspend fun logout(@Body request: LogoutRequest): LogoutResponse

    /**
     * TEA 3 - Crida PUT per actualitzar les dades d'un usuari existent.
     *
     * Aquesta operació permet modificar la informació d'un usuari identificat
     * pel seu nom. Requereix un token de sessió vàlid per autoritzar la petició.
     *
     * @author Jesús Ramos
     *
     * @param name Nom de l'usuari que es vol modificar.
     * @param token Token de sessió necessari per validar la petició.
     * @param request Objecte amb les noves dades de l'usuari.
     *
     * @return Resposta del servidor deserialitzada a UserResponse.
     */
    @PUT("api/users/{name}")
    suspend fun updateUser(
        @Path("name") name: String,
        @Header("Session-Token") token: String,
        @Body request: UpdateUserRequest
    ): UserResponse

    /**
     * TEA3 - Crida GET per obtenir el perfil de l'usuari autenticat.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió per autenticar la petició.
     * @return UserResponse amb les dades de l'usuari autenticat.
     */
    @GET("api/users/me")
    suspend fun getMyProfile(
        @Header("Session-Token") token: String
    ): UserResponse

}

