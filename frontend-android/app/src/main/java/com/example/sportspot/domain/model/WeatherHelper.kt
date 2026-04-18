package com.example.sportspot.domain.model

/**
 * Funcions d'utilitat per interpretar codis meteorològics WMO.
 *
 * Converteix el codi numèric retornat per Open-Meteo en una descripció
 * llegible i un emoji representatiu.
 *
 * @author Jesús Ramos
 */
object WeatherHelper {

    /**
     * Retorna un emoji representatiu per a un codi meteorològic WMO.
     *
     * @author Jesús Ramos
     *
     * @param code Codi WMO retornat per Open-Meteo.
     * @return Emoji que representa el temps.
     */
    fun weatherEmoji(code: Int): String = when (code) {
        0 -> "☀️"
        1, 2 -> "🌤️"
        3 -> "☁️"
        45, 48 -> "🌫️"
        51, 53, 55 -> "🌦️"
        61, 63, 65 -> "🌧️"
        71, 73, 75 -> "🌨️"
        80, 81, 82 -> "🌧️"
        95 -> "⛈️"
        96, 99 -> "⛈️"
        else -> "🌡️"
    }

    /**
     * Retorna una descripció textual per a un codi meteorològic WMO.
     *
     * @author Jesús Ramos
     *
     * @param code Codi WMO retornat per Open-Meteo.
     * @return Descripció del temps en català.
     */
    fun weatherDescription(code: Int): String = when (code) {
        0 -> "Cel clar"
        1, 2 -> "Parcialment ennuvolat"
        3 -> "Ennuvolat"
        45, 48 -> "Boira"
        51, 53, 55 -> "Plugim"
        61, 63, 65 -> "Pluja"
        71, 73, 75 -> "Neu"
        80, 81, 82 -> "Ruixats"
        95 -> "Tempesta"
        96, 99 -> "Tempesta amb pedra"
        else -> "Variable"
    }
}
