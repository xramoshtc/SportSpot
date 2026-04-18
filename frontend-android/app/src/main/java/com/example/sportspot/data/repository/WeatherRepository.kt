package com.example.sportspot.data.repository

import com.example.sportspot.data.remote.WeatherRetrofitInstance
import com.example.sportspot.domain.model.DayWeather

/**
 * Repositori que gestiona les consultes meteorològiques.
 *
 * Encadena la geocodificació de la ciutat amb la consulta de previsió
 * meteorològica d'Open-Meteo.
 *
 * @author Jesús Ramos
 */
class WeatherRepository {

    private val geocodingApi = WeatherRetrofitInstance.geocodingApi
    private val weatherApi = WeatherRetrofitInstance.weatherApi

    /**
     * Obté la previsió meteorològica per a una ciutat i una data concreta.
     *
     * Primer busca les coordenades de la ciutat i després consulta
     * la previsió per a aquelles coordenades. Retorna null si la ciutat
     * no es troba o si no hi ha dades per a la data indicada.
     *
     * @author Jesús Ramos
     *
     * @param city Nom de la ciutat (ex: "Barcelona").
     * @param date Data en format ISO (YYYY-MM-DD).
     * @return [DayWeather] amb les dades del dia o null si no es troben.
     */
    suspend fun getWeatherForCityAndDate(city: String, date: String): DayWeather? {
        // Pas 1: geocodificació → coordenades
        val geoResult = geocodingApi.searchCity(city).results?.firstOrNull()
            ?: return null

        // Log per veure les coordenades que s'estan utilitzant
        android.util.Log.d("WEATHER", "Ciutat: $city → lat: ${geoResult.latitude}, lon: ${geoResult.longitude}, nom trobat: ${geoResult.name}")


        // Pas 2: previsió meteorològica per coordenades
        val forecast = weatherApi.getForecast(
            latitude = geoResult.latitude,
            longitude = geoResult.longitude
        )

        // Pas 3: filtrem el dia que ens interessa
        val index = forecast.daily.time.indexOf(date)
        if (index == -1) return null

        return DayWeather(
            date = date,
            tempMax = forecast.daily.temperature_2m_max[index],
            tempMin = forecast.daily.temperature_2m_min[index],
            precipitationProbability = forecast.daily.precipitation_probability_max[index],
            windspeedMax = forecast.daily.windspeed_10m_max[index],
            weatherCode = forecast.daily.weathercode[index]
        )
    }
}