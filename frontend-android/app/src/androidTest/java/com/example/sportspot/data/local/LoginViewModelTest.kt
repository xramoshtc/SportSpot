package com.example.sportspot.ui.login

import com.example.sportspot.domain.model.User
import com.example.sportspot.domain.usecase.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    // Dispatcher de proves per a corrutines
    private val dispatcher = UnconfinedTestDispatcher()

    // Mocks i instància del ViewModel
    private lateinit var mockUseCase: LoginUseCase
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        // Posem el dispatcher de test com a Main perquè les corrutines del ViewModel funcionin
        Dispatchers.setMain(dispatcher)
        // Creem el mock del cas d'ús i el ViewModel amb aquest mock
        mockUseCase = mockk()
        viewModel = LoginViewModel(mockUseCase)
    }

    @After
    fun tearDown() {
        // Restablim el Main per evitar interferències amb altres tests
        Dispatchers.resetMain()
    }

    // -------------------------------------------------------------------------
    // Estat inicial
    // -------------------------------------------------------------------------

    @Test
    fun `estat inicial es Idle`() {
        // Comprovem que en crear el ViewModel l'estat inicial és Idle
        assertTrue(viewModel.uiState.value is LoginUiState.Idle)
    }

    // -------------------------------------------------------------------------
    // login() — cas d'èxit
    // -------------------------------------------------------------------------

    @Test
    fun `login correcte actualitza l'estat a Success`() = runTest {
        // Donat: el useCase retorna un usuari vàlid
        val fakeUser = User(role = "admin", token = "tok123")
        coEvery { mockUseCase(any(), any()) } returns fakeUser

        // Quan: cridem el mètode login del ViewModel
        viewModel.login("jesus", "1234")

        // Llavors: l'estat ha de ser Success i contenir l'usuari retornat
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Success)
        assertEquals(fakeUser, (state as LoginUiState.Success).user)
    }

    @Test
    fun `login correcte amb rol client retorna Success amb usuari client`() = runTest {
        // Donat: el useCase retorna un usuari amb rol client
        val fakeUser = User(role = "client", token = "tok999")
        coEvery { mockUseCase(any(), any()) } returns fakeUser

        // Quan
        viewModel.login("client_user", "pass")

        // Llavors: comprovem que el rol és client
        val state = viewModel.uiState.value as LoginUiState.Success
        assertEquals("client", state.user.role)
    }

    // -------------------------------------------------------------------------
    // login() — cas d'error
    // -------------------------------------------------------------------------

    @Test
    fun `login amb error actualitza l'estat a Error`() = runTest {
        // Donat: el useCase llença una excepció amb missatge
        coEvery { mockUseCase(any(), any()) } throws Exception("Credencials incorrectes")

        // Quan
        viewModel.login("jesus", "wrong")

        // Llavors: l'estat ha de ser Error amb el missatge de l'excepció
        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
        assertEquals("Credencials incorrectes", (state as LoginUiState.Error).message)
    }

    @Test
    fun `login amb excepcio sense missatge mostra Error desconegut`() = runTest {
        // Donat: el useCase llença una excepció sense missatge
        coEvery { mockUseCase(any(), any()) } throws Exception()

        // Quan
        viewModel.login("u", "p")

        // Llavors: l'estat Error ha de tenir el missatge per defecte "Error desconegut"
        val state = viewModel.uiState.value as LoginUiState.Error
        assertEquals("Error desconegut", state.message)
    }

    // -------------------------------------------------------------------------
    // Estat final després del login
    // -------------------------------------------------------------------------

    @Test
    fun `despres d'un login correcte l'estat no es Idle ni Loading`() = runTest {
        // Donat: login correcte
        coEvery { mockUseCase(any(), any()) } returns User(role = "admin", token = "t")

        // Quan
        viewModel.login("u", "p")

        // Llavors: l'estat final no ha de ser Idle ni Loading
        val state = viewModel.uiState.value
        assertTrue(state !is LoginUiState.Idle)
        assertTrue(state !is LoginUiState.Loading)
    }

    @Test
    fun `despres d'un login erroni l'estat no es Idle ni Loading`() = runTest {
        // Donat: el useCase llença una excepció
        coEvery { mockUseCase(any(), any()) } throws Exception("error")

        // Quan
        viewModel.login("u", "p")

        // Llavors: després d'un error tampoc ha de quedar en Idle ni Loading
        val state = viewModel.uiState.value
        assertTrue(state !is LoginUiState.Idle)
        assertTrue(state !is LoginUiState.Loading)
    }
}
