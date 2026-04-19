package com.example.sportspot.ui.viewmodel

import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.repository.CourtRepository
import com.example.sportspot.data.repository.UserRepository
import com.example.sportspot.domain.model.Court
import com.example.sportspot.domain.model.User
import com.example.sportspot.domain.usecase.LoginUseCase
import com.example.sportspot.ui.courts.CourtsUiState
import com.example.sportspot.ui.courts.CourtsViewModel
import com.example.sportspot.ui.login.LoginUiState
import com.example.sportspot.ui.login.LoginViewModel
import com.example.sportspot.ui.register.RegisterUiState
import com.example.sportspot.ui.register.RegisterViewModel
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
import org.mockito.kotlin.whenever

/**
 * Tests unitaris per als ViewModels de login, registre i llistat de pistes.
 *
 * Cada ViewModel es prova de forma aïllada amb mocks dels seus repositoris
 * i casos d'ús. S'utilitza [StandardTestDispatcher] per controlar
 * l'execució de les coroutines durant els tests.
 *
 * Dependències addicionals necessàries (ja incloses de la Fase 1):
 *   testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
 *   testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
 *
 * @author Jesús Ramos
 */

// ─────────────────────────────────────────────────────────────────────────────
// LoginViewModelTest
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Tests per a [LoginViewModel].
 *
 * Comprova les transicions d'estat: Idle → Loading → Success / Error.
 * El cas d'ús es mockeja per no dependre de la xarxa ni del repositori real.
 *
 * @author Jesús Ramos
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    // Dispatcher de test que permet controlar manualment l'execució de coroutines
    private val testDispatcher = StandardTestDispatcher()

    // Mock del cas d'ús de login
    private lateinit var loginUseCase: LoginUseCase

    // ViewModel que es prova
    private lateinit var viewModel: LoginViewModel

    /**
     * Configura el dispatcher de test i inicialitza el ViewModel abans de cada test.
     */
    @Before
    fun setUp() {
        // Substituïm el dispatcher principal per el de test
        Dispatchers.setMain(testDispatcher)
        loginUseCase = mock()
        viewModel = LoginViewModel(loginUseCase)
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
     *
     * Quan es crea el ViewModel, no s'ha fet cap acció,
     * per tant l'estat ha de ser Idle.
     */
    @Test
    fun `estat inicial es Idle`() {
        assertTrue(viewModel.uiState.value is LoginUiState.Idle)
    }

    /**
     * Comprova que login() transiciona a Success quan el cas d'ús té èxit.
     *
     * El flux esperat és: Idle → Loading → Success.
     */
    @Test
    fun `login transiciona a Success quan el cas d'us te exit`() = runTest {
        // Donat un cas d'ús que retorna un usuari vàlid
        val user = User(role = "user", token = "token_abc")
        whenever(loginUseCase("jesus", "1234")).thenReturn(user)

        // Quan es crida login()
        viewModel.login("jesus", "1234")

        // Esperem que totes les coroutines acabin
        advanceUntilIdle()

        // Aleshores l'estat és Success amb l'usuari correcte
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Success)
        assertEquals(user, (state as LoginUiState.Success).user)
    }

    /**
     * Comprova que login() transiciona a Error quan el cas d'ús falla.
     *
     * El flux esperat és: Idle → Loading → Error.
     */
    @Test
    fun `login transiciona a Error quan el cas d'us falla`() = runTest {
        // Donat un cas d'ús que llença una excepció
        whenever(loginUseCase("jesus", "incorrecte"))
            .thenAnswer { throw Exception("Credencials incorrectes") }

        // Quan es crida login()
        viewModel.login("jesus", "incorrecte")
        advanceUntilIdle()

        // Aleshores l'estat és Error amb el missatge correcte
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
        assertEquals("Credencials incorrectes", (state as LoginUiState.Error).message)
    }



    /**
     * Comprova que un login d'admin retorna Success amb rol "admin".
     */
    @Test
    fun `login admin retorna Success amb rol admin`() = runTest {
        val adminUser = User(role = "admin", token = "admin_token")
        whenever(loginUseCase("admin", "adminpass")).thenReturn(adminUser)

        viewModel.login("admin", "adminpass")
        advanceUntilIdle()

        val state = viewModel.uiState.value as LoginUiState.Success
        assertEquals("admin", state.user.role)
    }

    /**
     * Comprova que l'Error conté "Error desconegut" quan l'excepció no té missatge.
     */
    @Test
    fun `login mostra Error desconegut si l'excepcio no te missatge`() = runTest {
        whenever(loginUseCase("jesus", "1234"))
            .thenAnswer { throw Exception() }

        viewModel.login("jesus", "1234")
        advanceUntilIdle()

        val state = viewModel.uiState.value as LoginUiState.Error
        assertEquals("Error desconegut", state.message)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// RegisterViewModelTest
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Tests per a [RegisterViewModel].
 *
 * Comprova les transicions d'estat del registre d'un nou usuari:
 * Idle → Loading → Success / Error.
 *
 * @author Jesús Ramos
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: RegisterViewModel

    /**
     * Configura el dispatcher i el ViewModel abans de cada test.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userRepository = mock()
        viewModel = RegisterViewModel(userRepository)
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
        assertTrue(viewModel.uiState.value is RegisterUiState.Idle)
    }

    /**
     * Comprova que register() transiciona a Success quan el repositori té èxit.
     */
    @Test
    fun `register transiciona a Success quan el repositori te exit`() = runTest {
        // El repositori no llença cap excepció (mock retorna Unit per defecte)
        viewModel.register("jesus", "1234", "jesus@sportspot.cat")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is RegisterUiState.Success)
    }

    /**
     * Comprova que register() transiciona a Error quan el repositori falla.
     *
     * Per exemple, si el nom d'usuari ja existeix al servidor.
     */
    @Test
    fun `register transiciona a Error quan el repositori falla`() = runTest {
        whenever(userRepository.registerUser("jesus", "1234", "jesus@sportspot.cat"))
            .thenAnswer { throw Exception("L'usuari ja existeix") }

        viewModel.register("jesus", "1234", "jesus@sportspot.cat")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is RegisterUiState.Error)
        assertEquals("L'usuari ja existeix", (state as RegisterUiState.Error).message)
    }



    /**
     * Comprova que l'Error conté "Error desconegut" si l'excepció no té missatge.
     */
    @Test
    fun `register mostra Error desconegut si l'excepcio no te missatge`() = runTest {
        whenever(userRepository.registerUser("jesus", "1234", "jesus@a.cat"))
            .thenAnswer { throw Exception() }

        viewModel.register("jesus", "1234", "jesus@a.cat")
        advanceUntilIdle()

        val state = viewModel.uiState.value as RegisterUiState.Error
        assertEquals("Error desconegut", state.message)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// CourtsViewModelTest
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Tests per a [CourtsViewModel].
 *
 * Comprova la càrrega de pistes amb token vàlid, el cas de token absent
 * i el cas d'error de xarxa.
 *
 * El [DataStoreManager] es mockeja per controlar el token retornat
 * sense necessitat de context d'Android.
 *
 * @author Jesús Ramos
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CourtsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var courtRepository: CourtRepository
    private lateinit var dataStore: DataStoreManager
    private lateinit var viewModel: CourtsViewModel

    /**
     * Configura el dispatcher, els mocks i el ViewModel abans de cada test.
     */
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        courtRepository = mock()
        dataStore = mock()
        viewModel = CourtsViewModel(courtRepository, dataStore)
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
        assertTrue(viewModel.uiState.value is CourtsUiState.Idle)
    }

    /**
     * Comprova que loadCourts() transiciona a Success amb la llista de pistes.
     *
     * El DataStore retorna un token vàlid i el repositori retorna dues pistes.
     */
    @Test
    fun `loadCourts transiciona a Success amb la llista de pistes`() = runTest {
        // Donat un token vàlid al DataStore
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))

        // I un repositori que retorna dues pistes
        val pistes = listOf(
            Court(1L, "Pista A", "Pàdel", "Barcelona", 15.0, 4),
            Court(2L, "Pista B", "Tennis", "Madrid", 20.0, 2)
        )
        whenever(courtRepository.getCourts("token_valid")).thenReturn(pistes)

        // Quan es carreguen les pistes
        viewModel.loadCourts()
        advanceUntilIdle()

        // Aleshores l'estat és Success amb les dues pistes
        val state = viewModel.uiState.value
        assertTrue(state is CourtsUiState.Success)
        assertEquals(2, (state as CourtsUiState.Success).courts.size)
        assertEquals("Pista A", state.courts[0].name)
    }

    /**
     * Comprova que loadCourts() transiciona a Error quan no hi ha token.
     *
     * El DataStore retorna null (cap token guardat).
     */
    @Test
    fun `loadCourts transiciona a Error quan no hi ha token`() = runTest {
        // Donat un DataStore sense token
        whenever(dataStore.tokenFlow).thenReturn(flowOf(null))

        viewModel.loadCourts()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is CourtsUiState.Error)
        assertEquals("Sense token", (state as CourtsUiState.Error).message)
    }

    /**
     * Comprova que loadCourts() transiciona a Error quan el repositori falla.
     *
     * Per exemple, error de xarxa o servidor caigut.
     */
    @Test
    fun `loadCourts transiciona a Error quan el repositori falla`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(courtRepository.getCourts("token_valid"))
            .thenAnswer { throw Exception("Error de xarxa") }

        viewModel.loadCourts()
        advanceUntilIdle()

        val state = viewModel.uiState.value as CourtsUiState.Error
        assertEquals("Error de xarxa", state.message)
    }

    /**
     * Comprova que loadCourts() retorna una llista buida quan no hi ha pistes.
     */
    @Test
    fun `loadCourts Success amb llista buida si no hi ha pistes`() = runTest {
        whenever(dataStore.tokenFlow).thenReturn(flowOf("token_valid"))
        whenever(courtRepository.getCourts("token_valid")).thenReturn(emptyList())

        viewModel.loadCourts()
        advanceUntilIdle()

        val state = viewModel.uiState.value as CourtsUiState.Success
        assertTrue(state.courts.isEmpty())
    }


}