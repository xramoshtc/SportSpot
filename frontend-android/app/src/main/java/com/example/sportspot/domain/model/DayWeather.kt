package com.example.sportspot.domain.model

/**
 * Representa la previsió meteorològica d'un dia concret.
 *
 * @property date Data en format ISO (YYYY-MM-DD).
 * @property tempMax Temperatura màxima del dia en °C.
 * @property tempMin Temperatura mínima del dia en °C.
 * @property precipitationProbability Probabilitat de precipitació en %.
 * @property windspeedMax Velocitat màxima del vent en km/h.
 * @property weatherCode Codi WMO que descriu el temps predominant.
 *
 * @author Jesús Ramos
 */
data class DayWeather(
    val date: String,
    val tempMax: Double,
    val tempMin: Double,
    val precipitationProbability: Int,
    val windspeedMax: Double,
    val weatherCode: Int
)