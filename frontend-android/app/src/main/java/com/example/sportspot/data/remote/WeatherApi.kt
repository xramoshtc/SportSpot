package com.example.sportspot.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfície Retrofit per les crides a l'API de geocodificació d'Open-Meteo.
 *
 * Converteix el nom d'una ciutat a coordenades geogràfiques.
 *
 * @author Jesús Ramos
 */
interface GeocodingApi {

    /**
     * Cerca les coordenades d'una ciutat pel seu nom.
     *
     * @param name Nom de la ciutat a cercar.
     * @param count Nombre màxim de resultats (per defecte 1).
     * @return [GeocodingResponse] amb la llista de resultats.
     */
    @GET("v1/search")
    suspend fun searchCity(
        @Query("name") name: String,
        @Query("count") count: Int = 1,
        @Query("language") language: String = "ca",
        @Query("format") format: String = "json"
    ): GeocodingResponse
}

/**
 * Interfície Retrofit per les crides a l'API meteorològica d'Open-Meteo.
 *
 * Retorna la previsió del temps per a unes coordenades i dates concretes.
 *
 * @author Jesús Ramos
 */
interface WeatherApi {

    /**
     * Obté la previsió diària del temps per a unes coordenades.
     *
     * @param latitude Latitud de la ubicació.
     * @param longitude Longitud de la ubicació.
     * @param daily Variables meteorològiques diàries a obtenir.
     * @param timezone Zona horària per als resultats.
     * @return [WeatherResponse] amb la previsió sol·licitada.
     */
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,precipitation_probability_max,windspeed_10m_max,weathercode",
        @Query("timezone") timezone: String = "Europe/Madrid"
    ): WeatherResponse
}

// --- Data classes de geocodificació ---

/**
 * Resposta de l'API de geocodificació amb la llista de ciutats trobades.
 *
 * @property results Llista de resultats de la cerca.
 *
 * @author Jesús Ramos
 */
data class GeocodingResponse(
    val results: List<GeocodingResult>?
)

/**
 * Resultat individual de la cerca de geocodificació.
 *
 * @property latitude Latitud de la ciutat.
 * @property longitude Longitud de la ciutat.
 * @property name Nom de la ciutat.
 *
 * @author Jesús Ramos
 */
data class GeocodingResult(
    val latitude: Double,
    val longitude: Double,
    val name: String
)

// --- Data classes meteorològiques ---

/**
 * Resposta de l'API meteorològica amb les dades diàries.
 *
 * @property daily Objecte amb les llistes de valors diaris.
 *
 * @author Jesús Ramos
 */
data class WeatherResponse(
    val daily: DailyWeather
)

/**
 * Dades meteorològiques diàries retornades per Open-Meteo.
 *
 * Cada llista té un valor per dia, en el mateix ordre.
 *
 * @property time Llista de dates en format ISO (YYYY-MM-DD).
 * @property temperature_2m_max Temperatures màximes diàries en °C.
 * @property temperature_2m_min Temperatures mínimes diàries en °C.
 * @property precipitation_probability_max Probabilitat màxima de precipitació (%).
 * @property windspeed_10m_max Velocitat màxima del vent (km/h).
 * @property weathercode Codi WMO del temps predominant del dia.
 *
 * @author Jesús Ramos
 */
data class DailyWeather(
    val time: List<String>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val precipitation_probability_max: List<Int>,
    val windspeed_10m_max: List<Double>,
    val weathercode: List<Int>
)