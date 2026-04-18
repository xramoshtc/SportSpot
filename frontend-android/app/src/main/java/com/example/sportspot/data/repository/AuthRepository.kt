package com.example.sportspot.data.repository

import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.remote.LoginRequest
import com.example.sportspot.data.remote.LogoutRequest
import com.example.sportspot.data.remote.RetrofitInstance
import com.example.sportspot.domain.model.User

/**
 * Repositori que gestiona l'autenticació i el token local.
 *
 * S'encarrega de cridar l'API d'autenticació i de guardar/esborrar
 * el token al DataStore local.
 *
 * @author Jesús Ramos
 *
 * @param dataStore Manager per accedir a les preferències locals.
 */
class AuthRepository(
    private val dataStore: DataStoreManager
) {

    /**
     * Instància de l'API d'autenticació creada per Retrofit.
     *
     * @author Jesús Ramos
     *
     */
    private val api = RetrofitInstance.authApi

    /**
     * Realitza el procés de login amb usuari i contrasenya.
     *
     * Fa la crida al servidor i comprova la resposta. Si la resposta
     * indica error, llença una excepció amb un missatge senzill.
     * Si tot va bé, guarda el token al DataStore i retorna un objecte User
     * amb la informació mínima necessària.
     *
     * Aquesta funció és `suspend` perquè fa una operació de xarxa.
     *
     * @author Jesús Ramos
     *
     * @param user Nom d'usuari o identificador.
     * @param password Contrasenya en text pla.
     * @return User amb el rol i el token de sessió.
     * @throws Exception si les credencials són incorrectes o la resposta no és 200.
     */
    suspend fun login(user: String, password: String): User {
        val response = api.login(LoginRequest(user, password))

        if (!response.success || response.resultCode != 200) {
            throw Exception("Credencials incorrectes")
        }

        val normalizedRole = response.role.lowercase()
        dataStore.saveToken(response.sessionToken)
        dataStore.saveRole(normalizedRole)
        dataStore.saveUsername(user)

        return User(
            role = normalizedRole,
            token = response.sessionToken
        )
    }

    /**
     * Tanca la sessió al servidor i esborra el token local.
     *
     * Fa la crida de logout al servidor i, independentment de la resposta,
     * elimina el token guardat localment per assegurar que l'usuari quedi desconnectat.
     *
     * Aquesta funció és `suspend` perquè fa una operació de xarxa i d'E/S.
     *
     * @author Jesús Ramos
     *
     * @param token Token de sessió que s'ha de tancar.
     */
    suspend fun logout(token: String) {
        api.logout(LogoutRequest(token))
        dataStore.clearToken()
        dataStore.clearRole()
        dataStore.clearUsername()
    }


}