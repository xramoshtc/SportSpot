package com.example.sportspot.domain.model

/**
 * Representa una pista esportiva disponible per reservar.
 *
 * @author Jesús Ramos
 *
 * @property id Identificador únic de la pista.
 * @property name Nom de la pista.
 * @property type Tipus d'esport (Pàdel, Tennis, Bàsquet...).
 * @property location Localització de la pista.
 * @property pricePerHour Preu per hora en euros.
 * @property capacity Aforament de la pista.
 */
data class Court(
    val id: Long,
    val name: String,
    val type: String,
    val location: String,
    val pricePerHour: Double,
    val capacity: Int
)