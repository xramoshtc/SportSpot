package com.example.sportspot.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

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
}

