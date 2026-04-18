package com.example.sportspot.domain.usecase

import com.example.sportspot.data.repository.UserRepository

/**
 * TEA3 - Cas d'ús per eliminar el compte de l'usuari autenticat.
 *
 * Encapsula la crida al repositori d'usuari i exposa una interfície
 * senzilla per a la capa de presentació.
 *
 * @author Jesús Ramos
 *
 * @param repository Repositori que gestiona les operacions sobre l'usuari.
 */
class DeleteUserUseCase(
    private val repository: UserRepository
) {

    /**
     * Executa l'eliminació del compte de l'usuari.
     *
     * Aquesta funció és `suspend` perquè fa una operació de xarxa a través
     * del repositori. Llença una excepció si l'operació no és correcta.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió per autenticar la petició.
     * @param name Nom de l'usuari a eliminar.
     */
    suspend operator fun invoke(token: String, name: String) {
        repository.deleteUser(token = token, name = name)
    }
}