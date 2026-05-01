package com.example.sportspot.domain.model

/**
 * Model de domini que representa un esdeveniment esportiu.
 *
 * Un esdeveniment és una activitat organitzada per un usuari
 * en una pista concreta, a la qual altres usuaris es poden apuntar
 * fins arribar a la capacitat màxima de la pista.
 *
 * @property id Identificador únic de l'esdeveniment.
 * @property title Títol descriptiu de l'esdeveniment.
 * @property courtName Nom de la pista on es realitza.
 * @property organizerName Nom de l'usuari que ha creat l'esdeveniment.
 * @property dateTime Data i hora de l'esdeveniment en format ISO-8601.
 * @property currentParticipants Nombre actual de participants inscrits.
 * @property maxCapacity Capacitat màxima de participants (definida per la pista).
 * @property participantNames Llista amb els noms dels participants inscrits.
 *
 * @author Jesús Ramos
 */
data class Event(
    val id: Long,
    val title: String,
    val courtName: String,
    val organizerName: String,
    val dateTime: String,
    val currentParticipants: Int,
    val maxCapacity: Int,
    val participantNames: List<String>
) {
    /**
     * Retorna true si queda exactament una plaça lliure.
     * S'utilitza per mostrar el badge d'alerta "Quasi ple!" a la UI.
     *
     * @author Jesús Ramos
     */
    val isAlmostFull: Boolean
        get() = maxCapacity - currentParticipants == 1

    /**
     * Retorna true si l'esdeveniment ja ha arribat a la capacitat màxima.
     *
     * @author Jesús Ramos
     */
    val isFull: Boolean
        get() = currentParticipants >= maxCapacity
}