package com.example.sportspot.data.repository

import com.example.sportspot.data.remote.CreateBookingRequest
import com.example.sportspot.data.remote.CreateCourtRequest
import com.example.sportspot.data.remote.RetrofitInstance
import com.example.sportspot.data.remote.UpdateBookingRequest
import com.example.sportspot.data.remote.UpdateCourtRequest
import com.example.sportspot.domain.model.Booking
import com.example.sportspot.domain.model.Court

/**
 * Repositori que gestiona les operacions sobre pistes i reserves.
 *
 * S'encarrega de cridar l'API i transformar les respostes en
 * objectes del domini.
 *
 * @author Jesús Ramos
 */
class CourtRepository {

    private val api = RetrofitInstance.authApi

    /**
     * Obté la llista de totes les pistes disponibles.
     *
     * @param token Token de sessió per autenticar la petició.
     * @return Llista d'objectes [Court].
     */
    suspend fun getCourts(token: String): List<Court> {
        return api.getCourts(token).map { r ->
            Court(
                id = r.id,
                name = r.name,
                type = r.type,
                location = r.location,
                pricePerHour = r.pricePerHour,
                capacity = r.capacity
            )
        }
    }

    /**
     * Crea una nova pista esportiva. Només per a administradors.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió d'administrador.
     * @param name Nom de la nova pista.
     * @param type Tipus d'esport.
     * @param pricePerHour Preu per hora en euros.
     * @param location Localització de la pista.
     * @return [Court] amb la pista creada.
     */
    suspend fun createCourt(
        token: String,
        name: String,
        type: String,
        pricePerHour: Double,
        location: String
    ): Court {
        val response = api.createCourt(
            token = token,
            request = CreateCourtRequest(name, type, pricePerHour, location)
        )
        return Court(
            id = response.id,
            name = response.name,
            type = response.type,
            location = response.location,
            pricePerHour = response.pricePerHour,
            capacity = response.capacity
        )
    }

    /**
     * Modifica una pista esportiva existent. Només per a administradors.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió d'administrador.
     * @param id Identificador de la pista a modificar.
     * @param name Nou nom de la pista.
     * @param type Nou tipus d'esport.
     * @param pricePerHour Nou preu per hora en euros.
     * @param location Nova localització de la pista.
     * @return [Court] amb la pista actualitzada.
     */
    suspend fun updateCourt(
        token: String,
        id: Long,
        name: String,
        type: String,
        pricePerHour: Double,
        location: String
    ): Court {
        val response = api.updateCourt(
            id = id,
            token = token,
            request = UpdateCourtRequest(name, type, pricePerHour, location)
        )
        return Court(
            id = response.id,
            name = response.name,
            type = response.type,
            location = response.location,
            pricePerHour = response.pricePerHour,
            capacity = response.capacity
        )
    }

    /**
     * Elimina una pista esportiva. Només per a administradors.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió d'administrador.
     * @param id Identificador de la pista a eliminar.
     */
    suspend fun deleteCourt(token: String, id: Long) {
        api.deleteCourt(id = id, token = token)
    }


    /**
     * Crea una nova reserva per a l'usuari autenticat.
     *
     * @param token Token de sessió.
     * @param courtId ID de la pista a reservar.
     * @param dateTime Data i hora en format ISO.
     * @param durationMinutes Durada en minuts.
     * @return [Booking] amb la reserva confirmada.
     */
    suspend fun createBooking(
        token: String,
        courtId: Long,
        dateTime: String,
        durationMinutes: Int
    ): Booking {
        val response = api.createBooking(
            token = token,
            request = CreateBookingRequest(courtId, dateTime, durationMinutes)
        )
        return response.toBooking()
    }

    /**
     * Obté totes les reserves de l'usuari autenticat.
     *
     * @param token Token de sessió.
     * @return Llista de [Booking].
     */
    suspend fun getMyBookings(token: String): List<Booking> {
        return api.getMyBookings(token).map { it.toBooking() }
    }

    /**
     * Cancel·la una reserva pel seu ID.
     *
     * @param token Token de sessió.
     * @param bookingId ID de la reserva a cancel·lar.
     */
    suspend fun deleteBooking(token: String, bookingId: Long) {
        api.deleteBooking(id = bookingId, token = token)
    }

    /**
     * Modifica l'horari o durada d'una reserva existent.
     *
     * @param token Token de sessió.
     * @param bookingId ID de la reserva a modificar.
     * @param dateTime Nova data i hora en format ISO.
     * @param durationMinutes Nova durada en minuts.
     * @return [Booking] amb les dades actualitzades.
     */
    suspend fun updateBooking(
        token: String,
        bookingId: Long,
        dateTime: String,
        durationMinutes: Int
    ): Booking {
        val response = api.updateBooking(
            id = bookingId,
            token = token,
            request = UpdateBookingRequest(dateTime, durationMinutes)
        )
        return response.toBooking()
    }

    /**
     * Funció d'extensió privada per convertir [BookingResponse] a [Booking].
     */
    private fun com.example.sportspot.data.remote.BookingResponse.toBooking() = Booking(
        id = id,
        dateTime = dateTime,
        durationMinutes = durationMinutes,
        userName = userName,
        courtName = courtName,
        location = location
    )
}