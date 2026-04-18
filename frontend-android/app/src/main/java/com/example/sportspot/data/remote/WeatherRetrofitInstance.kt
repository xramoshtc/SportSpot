package com.example.sportspot.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Conté les instàncies de Retrofit per les APIs meteorològiques d'Open-Meteo.
 *
 * Separat del RetrofitInstance principal perquè utilitza URLs base diferents.
 *
 * @author Jesús Ramos
 */
object WeatherRetrofitInstance {

    /**
     * URL base de l'API de geocodificació d'Open-Meteo.
     *
     * @author Jesús Ramos
     */
    private const val GEOCODING_URL = "https://geocoding-api.open-meteo.com/"

    /**
     * URL base de l'API meteorològica d'Open-Meteo.
     *
     * @author Jesús Ramos
     */
    private const val WEATHER_URL = "https://api.open-meteo.com/"

    /**
     * Instància lazy de l'API de geocodificació.
     *
     * @author Jesús Ramos
     */
    val geocodingApi: GeocodingApi by lazy {
        Retrofit.Builder()
            .baseUrl(GEOCODING_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingApi::class.java)
    }

    /**
     * Instància lazy de l'API meteorològica.
     *
     * @author Jesús Ramos
     */
    val weatherApi: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}