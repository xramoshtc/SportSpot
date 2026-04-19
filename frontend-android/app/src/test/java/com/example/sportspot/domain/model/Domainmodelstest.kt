package com.example.sportspot.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

/**
 * Tests unitaris per als models del domini de SportSpot.
 *
 * Comprova el comportament de les data classes: igualtat, còpia amb
 * valors modificats i que els camps s'assignen correctament.
 *
 * Les data classes de Kotlin generen automàticament equals(), hashCode()
 * i copy(), però és bo verificar-ne el comportament per detectar errors
 * de refactorització o canvis de camps.
 *
 * @author Jesús Ramos
 */
class DomainModelsTest {

    // ─────────────────────────────────────────────────────────────
    // Tests de User
    // ─────────────────────────────────────────────────────────────

    /**
     * Comprova que dos objectes [User] amb els mateixos valors són iguals.
     */
    @Test
    fun `User equals retorna true amb mateixos valors`() {
        val user1 = User(role = "user", token = "abc123")
        val user2 = User(role = "user", token = "abc123")
        assertEquals(user1, user2)
    }

    /**
     * Comprova que dos objectes [User] amb rols diferents no són iguals.
     */
    @Test
    fun `User equals retorna false amb rol diferent`() {
        val user1 = User(role = "user", token = "abc123")
        val user2 = User(role = "admin", token = "abc123")
        assertNotEquals(user1, user2)
    }

    /**
     * Comprova que la funció copy() de [User] crea una còpia independent
     * amb el camp modificat correctament.
     */
    @Test
    fun `User copy modifica el camp indicat`() {
        val original = User(role = "user", token = "abc123")
        val copia = original.copy(role = "admin")
        assertEquals("admin", copia.role)
        assertEquals("abc123", copia.token)
    }

    // ─────────────────────────────────────────────────────────────
    // Tests de UserProfile
    // ─────────────────────────────────────────────────────────────

    /**
     * Comprova que els camps de [UserProfile] s'assignen correctament
     * i que l'objecte és actiu per defecte en el test.
     */
    @Test
    fun `UserProfile camps s'assignen correctament`() {
        val profile = UserProfile(
            id = 1,
            name = "jesusramos",
            email = "jesus@sportspot.cat",
            role = "user",
            active = true
        )
        assertEquals(1, profile.id)
        assertEquals("jesusramos", profile.name)
        assertEquals("jesus@sportspot.cat", profile.email)
        assertEquals("user", profile.role)
        assertTrue(profile.active)
    }

    /**
     * Comprova que un [UserProfile] amb active=false es pot crear i distingir.
     */
    @Test
    fun `UserProfile active false es representa correctament`() {
        val profile = UserProfile(
            id = 2,
            name = "usuariInactiu",
            email = "inactiu@sportspot.cat",
            role = "user",
            active = false
        )
        assertFalse(profile.active)
    }

    /**
     * Comprova que dos [UserProfile] amb el mateix id però email diferent no són iguals.
     */
    @Test
    fun `UserProfile equals detecta diferencies en email`() {
        val p1 = UserProfile(1, "jesus", "jesus@a.cat", "user", true)
        val p2 = UserProfile(1, "jesus", "jesus@b.cat", "user", true)
        assertNotEquals(p1, p2)
    }

    // ─────────────────────────────────────────────────────────────
    // Tests de Court
    // ─────────────────────────────────────────────────────────────

    /**
     * Comprova que els camps de [Court] s'assignen correctament.
     */
    @Test
    fun `Court camps s'assignen correctament`() {
        val court = Court(
            id = 10,
            name = "Pista Central",
            type = "Pàdel",
            location = "Barcelona",
            pricePerHour = 15.0,
            capacity = 4
        )
        assertEquals(10, court.id)
        assertEquals("Pista Central", court.name)
        assertEquals("Pàdel", court.type)
        assertEquals("Barcelona", court.location)
        assertEquals(15.0, court.pricePerHour, 0.001)
        assertEquals(4, court.capacity)
    }

