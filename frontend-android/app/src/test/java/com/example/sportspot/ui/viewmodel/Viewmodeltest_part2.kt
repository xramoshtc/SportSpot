package com.example.sportspot.ui.viewmodel

import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.repository.AuthRepository
import com.example.sportspot.data.repository.CourtRepository
import com.example.sportspot.data.repository.UserRepository
import com.example.sportspot.data.repository.WeatherRepository
import com.example.sportspot.domain.model.Booking
import com.example.sportspot.domain.model.Court
import com.example.sportspot.domain.model.DayWeather
import com.example.sportspot.domain.model.UserProfile
import com.example.sportspot.domain.usecase.DeleteUserUseCase
import com.example.sportspot.domain.usecase.UpdateUserUseCase
import com.example.sportspot.ui.admin.AdminCourtsUiState
import com.example.sportspot.ui.admin.AdminCourtsViewModel
import com.example.sportspot.ui.bookings.MyBookingsUiState
import com.example.sportspot.ui.bookings.MyBookingsViewModel
import com.example.sportspot.ui.courts.CourtDetailUiState
import com.example.sportspot.ui.courts.CourtDetailViewModel
import com.example.sportspot.ui.profile.ProfileUiState
import com.example.sportspot.ui.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Tests unitaris per als ViewModels de perfil, reserves, administració i detall de pista.
 *
 * @author Jesús Ramos
 */

