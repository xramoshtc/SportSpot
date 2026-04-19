package com.example.sportspot.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests unitaris per a l'objecte [WeatherHelper].
 *
 * Comprova que els codis meteorològics WMO es converteixen correctament
 * en emojis i descripcions de text en català.
 *
 * No necessita mocks ni coroutines perquè és lògica pura sense efectes secundaris.
 *
 * @author Jesús Ramos
 */
class WeatherHelperTest {

    // ─────────────────────────────────────────────────────────────
    // Tests de weatherEmoji()
    // ─────────────────────────────────────────────────────────────

    /**
     * Comprova que el codi 0 (cel clar) retorna l'emoji de sol.
     */
    @Test
    fun `weatherEmoji codi 0 retorna sol`() {
        assertEquals("☀️", WeatherHelper.weatherEmoji(0))
    }

    /**
     * Comprova que els codis 1 i 2 (parcialment ennuvolat) retornen
     * l'emoji de sol amb núvol.
     */
    @Test
    fun `weatherEmoji codis 1 i 2 retornen sol amb nuvol`() {
        assertEquals("🌤️", WeatherHelper.weatherEmoji(1))
        assertEquals("🌤️", WeatherHelper.weatherEmoji(2))
    }

    /**
     * Comprova que el codi 3 (ennuvolat) retorna l'emoji de núvol.
     */
    @Test
    fun `weatherEmoji codi 3 retorna nuvol`() {
        assertEquals("☁️", WeatherHelper.weatherEmoji(3))
    }

    /**
     * Comprova que els codis 45 i 48 (boira) retornen l'emoji de boira.
     */
    @Test
    fun `weatherEmoji codis 45 i 48 retornen boira`() {
        assertEquals("🌫️", WeatherHelper.weatherEmoji(45))
        assertEquals("🌫️", WeatherHelper.weatherEmoji(48))
    }

    /**
     * Comprova que els codis 51, 53 i 55 (plugim) retornen l'emoji de pluja lleugera.
     */
    @Test
    fun `weatherEmoji codis 51 53 55 retornen plugim`() {
        assertEquals("🌦️", WeatherHelper.weatherEmoji(51))
        assertEquals("🌦️", WeatherHelper.weatherEmoji(53))
        assertEquals("🌦️", WeatherHelper.weatherEmoji(55))
    }

    /**
     * Comprova que els codis 61, 63 i 65 (pluja) retornen l'emoji de pluja.
     */
    @Test
    fun `weatherEmoji codis 61 63 65 retornen pluja`() {
        assertEquals("🌧️", WeatherHelper.weatherEmoji(61))
        assertEquals("🌧️", WeatherHelper.weatherEmoji(63))
        assertEquals("🌧️", WeatherHelper.weatherEmoji(65))
    }

    /**
     * Comprova que els codis 71, 73 i 75 (neu) retornen l'emoji de neu.
     */
    @Test
    fun `weatherEmoji codis 71 73 75 retornen neu`() {
        assertEquals("🌨️", WeatherHelper.weatherEmoji(71))
        assertEquals("🌨️", WeatherHelper.weatherEmoji(73))
        assertEquals("🌨️", WeatherHelper.weatherEmoji(75))
    }

    /**
     * Comprova que els codis 80, 81 i 82 (ruixats) retornen l'emoji de pluja.
     */
    @Test
    fun `weatherEmoji codis 80 81 82 retornen ruixats`() {
        assertEquals("🌧️", WeatherHelper.weatherEmoji(80))
        assertEquals("🌧️", WeatherHelper.weatherEmoji(81))
        assertEquals("🌧️", WeatherHelper.weatherEmoji(82))
    }

    /**
     * Comprova que el codi 95 (tempesta) retorna l'emoji de tempesta.
     */
    @Test
    fun `weatherEmoji codi 95 retorna tempesta`() {
        assertEquals("⛈️", WeatherHelper.weatherEmoji(95))
    }

    /**
     * Comprova que els codis 96 i 99 (tempesta amb pedra) retornen l'emoji de tempesta.
     */
    @Test
    fun `weatherEmoji codis 96 i 99 retornen tempesta amb pedra`() {
        assertEquals("⛈️", WeatherHelper.weatherEmoji(96))
        assertEquals("⛈️", WeatherHelper.weatherEmoji(99))
    }

