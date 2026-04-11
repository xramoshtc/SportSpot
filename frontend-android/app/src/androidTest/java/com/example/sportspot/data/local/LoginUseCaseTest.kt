package com.example.sportspot.domain.usecase

import com.example.sportspot.data.repository.AuthRepository
import com.example.sportspot.domain.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Tests unitaris per LoginUseCase.
 *
 */
class LoginUseCaseTest {

    // Mocks i instància del cas d'ús
    private lateinit var mockRepo: AuthRepository // mock del repositori d'autenticació
    private lateinit var useCase: LoginUseCase   // cas d'ús que provem

    @Before
    fun setUp() {
        mockRepo = mockk()               // creem el mock
        useCase = LoginUseCase(mockRepo) // injectem el mock al cas d'ús
    }

    @Test
    fun `invoke delega al repositori i retorna l'usuari`() = runTest {
        // Donat
        // Preparem l'usuari esperat que retornarà el repositori
        val expectedUser = User(role = "admin", token = "tok123")
        coEvery { mockRepo.login("jesus", "1234") } returns expectedUser

        // Quan
        // Cridem el cas d'ús com si fos una funció (invoke)
        val result = useCase("jesus", "1234")

        // Llavors
        // Comprovem que el resultat és el mateix que ha retornat el mock
        assertEquals(expectedUser, result)
        // I verifiquem que el repositori s'ha cridat exactament una vegada
        coVerify(exactly = 1) { mockRepo.login("jesus", "1234") }
    }

    @Test(expected = Exception::class)
    fun `invoke propaga l'excepció del repositori`() = runTest {
        // Donat
        // Fem que el repositori llenci una excepció per simular credencials incorrectes
        coEvery { mockRepo.login(any(), any()) } throws Exception("Credencials incorrectes")

        // Quan / Llavors
        // Esperem que el useCase propagui la mateixa excepció (no la captura)
        useCase("jesus", "wrong")
    }

    @Test
    fun `invoke amb rol client retorna rol correcte`() = runTest {
        // Donat
        // Simulem que el repositori retorna un usuari amb rol "client"
        val expectedUser = User(role = "client", token = "tok999")
        coEvery { mockRepo.login(any(), any()) } returns expectedUser

        // Quan
        val result = useCase("client_user", "pass")

        // Llavors
        // Comprovem que el rol que arriba des del repositori es manté igual
        assertEquals("client", result.role)
    }
}
