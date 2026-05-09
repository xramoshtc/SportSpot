package com.example.sportspot.domain.model

/**
 * Representa una reserva d'una pista esportiva.
 *
 * @author Jesús Ramos
 *
 * @property id Identificador únic de la reserva.
 * @property dateTime Data i hora de la reserva en format ISO (YYYY-MM-DDTHH:MM).
 * @property durationHours Durada de la reserva en hores.
 * @property endTime Data i hora de la finalització de la reserva en format ISO (YYYY-MM-DDTHH:MM).
 * @property userName Nom de l'usuari que ha fet la reserva.
 * @property courtName Nom de la pista reservada.
 * @property location Localització de la pista reservada.
 */
data class Booking(
    val id: Long,
    val dateTime: String,
    val durationHours: Int,
    val endTime: String,
    val userName: String,
    val courtName: String,
    val location: String
)