// ─────────────────────────────────────────────────────────────────────────────
// ProfileViewModelTest
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Tests per a [ProfileViewModel].
 *
 * Comprova la càrrega del perfil, la modificació de dades, l'eliminació
 * de compte i el cas especial de logout automàtic en canviar les credencials.
 *
 * @author Jesús Ramos
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var updateUserUseCase: UpdateUserUseCase
    private lateinit var deleteUserUseCase: DeleteUserUseCase
    private lateinit var userRepository: UserRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var dataStore: DataStoreManager
    private lateinit var viewModel: ProfileViewModel

    /**
     * Configura tots els mocks i el ViewModel abans de cada test.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        updateUserUseCase = mock()
        deleteUserUseCase = mock()
        userRepository = mock()
        authRepository = mock()
        dataStore = mock()
        viewModel = ProfileViewModel(
            updateUserUseCase = updateUserUseCase,
            deleteUserUseCase = deleteUserUseCase,
            repository = userRepository,
            authRepository = authRepository,
            dataStore = dataStore
        )
    }

    /**
     * Restaura el dispatcher original després de cada test.
     */
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Comprova que l'estat inicial és Idle.
     */
    @Test
    fun `estat inicial es Idle`() {
        assertTrue(viewModel.uiState.value is ProfileUiState.Idle)
    }

    /**
     * Comprova que loadProfile() transiciona a Success amb el perfil de l'usuari.
     */
    @Test
    fun `loadProfile transiciona a Success amb el perfil`() = runTest {
        val profile = UserProfile(1L, "jesus", "jesus@sportspot.cat", "user", true)
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(userRepository.getMyProfile("token_valid")).thenReturn(profile)

        viewModel.loadProfile()
        advanceUntilIdle()

        val state = viewModel.uiState.value as ProfileUiState.Success
        assertEquals("jesus", state.profile.name)
        assertEquals("jesus@sportspot.cat", state.profile.email)
    }

    /**
     * Comprova que loadProfile() transiciona a Error quan no hi ha token.
     */
    @Test
    fun `loadProfile transiciona a Error quan no hi ha token`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf(null))

        viewModel.loadProfile()
        advanceUntilIdle()

        val state = viewModel.uiState.value as ProfileUiState.Error
        assertEquals("Sense token", state.message)
    }

    /**
     * Comprova que updateUser() transiciona a Success quan el nom NO canvia.
     *
     * Si el nom és el mateix i la contrasenya és buida, les credencials
     * NO han canviat → l'usuari NO ha de tancar sessió.
     *
     * NOTA: El mock del DataStore ha de retornar el token dues vegades
     * perquè el ViewModel fa tokenFlow.first() dues vegades en aquest flux.
     *
     * @author Jesús Ramos
     */
    @Test
    fun `updateUser transiciona a Success quan les credencials no canvien`() = runTest {
        val updatedProfile = UserProfile(1L, "jesus", "nou@sportspot.cat", "user", true)
        // tokenFlow s'usa dues vegades: un per updateUserUseCase i un per la comprovació interna
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(
            updateUserUseCase("token_valid", "jesus", "jesus", "", "nou@sportspot.cat")
        ).thenReturn(updatedProfile)

        // El nom no canvia i la contrasenya és buida → sense logout
        viewModel.updateUser("jesus", "jesus", "", "nou@sportspot.cat")
        advanceUntilIdle()

        // Comprovem amb assertTrue primer per donar un missatge clar si falla
        val state = viewModel.uiState.value
        assertTrue("S'esperava Success però era: $state", state is ProfileUiState.Success)
        assertEquals("nou@sportspot.cat", (state as ProfileUiState.Success).profile.email)
    }

    /**
     * Comprova que updateUser() transiciona a LoggedOut quan el nom canvia.
     *
     * Si el nom d'usuari canvia, les credencials han canviat → logout automàtic.
     * El ViewModel crida authRepository.logout(token), que cal mockejar.
     *
     * @author Jesús Ramos
     */
    @Test
    fun `updateUser transiciona a LoggedOut quan el nom canvia`() = runTest {
        val updatedProfile = UserProfile(1L, "nounom", "jesus@sportspot.cat", "user", true)
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(
            updateUserUseCase("token_valid", "jesus", "nounom", "", "jesus@sportspot.cat")
        ).thenReturn(updatedProfile)
        // Cal mockejar logout perquè el ViewModel el crida quan canvien les credencials
        whenever(authRepository.logout("token_valid")).thenReturn(Unit)

        viewModel.updateUser("jesus", "nounom", "", "jesus@sportspot.cat")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("S'esperava LoggedOut però era: $state", state is ProfileUiState.LoggedOut)
    }

    /**
     * Comprova que updateUser() transiciona a LoggedOut quan la contrasenya canvia.
     *
     * Si la nova contrasenya no és buida, les credencials han canviat → logout.
     * El ViewModel crida authRepository.logout(token), que cal mockejar.
     *
     * @author Jesús Ramos
     */
    @Test
    fun `updateUser transiciona a LoggedOut quan la contrasenya canvia`() = runTest {
        val updatedProfile = UserProfile(1L, "jesus", "jesus@sportspot.cat", "user", true)
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(
            updateUserUseCase("token_valid", "jesus", "jesus", "novapass", "jesus@sportspot.cat")
        ).thenReturn(updatedProfile)
        // Cal mockejar logout perquè el ViewModel el crida quan canvien les credencials
        whenever(authRepository.logout("token_valid")).thenReturn(Unit)

        viewModel.updateUser("jesus", "jesus", "novapass", "jesus@sportspot.cat")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("S'esperava LoggedOut però era: $state", state is ProfileUiState.LoggedOut)
    }

    /**
     * Comprova que deleteUser() transiciona a Deleted i neteja la sessió.
     */
    @Test
    fun `deleteUser transiciona a Deleted`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))

        viewModel.deleteUser("jesus")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is ProfileUiState.Deleted)
        // Comprova que es neteja la sessió local
        verify(dataStore).clearToken()
        verify(dataStore).clearRole()
    }

    /**
     * Comprova que deleteUser() transiciona a Error quan el repositori falla.
     */
    @Test
    fun `deleteUser transiciona a Error quan el repositori falla`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(deleteUserUseCase("token_valid", "jesus"))
            .thenAnswer { throw Exception("Error en eliminar") }

        viewModel.deleteUser("jesus")
        advanceUntilIdle()

        val state = viewModel.uiState.value as ProfileUiState.Error
        assertEquals("Error en eliminar", state.message)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// MyBookingsViewModelTest
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Tests per a [MyBookingsViewModel].
 *
 * Comprova la càrrega, cancel·lació i modificació de reserves.
 * El [WeatherRepository] es mockeja per no fer crides reals a l'API del temps.
 *
 * @author Jesús Ramos
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MyBookingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var courtRepository: CourtRepository
    private lateinit var dataStore: DataStoreManager
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var viewModel: MyBookingsViewModel

    // Reserva de prova reutilitzable
    private val bookingTest = Booking(
        id = 1L,
        dateTime = "2025-06-15T10:00",
        durationMinutes = 60,
        userName = "jesus",
        courtName = "Pista A",
        location = "Barcelona"
    )

    /**
     * Configura els mocks i el ViewModel abans de cada test.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        courtRepository = mock()
        dataStore = mock()
        weatherRepository = mock()
        viewModel = MyBookingsViewModel(courtRepository, dataStore, weatherRepository)
    }

    /**
     * Restaura el dispatcher original després de cada test.
     */
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Comprova que l'estat inicial és Idle.
     */
    @Test
    fun `estat inicial es Idle`() {
        assertTrue(viewModel.uiState.value is MyBookingsUiState.Idle)
    }

    /**
     * Comprova que loadBookings() transiciona a Success amb la llista de reserves.
     */
    @Test
    fun `loadBookings transiciona a Success amb les reserves`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(courtRepository.getMyBookings("token_valid")).thenReturn(listOf(bookingTest))
        // El temps pot fallar sense afectar l'estat principal
        whenever(weatherRepository.getWeatherForCityAndDate("Barcelona", "2025-06-15"))
            .thenReturn(null)

        viewModel.loadBookings()
        advanceUntilIdle()

        val state = viewModel.uiState.value as MyBookingsUiState.Success
        assertEquals(1, state.bookings.size)
        assertEquals("Pista A", state.bookings[0].courtName)
    }

    /**
     * Comprova que loadBookings() transiciona a Error quan no hi ha token.
     */
    @Test
    fun `loadBookings transiciona a Error quan no hi ha token`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf(null))

        viewModel.loadBookings()
        advanceUntilIdle()

        val state = viewModel.uiState.value as MyBookingsUiState.Error
        assertEquals("Sense token", state.message)
    }

    /**
     * Comprova que loadBookings() retorna una llista buida quan no hi ha reserves.
     */
    @Test
    fun `loadBookings Success amb llista buida si no hi ha reserves`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(courtRepository.getMyBookings("token_valid")).thenReturn(emptyList())

        viewModel.loadBookings()
        advanceUntilIdle()

        val state = viewModel.uiState.value as MyBookingsUiState.Success
        assertTrue(state.bookings.isEmpty())
    }

    /**
     * Comprova que deleteBooking() crida el repositori i recarrega les reserves.
     *
     * Després d'eliminar, s'hauria de tornar a cridar getMyBookings.
     */
    @Test
    fun `deleteBooking crida el repositori i recarrega les reserves`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(courtRepository.getMyBookings("token_valid")).thenReturn(emptyList())

        viewModel.deleteBooking(1L)
        advanceUntilIdle()

        // Comprova que s'ha cridat deleteBooking i getMyBookings (recàrrega)
        verify(courtRepository).deleteBooking("token_valid", 1L)
        verify(courtRepository).getMyBookings("token_valid")
    }

    /**
     * Comprova que updateBooking() crida el repositori i recarrega les reserves.
     */
    @Test
    fun `updateBooking crida el repositori i recarrega les reserves`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(courtRepository.getMyBookings("token_valid")).thenReturn(emptyList())

        viewModel.updateBooking(1L, "2025-07-01T11:00", 90)
        advanceUntilIdle()

        verify(courtRepository).updateBooking("token_valid", 1L, "2025-07-01T11:00", 90)
        verify(courtRepository).getMyBookings("token_valid")
    }

    /**
     * Comprova que el mapa de temps s'omple correctament per a una reserva.
     */
    @Test
    fun `loadBookings emplena el mapa de temps per a cada reserva`() = runTest {
        val weather = DayWeather("2025-06-15", 28.0, 18.0, 10, 12.0, 0)
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(courtRepository.getMyBookings("token_valid")).thenReturn(listOf(bookingTest))
        whenever(weatherRepository.getWeatherForCityAndDate("Barcelona", "2025-06-15"))
            .thenReturn(weather)

        viewModel.loadBookings()
        advanceUntilIdle()

        // La reserva amb id=1 hauria de tenir el temps associat
        assertEquals(weather, viewModel.weatherMap.value[1L])
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AdminCourtsViewModelTest
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Tests per a [AdminCourtsViewModel].
 *
 * Comprova les operacions CRUD sobre pistes des del perfil d'administrador:
 * càrrega, creació, modificació i eliminació.
 *
 * @author Jesús Ramos
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AdminCourtsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var courtRepository: CourtRepository
    private lateinit var dataStore: DataStoreManager
    private lateinit var viewModel: AdminCourtsViewModel

    private val pistaTest = Court(1L, "Pista Admin", "Pàdel", "Barcelona", 18.0, 4)

    /**
     * Configura els mocks i el ViewModel abans de cada test.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        courtRepository = mock()
        dataStore = mock()
        viewModel = AdminCourtsViewModel(courtRepository, dataStore)
    }

    /**
     * Restaura el dispatcher original després de cada test.
     */
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Comprova que l'estat inicial és Idle.
     */
    @Test
    fun `estat inicial es Idle`() {
        assertTrue(viewModel.uiState.value is AdminCourtsUiState.Idle)
    }

    /**
     * Comprova que loadCourts() transiciona a Success amb la llista de pistes.
     */
    @Test
    fun `loadCourts transiciona a Success amb la llista de pistes`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("admin_token"))
        whenever(courtRepository.getCourts("admin_token")).thenReturn(listOf(pistaTest))

        viewModel.loadCourts()
        advanceUntilIdle()

        val state = viewModel.uiState.value as AdminCourtsUiState.Success
        assertEquals(1, state.courts.size)
        assertEquals("Pista Admin", state.courts[0].name)
    }

    /**
     * Comprova que createCourt() crida el repositori i recarrega la llista.
     */
    @Test
    fun `createCourt crida el repositori i recarrega la llista`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("admin_token"))
        whenever(courtRepository.getCourts("admin_token")).thenReturn(listOf(pistaTest))

        viewModel.createCourt("Nova Pista", "Tennis", 22.0, "Madrid")
        advanceUntilIdle()

        verify(courtRepository).createCourt("admin_token", "Nova Pista", "Tennis", 22.0, "Madrid")
        verify(courtRepository).getCourts("admin_token")
    }

    /**
     * Comprova que updateCourt() crida el repositori i recarrega la llista.
     */
    @Test
    fun `updateCourt crida el repositori i recarrega la llista`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("admin_token"))
        whenever(courtRepository.getCourts("admin_token")).thenReturn(listOf(pistaTest))

        viewModel.updateCourt(1L, "Pista Modificada", "Pàdel", 20.0, "Barcelona")
        advanceUntilIdle()

        verify(courtRepository).updateCourt("admin_token", 1L, "Pista Modificada", "Pàdel", 20.0, "Barcelona")
    }

    /**
     * Comprova que deleteCourt() crida el repositori i recarrega la llista.
     */
    @Test
    fun `deleteCourt crida el repositori i recarrega la llista`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("admin_token"))
        whenever(courtRepository.getCourts("admin_token")).thenReturn(emptyList())

        viewModel.deleteCourt(1L)
        advanceUntilIdle()

        verify(courtRepository).deleteCourt("admin_token", 1L)
        verify(courtRepository).getCourts("admin_token")
    }

    /**
     * Comprova que loadCourts() transiciona a Error quan no hi ha token.
     */
    @Test
    fun `loadCourts transiciona a Error quan no hi ha token`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf(null))

        viewModel.loadCourts()
        advanceUntilIdle()

        val state = viewModel.uiState.value as AdminCourtsUiState.Error
        assertEquals("Sense token", state.message)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// CourtDetailViewModelTest
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Tests per a [CourtDetailViewModel].
 *
 * Comprova la càrrega d'una pista per ID, la creació de reserves
 * i la càrrega del temps associat a la pista.
 *
 * @author Jesús Ramos
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CourtDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var courtRepository: CourtRepository
    private lateinit var dataStore: DataStoreManager
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var viewModel: CourtDetailViewModel

    private val pistaTest = Court(5L, "Pista Central", "Pàdel", "Barcelona", 15.0, 4)
    private val bookingTest = Booking(10L, "2025-06-15T10:00", 60, "jesus", "Pista Central", "Barcelona")

    /**
     * Configura els mocks i el ViewModel abans de cada test.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        courtRepository = mock()
        dataStore = mock()
        weatherRepository = mock()
        viewModel = CourtDetailViewModel(courtRepository, dataStore, weatherRepository)
    }

    /**
     * Restaura el dispatcher original després de cada test.
     */
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Comprova que loadCourt() transiciona a Success quan la pista existeix.
     */
    @Test
    fun `loadCourt transiciona a Success quan la pista existeix`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(courtRepository.getCourts("token_valid")).thenReturn(listOf(pistaTest))

        viewModel.loadCourt(5L)
        advanceUntilIdle()

        val state = viewModel.uiState.value as CourtDetailUiState.Success
        assertEquals("Pista Central", state.court.name)
        assertEquals(5L, state.court.id)
    }

    /**
     * Comprova que loadCourt() transiciona a Error quan la pista no existeix.
     *
     * Es busca un ID que no està a la llista → "Pista no trobada".
     */
    @Test
    fun `loadCourt transiciona a Error quan la pista no existeix`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(courtRepository.getCourts("token_valid")).thenReturn(listOf(pistaTest))

        // ID 999 no existeix a la llista
        viewModel.loadCourt(999L)
        advanceUntilIdle()

        val state = viewModel.uiState.value as CourtDetailUiState.Error
        assertEquals("Pista no trobada", state.message)
    }

    /**
     * Comprova que loadCourt() transiciona a Error quan no hi ha token.
     */
    @Test
    fun `loadCourt transiciona a Error quan no hi ha token`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf(null))

        viewModel.loadCourt(5L)
        advanceUntilIdle()

        val state = viewModel.uiState.value as CourtDetailUiState.Error
        assertEquals("Sense token", state.message)
    }

    /**
     * Comprova que createBooking() transiciona a BookingSuccess quan la reserva té èxit.
     *
     * Primer cal que l'estat sigui Success (pista carregada) per poder reservar.
     */
    @Test
    fun `createBooking transiciona a BookingSuccess quan te exit`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(courtRepository.getCourts("token_valid")).thenReturn(listOf(pistaTest))
        whenever(
            courtRepository.createBooking("token_valid", 5L, "2025-06-15T10:00", 60)
        ).thenReturn(bookingTest)

        // Primer carreguem la pista (l'estat ha de ser Success per poder reservar)
        viewModel.loadCourt(5L)
        advanceUntilIdle()

        // Ara fem la reserva
        viewModel.createBooking(5L, "2025-06-15T10:00", 60)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is CourtDetailUiState.BookingSuccess)
    }

    /**
     * Comprova que createBooking() transiciona a BookingError quan falla la reserva.
     *
     * Per exemple, si ja hi ha una reserva en aquell horari.
     */
    @Test
    fun `createBooking transiciona a BookingError quan falla`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(courtRepository.getCourts("token_valid")).thenReturn(listOf(pistaTest))
        whenever(
            courtRepository.createBooking("token_valid", 5L, "2025-06-15T10:00", 60)
        ).thenAnswer { throw Exception("Horari no disponible") }

        viewModel.loadCourt(5L)
        advanceUntilIdle()

        viewModel.createBooking(5L, "2025-06-15T10:00", 60)
        advanceUntilIdle()

        val state = viewModel.uiState.value as CourtDetailUiState.BookingError
        assertEquals("Horari no disponible", state.message)
    }

    /**
     * Comprova que loadWeather() actualitza el StateFlow de temps.
     */
    @Test
    fun `loadWeather actualitza el StateFlow weather`() = runTest {
        val weather = DayWeather("2025-06-15", 30.0, 20.0, 5, 10.0, 0)
        whenever(weatherRepository.getWeatherForCityAndDate("Barcelona", "2025-06-15"))
            .thenReturn(weather)

        viewModel.loadWeather("Barcelona", "2025-06-15")
        advanceUntilIdle()

        assertEquals(weather, viewModel.weather.value)
    }

    /**
     * Comprova que loadWeather() deixa el temps a null si el repositori falla.
     *
     * Un error de temps no ha d'afectar la UI principal de la pista.
     */
    @Test
    fun `loadWeather deixa weather a null si el repositori falla`() = runTest {
        whenever(weatherRepository.getWeatherForCityAndDate("Barcelona", "2025-06-15"))
            .thenAnswer { throw Exception("Error API temps") }

        viewModel.loadWeather("Barcelona", "2025-06-15")
        advanceUntilIdle()

        assertEquals(null, viewModel.weather.value)
    }
}