    /**
     * Comprova que un codi desconegut retorna l'emoji per defecte (termòmetre).
     */
    @Test
    fun `weatherEmoji codi desconegut retorna emoji per defecte`() {
        assertEquals("🌡️", WeatherHelper.weatherEmoji(999))
        assertEquals("🌡️", WeatherHelper.weatherEmoji(-1))
    }

    // ─────────────────────────────────────────────────────────────
    // Tests de weatherDescription()
    // ─────────────────────────────────────────────────────────────

    /**
     * Comprova que el codi 0 retorna la descripció "Cel clar".
     */
    @Test
    fun `weatherDescription codi 0 retorna cel clar`() {
        assertEquals("Cel clar", WeatherHelper.weatherDescription(0))
    }

    /**
     * Comprova que els codis 1 i 2 retornen "Parcialment ennuvolat".
     */
    @Test
    fun `weatherDescription codis 1 i 2 retornen parcialment ennuvolat`() {
        assertEquals("Parcialment ennuvolat", WeatherHelper.weatherDescription(1))
        assertEquals("Parcialment ennuvolat", WeatherHelper.weatherDescription(2))
    }

    /**
     * Comprova que el codi 3 retorna "Ennuvolat".
     */
    @Test
    fun `weatherDescription codi 3 retorna ennuvolat`() {
        assertEquals("Ennuvolat", WeatherHelper.weatherDescription(3))
    }

    /**
     * Comprova que els codis 45 i 48 retornen "Boira".
     */
    @Test
    fun `weatherDescription codis 45 i 48 retornen boira`() {
        assertEquals("Boira", WeatherHelper.weatherDescription(45))
        assertEquals("Boira", WeatherHelper.weatherDescription(48))
    }

    /**
     * Comprova que els codis 51, 53 i 55 retornen "Plugim".
     */
    @Test
    fun `weatherDescription codis 51 53 55 retornen plugim`() {
        assertEquals("Plugim", WeatherHelper.weatherDescription(51))
        assertEquals("Plugim", WeatherHelper.weatherDescription(53))
        assertEquals("Plugim", WeatherHelper.weatherDescription(55))
    }

    /**
     * Comprova que els codis 61, 63 i 65 retornen "Pluja".
     */
    @Test
    fun `weatherDescription codis 61 63 65 retornen pluja`() {
        assertEquals("Pluja", WeatherHelper.weatherDescription(61))
        assertEquals("Pluja", WeatherHelper.weatherDescription(63))
        assertEquals("Pluja", WeatherHelper.weatherDescription(65))
    }

    /**
     * Comprova que els codis 71, 73 i 75 retornen "Neu".
     */
    @Test
    fun `weatherDescription codis 71 73 75 retornen neu`() {
        assertEquals("Neu", WeatherHelper.weatherDescription(71))
        assertEquals("Neu", WeatherHelper.weatherDescription(73))
        assertEquals("Neu", WeatherHelper.weatherDescription(75))
    }

    /**
     * Comprova que els codis 80, 81 i 82 retornen "Ruixats".
     */
    @Test
    fun `weatherDescription codis 80 81 82 retornen ruixats`() {
        assertEquals("Ruixats", WeatherHelper.weatherDescription(80))
        assertEquals("Ruixats", WeatherHelper.weatherDescription(81))
        assertEquals("Ruixats", WeatherHelper.weatherDescription(82))
    }

    /**
     * Comprova que el codi 95 retorna "Tempesta".
     */
    @Test
    fun `weatherDescription codi 95 retorna tempesta`() {
        assertEquals("Tempesta", WeatherHelper.weatherDescription(95))
    }

    /**
     * Comprova que els codis 96 i 99 retornen "Tempesta amb pedra".
     */
    @Test
    fun `weatherDescription codis 96 i 99 retornen tempesta amb pedra`() {
        assertEquals("Tempesta amb pedra", WeatherHelper.weatherDescription(96))
        assertEquals("Tempesta amb pedra", WeatherHelper.weatherDescription(99))
    }

    /**
     * Comprova que un codi desconegut retorna "Variable".
     */
    @Test
    fun `weatherDescription codi desconegut retorna variable`() {
        assertEquals("Variable", WeatherHelper.weatherDescription(999))
        assertEquals("Variable", WeatherHelper.weatherDescription(-1))
    }
}