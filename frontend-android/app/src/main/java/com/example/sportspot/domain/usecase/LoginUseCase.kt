package com.example.sportspot.domain.usecase

import com.example.sportspot.data.repository.AuthRepository
import com.example.sportspot.domain.model.User

/**
 * Cas d'ús per fer el procés de login.
 *
 * Aquesta classe encapsula la crida al repositori d'autenticació i
 * exposa una interfície senzilla per a la capa de presentació.
 *
 * @author Jesús Ramos
 *
 * @param repo Repositori que gestiona les operacions d'autenticació.
 */
class LoginUseCase(
    private val repo: AuthRepository
) {

    /**
     * Executa el login amb usuari i contrasenya.
     *
     * Aquesta funció és `suspend` perquè fa una operació de xarxa a través
     * del repositori. Retorna un objecte [User] amb la informació de sessió
     * si l'autenticació és correcta, o llença una excepció en cas contrari.
     *
     * @author Jesús Ramos
     *
     * @param user Nom d'usuari o identificador.
     * @param password Contrasenya en text pla.
     * @return [User] amb el rol i el token de sessió.
     */
    suspend operator fun invoke(user: String, password: String): User {
        return repo.login(user, password)
    }
}