    /**
     * Comprova que copy() de [Court] permet actualitzar el preu
     * sense modificar la resta de camps.
     */
    @Test
    fun `Court copy actualitza el preu correctament`() {
        val original = Court(1, "Pista A", "Tennis", "Madrid", 10.0, 2)
        val actualitzat = original.copy(pricePerHour = 20.0)
        assertEquals(20.0, actualitzat.pricePerHour, 0.001)
        assertEquals("Pista A", actualitzat.name)
    }

    /**
     * Comprova que dos [Court] amb preus diferents no són iguals.
     */
    @Test
    fun `Court equals detecta diferencies en preu`() {
        val c1 = Court(1, "Pista A", "Tennis", "Madrid", 10.0, 2)
        val c2 = Court(1, "Pista A", "Tennis", "Madrid", 20.0, 2)
        assertNotEquals(c1, c2)
    }

    // ─────────────────────────────────────────────────────────────
    // Tests de Booking
    // ─────────────────────────────────────────────────────────────

    /**
     * Comprova que els camps de [Booking] s'assignen correctament.
     */
    @Test
    fun `Booking camps s'assignen correctament`() {
        val booking = Booking(
            id = 100,
            dateTime = "2025-06-15T10:00",
            durationMinutes = 60,
            userName = "jesusramos",
            courtName = "Pista Central",
            location = "Barcelona"
        )
        assertEquals(100, booking.id)
        assertEquals("2025-06-15T10:00", booking.dateTime)
        assertEquals(60, booking.durationMinutes)
        assertEquals("jesusramos", booking.userName)
        assertEquals("Pista Central", booking.courtName)
        assertEquals("Barcelona", booking.location)
    }

    /**
     * Comprova que dues reserves amb el mateix ID i camps iguals són iguals.
     */
    @Test
    fun `Booking equals retorna true amb mateixos valors`() {
        val b1 = Booking(1, "2025-06-15T10:00", 60, "jesus", "Pista A", "Barcelona")
        val b2 = Booking(1, "2025-06-15T10:00", 60, "jesus", "Pista A", "Barcelona")
        assertEquals(b1, b2)
    }

    /**
     * Comprova que dues reserves amb durades diferents no són iguals.
     */
    @Test
    fun `Booking equals detecta diferencia en durada`() {
        val b1 = Booking(1, "2025-06-15T10:00", 60, "jesus", "Pista A", "Barcelona")
        val b2 = Booking(1, "2025-06-15T10:00", 90, "jesus", "Pista A", "Barcelona")
        assertNotEquals(b1, b2)
    }

    // ─────────────────────────────────────────────────────────────
    // Tests de DayWeather
    // ─────────────────────────────────────────────────────────────

    /**
     * Comprova que els camps de [DayWeather] s'assignen correctament.
     */
    @Test
    fun `DayWeather camps s'assignen correctament`() {
        val weather = DayWeather(
            date = "2025-06-15",
            tempMax = 32.5,
            tempMin = 18.0,
            precipitationProbability = 10,
            windspeedMax = 15.0,
            weatherCode = 0
        )
        assertEquals("2025-06-15", weather.date)
        assertEquals(32.5, weather.tempMax, 0.001)
        assertEquals(18.0, weather.tempMin, 0.001)
        assertEquals(10, weather.precipitationProbability)
        assertEquals(15.0, weather.windspeedMax, 0.001)
        assertEquals(0, weather.weatherCode)
    }

    /**
     * Comprova que la temperatura màxima pot ser inferior a la mínima
     * (dada provinent de l'API, el model no valida).
     */
    @Test
    fun `DayWeather accepta valors de temperatura invertits sense validar`() {
        // El model de domini no valida, la responsabilitat és de l'API
        val weather = DayWeather(
            date = "2025-01-01",
            tempMax = 5.0,
            tempMin = 10.0,
            precipitationProbability = 80,
            windspeedMax = 40.0,
            weatherCode = 65
        )
        // Simplement comprovem que l'objecte es crea sense errors
        assertEquals(5.0, weather.tempMax, 0.001)
        assertEquals(10.0, weather.tempMin, 0.001)
    }
}