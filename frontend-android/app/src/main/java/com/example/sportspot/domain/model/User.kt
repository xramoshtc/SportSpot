package com.example.sportspot.domain.model

/**
 * Representa un usuari autenticat dins l'aplicació.
 *
 * Conté la informació mínima necessària per gestionar la sessió.
 *
 * @author Jesús Ramos
 *
 * @property role Rol de l'usuari (per exemple "user" o "admin").
 * @property token Token de sessió utilitzat per autoritzar peticions al servidor.
 */
data class User(
    val role: String,
    val token: String
)