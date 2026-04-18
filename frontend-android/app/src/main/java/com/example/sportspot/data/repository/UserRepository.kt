package com.example.sportspot.data.repository


import com.example.sportspot.data.remote.RegisterRequest
import com.example.sportspot.data.remote.RetrofitInstance
import com.example.sportspot.data.remote.UpdateUserRequest
import com.example.sportspot.domain.model.UserProfile

/**
 * Repositori que gestiona les operacions sobre l'usuari.
 *
 * S'encarrega de cridar l'API per obtenir i modificar les dades
 * de l'usuari autenticat
 *
 * @author Jesús Ramos
 *
 */
class UserRepository {

    /**
     * Instància de l'API d'autenticació creada per Retrofit.
     *
     * @author Jesús Ramos
     *
     */
    private val api = RetrofitInstance.authApi

    /**
     * TEA3 - Obté el perfil de l'usuari autenticat.
     *
     * Fa una crida GET al servidor amb el token de sessió
     * i retorna un objecte [UserProfile] amb les dades de l'usuari.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió per autenticar la petició.
     * @return [UserProfile] amb les dades de l'usuari.
     * @throws Exception si la crida falla o el token no és vàlid.
     */
    suspend fun getMyProfile(token: String): UserProfile {
        val response = api.getMyProfile(token)
        return UserProfile(
            id = response.id,
            name = response.name,
            email = response.email,
            role = response.role,
            active = response.active
        )
    }

    /**
     * Registra un nou usuari client sense token.
     *
     * @author Jesús Ramos
     *
     * @param name Nom d'usuari.
     * @param password Contrasenya.
     * @param email Correu electrònic.
     * @return [UserProfile] amb les dades de l'usuari creat.
     * @throws Exception si la crida falla.
     */
    suspend fun registerUser(name: String, password: String, email: String): UserProfile {
        val response = api.register(RegisterRequest(name, password, email))
        return UserProfile(
            id = response.id,
            name = response.name,
            email = response.email,
            role = response.role,
            active = response.active
        )
    }

    /**
     * Modifica les dades de l'usuari autenticat.
     *
     * Fa una crida PUT al servidor amb el token de sessió i les
     * noves dades. Retorna un [UserProfile] actualitzat si tot va bé.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió per autenticar la petició.
     * @param name Nom actual de l'usuari (s'utilitza a la URL).
     * @param newName Nou nom d'usuari.
     * @param newPassword Nova contrasenya.
     * @param newEmail Nou correu electrònic.
     * @return [UserProfile] amb les dades actualitzades.
     * @throws Exception si la crida falla o les dades no són vàlides.
     */
    suspend fun updateUser(
        token: String,
        name: String,
        newName: String,
        newPassword: String,
        newEmail: String
    ): UserProfile {
        val request = UpdateUserRequest(
            name = newName,
            // Si password està buit, enviem null perquè el servidor no el modifiqui
            password = newPassword.ifBlank { null },
            email = newEmail,
            role = null
        )
        val response = api.updateUser(
            name = name,
            token = token,
            request = request
        )
        return UserProfile(
            id = response.id,
            name = response.name,
            email = response.email,
            role = response.role,
            active = response.active
        )
    }

    /**
     * Elimina el compte de l'usuari autenticat.
     *
     * Fa una crida DELETE al servidor amb el token de sessió
     * i el nom de l'usuari a eliminar.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió per autenticar la petició.
     * @param name Nom de l'usuari a eliminar.
     * @throws Exception si la crida falla o el token no és vàlid.
     */
    suspend fun deleteUser(token: String, name: String) {
        api.deleteUser(name = name, token = token)
    }
}