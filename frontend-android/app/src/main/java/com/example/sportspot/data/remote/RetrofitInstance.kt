package com.example.sportspot.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Conté la configuració de Retrofit per crides remotes.
 *
 * Aquí es defineix la URL base i l'instància de l'API d'autenticació.
 * L'objecte és singleton perquè només cal una instància de Retrofit.
 *
 * @author Jesús Ramos
 *
 */
object RetrofitInstance {

    /**
     * URL base del servidor on es troba exposada l'API
     *
     * @author Jesús Ramos
     *
     */
    private const val BASE_URL = "http://10.2.3.145:8080/"

    /**
     * Instanciació ajornada (lazy) de l'API d'autenticació.
     *
     * Es crea quan es necessita per primera vegada. Utilitza Gson per convertir
     * automàticament JSON a objectes Kotlin i viceversa.
     *
     * @author Jesús Ramos
     *
     */
    val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }
}