package com.example.sportspot.data.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Conté la configuració de Retrofit per crides remotes amb HTTPS.
 *
 * Configura OkHttp per confiar en el certificat autosignat del servidor,
 * permetent comunicacions xifrades amb TLS.
 *
 * @author Jesús Ramos
 */
object RetrofitInstance {

    /**
     * URL base del servidor amb HTTPS i port 8443.
     *
     * @author Jesús Ramos
     */
    private const val BASE_URL = "https://10.2.3.145:8443/"

    /**
     * Gson configurat per deserialitzar dates en format ISO-8601
     * que retorna el backend com a LocalDateTime.
     *
     * @author Jesús Ramos
     */
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create()

    /**
     * Instanciació ajornada (lazy) de l'API d'autenticació.
     *
     * Configura OkHttp amb el certificat autosignat del servidor per
     * permetre connexions HTTPS.
     *
     * @author Jesús Ramos
     */
    val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(AuthApi::class.java)
    }
}