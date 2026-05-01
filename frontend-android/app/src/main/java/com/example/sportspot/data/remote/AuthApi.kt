package com.example.sportspot.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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
 * Petició per registrar un nou usuari client sense token.
 *
 * @property name Nom d'usuari.
 * @property password Contrasenya.
 * @property email Correu electrònic.
 */
data class RegisterRequest(
    val name: String,
    val password: String,
    val email: String
)

/**
 * Dades necessàries per actualitzar la informació d'un usuari.
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
    val password: String?,
    val email: String,
    val role: String?
)

/**
 * Resposta del servidor amb les dades d'un usuari.
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
 * Resposta del servidor amb les dades d'una pista esportiva.
 *
 * Aquest objecte es retorna en consultes de pistes disponibles o
 * després de crear o modificar una pista.
 *
 * @property id Identificador únic de la pista.
 * @property name Nom de la pista.
 * @property type Tipus d'esport associat.
 * @property location Localització física de la pista.
 * @property pricePerHour Preu de lloguer per hora.
 * @property capacity Aforament màxim de la pista.
 */
data class CourtResponse(
    val id: Long,
    val name: String,
    val type: String,
    val location: String,
    val pricePerHour: Double,
    val capacity: Int
)

/**
 * Petició per crear una nova pista esportiva.
 *
 * @property name Nom de la pista.
 * @property type Tipus d'esport.
 * @property pricePerHour Preu per hora en euros.
 * @property location Localització de la pista.
 *
 * @author Jesús Ramos
 */
data class CreateCourtRequest(
    val name: String,
    val type: String,
    val pricePerHour: Double,
    val location: String
)

/**
 * Petició per modificar una pista esportiva existent.
 *
 * @property name Nou nom de la pista.
 * @property type Nou tipus d'esport.
 * @property pricePerHour Nou preu per hora en euros.
 * @property location Nova localització de la pista.
 * @property capacity Aforament de la pista.
 *
 * @author Jesús Ramos
 */
data class UpdateCourtRequest(
    val name: String,
    val type: String,
    val pricePerHour: Double,
    val location: String,
    val capacity: Int = 0
)


/**
 * Petició per crear una nova reserva.
 *
 * @property courtId ID de la pista a reservar.
 * @property dateTime Data i hora en format ISO "YYYY-MM-DDTHH:MM".
 * @property durationMinutes Durada en minuts.
 */
data class CreateBookingRequest(
    val courtId: Long,
    val dateTime: String,
    val durationMinutes: Int
)

/**
 * Petició per modificar una reserva existent.
 *
 * @property dateTime Nova data i hora en format ISO.
 * @property durationMinutes Nova durada en minuts.
 */
data class UpdateBookingRequest(
    val dateTime: String,
    val durationMinutes: Int
)

/**
 * Resposta del servidor amb les dades d'una reserva.
 *
 * Inclou informació de la pista i de l'usuari per facilitar-ne la visualització.
 *
 * @property id Identificador únic de la reserva.
 * @property dateTime Data i hora de la reserva.
 * @property durationMinutes Durada total en minuts.
 * @property userName Nom de l'usuari que ha fet la reserva.
 * @property courtName Nom de la pista reservada.
 * @property location Ubicació de la pista.
 */
data class BookingResponse(
    val id: Long,
    val dateTime: String,
    val durationMinutes: Int,
    val userName: String,
    val courtName: String,
    val location: String
)

/**
 * Cos de la petició per crear un nou esdeveniment.
 *
 * @property title Títol de l'esdeveniment.
 * @property courtId Identificador de la pista on es realitzarà.
 * @property dateTime Data i hora en format ISO-8601 (ex: "2026-05-10T10:00:00").
 *
 * @author Jesús Ramos
 */
data class CreateEventRequest(
    val title: String,
    val courtId: Long,
    val dateTime: String
)

/**
 * Cos de la petició per modificar un esdeveniment existent.
 *
 * Els camps null no es modifiquen al servidor.
 *
 * @property title Nou títol de l'esdeveniment, o null per no modificar.
 * @property courtId Nou ID de pista, o null per no modificar.
 * @property dateTime Nova data i hora, o null per no modificar.
 *
 * @author Jesús Ramos
 */
data class UpdateEventRequest(
    val title: String? = null,
    val courtId: Long? = null,
    val dateTime: String? = null
)
/**
 * Resposta del servidor que representa un esdeveniment.
 *
 * @property id Identificador únic de l'esdeveniment.
 * @property title Títol de l'esdeveniment.
 * @property courtName Nom de la pista on es realitza.
 * @property organizerName Nom de l'usuari organitzador.
 * @property dateTime Data i hora de l'esdeveniment.
 * @property currentParticipants Nombre actual de participants.
 * @property maxCapacity Capacitat màxima de la pista.
 * @property participantNames Llista de noms dels participants inscrits.
 *
 * @author Jesús Ramos
 */
