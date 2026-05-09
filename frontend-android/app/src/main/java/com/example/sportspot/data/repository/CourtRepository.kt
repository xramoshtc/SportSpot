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
     * @author Jesús Ramos
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
     * @param capacity Aforament màxim de la pista.
     * @return [Court] amb la pista creada.
     */
    suspend fun createCourt(
        token: String,
        name: String,
        type: String,
        pricePerHour: Double,
        location: String,
        capacity: Int
    ): Court {
        val response = api.createCourt(
            token = token,
            request = CreateCourtRequest(name, type, pricePerHour, location, capacity)
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
     * @param capacity Nou aforament màxim de la pista.
     * @return [Court] amb la pista actualitzada.
     */
    suspend fun updateCourt(
        token: String,
        id: Long,
        name: String,
        type: String,
        pricePerHour: Double,
        location: String,
        capacity: Int
    ): Court {
        val response = api.updateCourt(
            id = id,
            token = token,
            request = UpdateCourtRequest(name, type, pricePerHour, location, capacity)
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
     * @author Jesús Ramos
     *
     * @param token Token de sessió.
     * @param courtId ID de la pista a reservar.
     * @param dateTime Data i hora en format ISO.
     * @param durationHours Durada en hores.
     * @return [Booking] amb la reserva confirmada.
     */
    suspend fun createBooking(
        token: String,
        courtId: Long,
        dateTime: String,
        durationHours: Int
    ): Booking {
        val response = api.createBooking(
            token = token,
            request = CreateBookingRequest(courtId, dateTime, durationHours)
        )
        return response.toBooking()
    }

    /**
     * Obté totes les reserves de l'usuari autenticat.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió.
     * @return Llista de [Booking].
     */
    suspend fun getMyBookings(token: String): List<Booking> {
        return api.getMyBookings(token).map { it.toBooking() }
    }

    /**
     * Obté les franges horàries ocupades d'una pista per a un dia concret.
     *
     * No utilitza el model [Booking] per evitar el problema del userName null
     * que retorna el backend per privacitat. Treballa directament amb
     * la resposta de l'API sense transformar-la.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió.
     * @param courtId ID de la pista.
     * @param date Data en format ISO (YYYY-MM-DD).
     * @return Conjunt de franges horàries ocupades (ex: "10:00", "11:00").
     */
    suspend fun getOccupiedSlots(token: String, courtId: Long, date: String): Set<String> {
        val slots = mutableSetOf<String>()
        api.getCourtBookings(token, courtId)
            .filter { it.dateTime.startsWith(date) }
            .forEach { r ->
                val startHour = r.dateTime.substring(11, 13).toIntOrNull() ?: return@forEach
                repeat(r.durationHours) { offset ->
                    slots.add("%02d:00".format(startHour + offset))
                }
            }
        return slots
    }

    /**
     * Cancel·la una reserva pel seu ID.
     *
     * @author Jesús Ramos
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
     * @author Jesús Ramos
     *
     * @param token Token de sessió.
     * @param bookingId ID de la reserva a modificar.
     * @param dateTime Nova data i hora en format ISO.
     * @param durationHours Durada en hores.
     * @return [Booking] amb les dades actualitzades.
     */
    suspend fun updateBooking(
        token: String,
        bookingId: Long,
        dateTime: String,
        durationHours: Int
    ): Booking {
        android.util.Log.d("UPDATE_BOOKING", "id=$bookingId dateTime=$dateTime durationHours=$durationHours")

        val response = api.updateBooking(
            id = bookingId,
            token = token,
            request = UpdateBookingRequest(dateTime, durationHours)
        )
        return response.toBooking()
    }

    /**
     * Funció d'extensió privada per convertir [BookingResponse] a [Booking].
     *
     * @author Jesús Ramos
     */
    private fun com.example.sportspot.data.remote.BookingResponse.toBooking() = Booking(
        id = id,
        dateTime = dateTime,
        durationHours = durationHours,
        endTime = endTime,
        userName = userName,
        courtName = courtName,
        location = location
    )
}