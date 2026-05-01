package com.example.sportspot.data.repository

import com.example.sportspot.data.remote.CreateEventRequest
import com.example.sportspot.data.remote.RetrofitInstance
import com.example.sportspot.data.remote.UpdateEventRequest
import com.example.sportspot.domain.model.Event

/**
 * Repositori que gestiona totes les operacions sobre esdeveniments.
 *
 * S'encarrega de comunicar-se amb l'API REST i transformar les respostes
 * del servidor en objectes del domini [Event].
 *
 * @author Jesús Ramos
 */
class EventRepository {

    private val api = RetrofitInstance.authApi

    /**
     * Obté la llista de tots els esdeveniments actius del servidor.
     *
     * @param token Token de sessió de l'usuari autenticat.
     * @return Llista d'objectes [Event] del domini.
     *
     * @author Jesús Ramos
     */
    suspend fun getEvents(token: String): List<Event> {
        return api.getEvents(token).map { r ->
            Event(
                id = r.id,
                title = r.title,
                courtName = r.courtName,
                organizerName = r.organizerName,
                dateTime = r.dateTime,
                currentParticipants = r.currentParticipants,
                maxCapacity = r.maxCapacity,
                participantNames = r.participantNames
            )
        }
    }

    /**
     * Crea un nou esdeveniment al servidor.
     *
     * @param token Token de sessió de l'usuari organitzador.
     * @param title Títol de l'esdeveniment.
     * @param courtId Identificador de la pista on es realitzarà.
     * @param dateTime Data i hora en format ISO-8601 (ex: "2026-05-10T10:00:00").
     * @return [Event] amb les dades de l'esdeveniment creat.
     *
     * @author Jesús Ramos
     */
    suspend fun createEvent(
        token: String,
        title: String,
        courtId: Long,
        dateTime: String
    ): Event {
        val response = api.createEvent(
            token = token,
            body = CreateEventRequest(
                title = title,
                courtId = courtId,
                dateTime = dateTime
            )
        )
        return Event(
            id = response.id,
            title = response.title,
            courtName = response.courtName,
            organizerName = response.organizerName,
            dateTime = response.dateTime,
            currentParticipants = response.currentParticipants,
            maxCapacity = response.maxCapacity,
            participantNames = response.participantNames
        )
    }

    /**
     * Modifica un esdeveniment existent.
     *
     * Només l'organitzador o un administrador pot fer aquesta operació.
     * Els camps amb valor null no s'actualitzen al servidor.
     *
     * @param token Token de sessió.
     * @param eventId Identificador de l'esdeveniment a modificar.
     * @param title Nou títol, o null per no modificar.
     * @param courtId Nou ID de pista, o null per no modificar.
     * @param dateTime Nova data i hora, o null per no modificar.
     * @return [Event] actualitzat.
     *
     * @author Jesús Ramos
     */
    suspend fun updateEvent(
        token: String,
        eventId: Long,
        title: String? = null,
        courtId: Long? = null,
        dateTime: String? = null
    ): Event {
        val response = api.updateEvent(
            token = token,
            id = eventId,
            body = UpdateEventRequest(
                title = title,
                courtId = courtId,
                dateTime = dateTime
            )
        )
        return Event(
            id = response.id,
            title = response.title,
            courtName = response.courtName,
            organizerName = response.organizerName,
            dateTime = response.dateTime,
            currentParticipants = response.currentParticipants,
            maxCapacity = response.maxCapacity,
            participantNames = response.participantNames
        )
    }

    /**
     * Apunta l'usuari autenticat a un esdeveniment.
     *
     * Si l'esdeveniment ja és ple, el servidor retornarà 403.
     * Si l'usuari ja hi és inscrit, el servidor retornarà 409.
     *
     * @param token Token de sessió de l'usuari que es vol inscriure.
     * @param eventId Identificador de l'esdeveniment.
     *
     * @author Jesús Ramos
     */
    suspend fun joinEvent(token: String, eventId: Long) {
        api.joinEvent(token = token, id = eventId)
    }

    /**
     * Elimina un esdeveniment o abandona'l segons el rol de l'usuari.
     *
     * - Si l'usuari és l'organitzador o admin: elimina l'esdeveniment completament.
     * - Si l'usuari és un participant: l'abandona i es treu de la llista.
     *
     * @param token Token de sessió.
     * @param eventId Identificador de l'esdeveniment.
     *
     * @author Jesús Ramos
     */
    suspend fun deleteOrLeaveEvent(token: String, eventId: Long) {
        api.deleteOrLeaveEvent(token = token, id = eventId)
    }
}