data class EventResponse(
    val id: Long,
    val title: String,
    val courtName: String,
    val organizerName: String,
    val dateTime: String,
    val currentParticipants: Int,
    val maxCapacity: Int,
    val participantNames: List<String>
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
     * Crida POST pública per registrar un nou usuari client.
     *
     * @author Jesús Ramos
     *
     * @param request Objecte amb les dades del nou usuari.
     * @return UserResponse amb les dades de l'usuari creat.
     */
    @POST("api/users/newuser")
    suspend fun register(@Body request: RegisterRequest): UserResponse

    /**
     * Crida PUT per actualitzar les dades d'un usuari existent.
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
     * Crida GET per obtenir el perfil de l'usuari autenticat.
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

    /**
     * Crida DELETE per eliminar un usuari existent.
     *
     * @author Jesús Ramos
     *
     * @param name Nom de l'usuari a eliminar (s'envia a la URL).
     * @param token Token de sessió per autenticar la petició.
     */
    @DELETE("api/users/{name}")
    suspend fun deleteUser(
        @Path("name") name: String,
        @Header("Session-Token") token: String
    ): Response<Unit>

    /**
     * Retorna la llista de totes les pistes esportives disponibles.
     *
     * @param token Token de sessió per autenticar la petició.
     * @return Llista d'objectes [CourtResponse].
     */
    @GET("api/courts")
    suspend fun getCourts(
        @Header("Session-Token") token: String
    ): List<CourtResponse>

    /**
     * Crea una nova reserva per a l'usuari autenticat.
     *
     * @param token Token de sessió.
     * @param request Dades de la reserva.
     * @return [BookingResponse] amb la reserva confirmada.
     */
    @POST("api/bookings")
    suspend fun createBooking(
        @Header("Session-Token") token: String,
        @Body request: CreateBookingRequest
    ): BookingResponse

    /**
     * Retorna totes les reserves de l'usuari autenticat.
     *
     * @param token Token de sessió.
     * @return Llista de [BookingResponse].
     */
    @GET("api/bookings/my")
    suspend fun getMyBookings(
        @Header("Session-Token") token: String
    ): List<BookingResponse>

    /**
     * Cancel·la una reserva pel seu ID.
     *
     * @param id Identificador de la reserva a cancel·lar.
     * @param token Token de sessió.
     */
    @DELETE("api/bookings/{id}")
    suspend fun deleteBooking(
        @Path("id") id: Long,
        @Header("Session-Token") token: String
    ): Response<Unit>

    /**
     * Modifica l'horari o durada d'una reserva existent.
     *
     * @param id Identificador de la reserva a modificar.
     * @param token Token de sessió.
     * @param request Noves dades de la reserva.
     * @return [BookingResponse] amb la reserva actualitzada.
     */
    @PUT("api/bookings/{id}")
    suspend fun updateBooking(
        @Path("id") id: Long,
        @Header("Session-Token") token: String,
        @Body request: UpdateBookingRequest
    ): BookingResponse

    /**
     * Crida POST per crear una nova pista. Només per a administradors.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió d'administrador.
     * @param request Dades de la nova pista.
     * @return [CourtResponse] amb la pista creada.
     */
    @POST("api/courts")
    suspend fun createCourt(
        @Header("Session-Token") token: String,
        @Body request: CreateCourtRequest
    ): CourtResponse

    /**
     * Crida PUT per modificar una pista existent. Només per a administradors.
     *
     * @author Jesús Ramos
     *
     * @param id Identificador de la pista a modificar.
     * @param token Token de sessió d'administrador.
     * @param request Noves dades de la pista.
     * @return [CourtResponse] amb la pista actualitzada.
     */
    @PUT("api/courts/{id}")
    suspend fun updateCourt(
        @Path("id") id: Long,
        @Header("Session-Token") token: String,
        @Body request: UpdateCourtRequest
    ): CourtResponse

    /**
     * Crida DELETE per eliminar una pista existent. Només per a administradors.
     *
     * @author Jesús Ramos
     *
     * @param id Identificador de la pista a eliminar.
     * @param token Token de sessió d'administrador.
     */
    @DELETE("api/courts/{id}")
    suspend fun deleteCourt(
        @Path("id") id: Long,
        @Header("Session-Token") token: String
    ): Response<Unit>

    /**
     * Crida GET que obté la llista de tots els esdeveniments actius.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió de l'usuari autenticat.
     * @return Llista d'[EventResponse].
     */
    @GET("api/events")
    suspend fun getEvents(
        @Header("Session-Token") token: String
    ): List<EventResponse>

    /**
     * Crida POST que crea un nou esdeveniment.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió de l'usuari autenticat.
     * @param body Dades del nou esdeveniment.
     * @return [EventResponse] amb l'esdeveniment creat.
     */
    @POST("api/events")
    suspend fun createEvent(
        @Header("Session-Token") token: String,
        @Body body: CreateEventRequest
    ): EventResponse

    /**
     * Crida PUT que modifica un esdeveniment existent.
     *
     * @author Jesús Ramos
     *
     * Només l'organitzador o un admin pot modificar l'esdeveniment.
     *
     * @param token Token de sessió.
     * @param id Identificador de l'esdeveniment a modificar.
     * @param body Camps a actualitzar (els null no es modifiquen).
     * @return [EventResponse] amb les dades actualitzades.
     */
    @PUT("api/events/{id}")
    suspend fun updateEvent(
        @Header("Session-Token") token: String,
        @Path("id") id: Long,
        @Body body: UpdateEventRequest
    ): EventResponse

    /**
     * Crida POST que apunta a un esdeveniment existent.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió de l'usuari que es vol inscriure.
     * @param id Identificador de l'esdeveniment.
     */
    @POST("api/events/{id}/join")
    suspend fun joinEvent(
        @Header("Session-Token") token: String,
        @Path("id") id: Long
    )

    /**
     * Crida DELETE que elimina un esdeveniment o abandona'l.
     *
     * @author Jesús Ramos
     *
     * Si l'usuari és l'organitzador o admin, elimina l'esdeveniment.
     * Si és un participant, l'abandona.
     *
     * @param token Token de sessió.
     * @param id Identificador de l'esdeveniment.
     */
    @DELETE("api/events/{id}")
    suspend fun deleteOrLeaveEvent(
        @Header("Session-Token") token: String,
        @Path("id") id: Long
    ): Response<Unit>

}

