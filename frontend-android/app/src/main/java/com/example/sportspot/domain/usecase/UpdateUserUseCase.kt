package com.example.sportspot.domain.usecase

import com.example.sportspot.data.repository.UserRepository
import com.example.sportspot.domain.model.UserProfile

/**
 * TEA3 - Cas d'ús per modificar les dades de l'usuari autenticat.
 *
 * Encapsula la crida al repositori d'usuari i exposa una interfície
 * senzilla per a la capa de presentació
 *
 * @author Jesús Ramos
 *
 * @param repository Repositori que gestiona les operacions sobre l'usuari.
 */
class UpdateUserUseCase(
    private val repository: UserRepository
) {

    /**
     * Executa la modificació de les dades de l'usuari.
     *
     * Aquesta funció és `suspend` perquè fa una operació de xarxa a través
     * del repositori. Retorna un objecte [UserProfile] actualitzat si
     * l'operació és correcta, o llença una excepció en cas contrari.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió per autenticar la petició.
     * @param name Nom actual de l'usuari (s'utilitza a la URL).
     * @param newName Nou nom d'usuari.
     * @param newPassword Nova contrasenya.
     * @param newEmail Nou correu electrònic.
     * @return [UserProfile] amb les dades actualitzades.
     */
    suspend operator fun invoke(
        token: String,
        name: String,
        newName: String,
        newPassword: String,
        newEmail: String
    ): UserProfile {
        return repository.updateUser(
            token = token,
            name = name,
            newName = newName,
            newPassword = newPassword,
            newEmail = newEmail
        )
    }
}