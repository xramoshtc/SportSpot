package com.example.sportspot.data.repository

import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.remote.AuthApi
import com.example.sportspot.data.remote.LoginResponse
import com.example.sportspot.data.remote.LogoutRequest
import com.example.sportspot.data.remote.LogoutResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

/**
 * Tests unitaris per AuthRepository.
 *
 * S'utilitza MockK per simular l'API i el DataStore sense necessitat
 * de xarxa ni de context Android.
 *
 */
class AuthRepositoryTest {

    // Mocks de les dependències
    private lateinit var mockApi: AuthApi          // aquí guardo el mock de l'API
    private lateinit var mockDataStore: DataStoreManager // mock del DataStore (persistència local)
    private lateinit var repository: AuthRepository // instancia que estem provant

    @Before
    fun setUp() {
        mockApi = mockk() // creem el mock de l'API
        mockDataStore = mockk(relaxed = true) // relaxed = no cal definir cada stub, útil per tests
        // Injectem el mock de l'API manualment (vegeu nota al final)
        repository = AuthRepository(mockDataStore) // creem el repositori amb el DataStore mockejat
        // Substituïm el camp privat 'api' via reflexió per injectar el mock
        // (això es fa perquè el constructor no rep l'API directament)
        val field = AuthRepository::class.java.getDeclaredField("api")
        field.isAccessible = true
        field.set(repository, mockApi)
    }

    // -------------------------------------------------------------------------
    // login()
    // -------------------------------------------------------------------------

    @Test
    fun `login amb credencials correctes retorna User i guarda el token`() = runTest {
        // Donat
        // Creem una resposta falsa que simula l'API quan el login és correcte
        val fakeResponse = LoginResponse(
            success = true,
            message = "OK",
            resultCode = 200,
            sessionToken = "tok123",
            role = "ADMIN",
            permissions = ""
        )
        // Li diem al mock que quan es cridi login(any()) retorni la fakeResponse
        coEvery { mockApi.login(any()) } returns fakeResponse

        // Quan
        // Cridem al mètode que volem provar
        val user = repository.login("jesus", "1234")

        // Llavors
        // Comprovem que el rol s'ha normalitzat a minúscules i que el token és correcte
        assertEquals("admin", user.role)          // el rol s'ha normalitzat a minúscules
        assertEquals("tok123", user.token)
        // Verifiquem que s'han guardat les dades al DataStore (efecte secundari)
        coVerify { mockDataStore.saveToken("tok123") }
        coVerify { mockDataStore.saveRole("admin") }
    }

    @Test
    fun `login amb success false llença excepció`() = runTest {
        // Donat
        // Simulem que l'API diu que no s'ha pogut autenticar (credencials incorrectes)
        val fakeResponse = LoginResponse(
            success = false,
            message = "Credencials incorrectes",
            resultCode = 401,
            sessionToken = "",
            role = "",
            permissions = ""
        )
        coEvery { mockApi.login(any()) } returns fakeResponse

        // Llavors — s'espera excepció
        // assertThrows no accepta lambdas suspend directament, per això fem runBlocking dins
        assertThrows(Exception::class.java) {
            kotlinx.coroutines.runBlocking { repository.login("jesus", "wrong") }
        }
    }

    @Test
    fun `login amb resultCode diferent de 200 llença excepció`() = runTest {
        // Donat
        // Aquí l'API marca success = true però el resultCode és 500 (error intern)
        // El repositori ha de tractar-ho com a error i llençar excepció
        val fakeResponse = LoginResponse(
            success = true,
            message = "Error intern",
            resultCode = 500,
            sessionToken = "",
            role = "",
            permissions = ""
        )
        coEvery { mockApi.login(any()) } returns fakeResponse

        // Llavors — s'espera excepció
        assertThrows(Exception::class.java) {
            kotlinx.coroutines.runBlocking { repository.login("jesus", "1234") }
        }
    }

    @Test
    fun `login normalitza el rol a minúscules`() = runTest {
        // Donat
        // Simulem que l'API retorna el rol en majúscules
        coEvery { mockApi.login(any()) } returns LoginResponse(
            success = true, message = "OK", resultCode = 200,
            sessionToken = "t", role = "CLIENT", permissions = ""
        )

        // Quan
        val user = repository.login("u", "p")
        // Llavors
        // Comprovem que el repositori normalitza el rol a minúscules (client)
        assertEquals("client", user.role)
    }

    // -------------------------------------------------------------------------
    // logout()
    // -------------------------------------------------------------------------

    @Test
    fun `logout crida l'API i esborra el token i el rol locals`() = runTest {
        // Donat
        // Simulem una resposta d'logout correcta
        coEvery { mockApi.logout(any()) } returns LogoutResponse(
            success = true, message = "OK", resultCode = 200,
            sessionToken = "", role = ""
        )

        // Quan
        // Cridem al mètode logout del repositori
        repository.logout("tok123")

        // Llavors
        // Verifiquem que s'ha cridat l'API amb la petició correcta
        coVerify { mockApi.logout(LogoutRequest("tok123")) }
        // I que s'han esborrat les dades locals (token i rol)
        coVerify { mockDataStore.clearToken() }
        coVerify { mockDataStore.clearRole() }
    }
}
