package com.example.sportspot.domain.model

/**
 * TEA3 - Representa el perfil complet d'un usuari autenticat.
 *
 * Conté tota la informació de l'usuari retornada pel servidor
 *
 * @author Jesús Ramos
 *
 * @property id Identificador únic de l'usuari al servidor.
 * @property name Nom d'usuari.
 * @property email Correu electrònic de l'usuari.
 * @property role Rol de l'usuari ("user" o "admin").
 * @property active Indica si el compte està actiu.
 */
data class UserProfile(
    val id: Long,
    val name: String,
    val email: String,
    val role: String,
    val active: Boolean
)