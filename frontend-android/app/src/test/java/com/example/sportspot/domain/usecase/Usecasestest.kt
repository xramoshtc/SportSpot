package com.example.sportspot.domain.usecase

import com.example.sportspot.data.repository.AuthRepository
import com.example.sportspot.data.repository.UserRepository
import com.example.sportspot.domain.model.User
import com.example.sportspot.domain.model.UserProfile
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Tests unitaris per als casos d'ús del domini de SportSpot.
 *
 * Cada cas d'ús es prova de forma aïllada usant mocks del repositori
 * corresponent. Això permet verificar la lògica del cas d'ús sense
 * dependre de la xarxa ni del servidor real.
 *
 * Dependències necessàries al build.gradle (app):
 *   testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
 *   testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
 *   testImplementation("junit:junit:4.13.2")
 *
 * @author Jesús Ramos
 */

// ─────────────────────────────────────────────────────────────────────────────
// LoginUseCaseTest
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Tests per a [LoginUseCase].
 *
 * Comprova que el cas d'ús delega correctament al repositori i que
 * propaga els resultats (èxit i error) a la capa superior.
 *
 * @author Jesús Ramos
 */
class LoginUseCaseTest {

    // Mock del repositori d'autenticació
    private lateinit var authRepository: AuthRepository

    // Instància del cas d'ús que es prova
    private lateinit var loginUseCase: LoginUseCase

    /**
     * Inicialitza els mocks i el cas d'ús abans de cada test.
     */
    @Before
    fun setUp() {
        authRepository = mock()
        loginUseCase = LoginUseCase(authRepository)
    }

    /**
     * Comprova que invoke() retorna l'usuari quan el repositori té èxit.
     *
     * El repositori retorna un User correcte → el cas d'ús el retorna tal qual.
     */
    @Test
    fun `invoke retorna User quan el repositori te exit`() = runTest {
        // Donat un repositori que retorna un usuari vàlid
        val expectedUser = User(role = "user", token = "token_abc")
        whenever(authRepository.login("jesus", "1234")).thenReturn(expectedUser)

        // Quan s'invoca el cas d'ús
        val result = loginUseCase("jesus", "1234")

        // Aleshores el resultat és l'usuari esperat
        assertEquals(expectedUser, result)
    }

    /**
     * Comprova que invoke() propaga l'excepció quan el repositori falla.
     *
     * El repositori llença Exception → el cas d'ús la propaga sense modificar.
     *
     * NOTA: Per a suspend functions no es pot usar thenThrow() directament
     * (Mockito no admet checked exceptions en aquest context). En lloc d'això
     * s'usa thenAnswer { throw ... } que funciona correctament amb coroutines.
     *
     * @author Jesús Ramos
     */
    @Test
    fun `invoke propaga l'excepcio quan el repositori falla`() = runTest {
        // Donat un repositori que llença una excepció (via thenAnswer per a suspend fun)
        whenever(authRepository.login("jesus", "incorrecte"))
            .thenAnswer { throw Exception("Credencials incorrectes") }

        // Quan s'invoca el cas d'ús, s'espera una excepció
        try {
            loginUseCase("jesus", "incorrecte")
            fail("S'hauria d'haver llençat una Exception")
        } catch (e: Exception) {
            assertEquals("Credencials incorrectes", e.message)
        }
    }

    /**
     * Comprova que invoke() crida el repositori amb els paràmetres correctes.
     *
     * Verifica que el cas d'ús no modifica ni filtra les credencials.
     */
    @Test
    fun `invoke crida el repositori amb els parametres correctes`() = runTest {
        // Donat un repositori configurat
        whenever(authRepository.login("jesus", "pass123"))
            .thenReturn(User("user", "token"))

        // Quan s'invoca
        loginUseCase("jesus", "pass123")

        // Aleshores el repositori s'ha cridat exactament amb els mateixos paràmetres
        verify(authRepository).login("jesus", "pass123")
    }

    /**
     * Comprova que el Login funciona correctament per a un usuari admin.
     */
    @Test
    fun `invoke retorna usuari admin correctament`() = runTest {
        val adminUser = User(role = "admin", token = "admin_token_xyz")
        whenever(authRepository.login("adminuser", "adminpass")).thenReturn(adminUser)

        val result = loginUseCase("adminuser", "adminpass")

        assertEquals("admin", result.role)
        assertEquals("admin_token_xyz", result.token)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// DeleteUserUseCaseTest
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Tests per a [DeleteUserUseCase].
 *
 * Comprova que el cas d'ús delega correctament al repositori d'usuari
 * i que gestiona els errors adequadament.
 *
 * @author Jesús Ramos
 */
class DeleteUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var deleteUserUseCase: DeleteUserUseCase

    /**
     * Inicialitza els mocks abans de cada test.
     */
    @Before
    fun setUp() {
        userRepository = mock()
        deleteUserUseCase = DeleteUserUseCase(userRepository)
    }

    /**
     * Comprova que invoke() crida deleteUser al repositori amb token i nom correctes.
     *
     * La funció no retorna res (Unit), però es verifica que el repositori s'ha cridat.
     */
    @Test
    fun `invoke crida el repositori amb token i nom`() = runTest {
        // Donat un repositori que no llença cap excepció (èxit)
        // (Per defecte Mockito no fa res per a suspend fun que retorna Unit)

        // Quan s'invoca el cas d'ús
        deleteUserUseCase(token = "token_abc", name = "jesusramos")

        // Aleshores el repositori s'ha cridat amb els paràmetres correctes
        verify(userRepository).deleteUser(token = "token_abc", name = "jesusramos")
    }

    /**
     * Comprova que invoke() propaga l'excepció si el repositori falla.
     *
     * Per exemple, si el token no és vàlid, el servidor retorna error
     * i el repositori llença una excepció.
     */
    @Test
    fun `invoke propaga l'excepcio si el repositori falla`() = runTest {
        // thenAnswer en lloc de thenThrow per compatibilitat amb suspend fun
        whenever(userRepository.deleteUser(token = "token_invalid", name = "jesus"))
            .thenAnswer { throw Exception("Token no vàlid") }

        try {
            deleteUserUseCase(token = "token_invalid", name = "jesus")
            fail("S'hauria d'haver llençat una Exception")
        } catch (e: Exception) {
            assertEquals("Token no vàlid", e.message)
        }
    }

    /**
     * Comprova que invoke() completa sense errors amb credencials vàlides.
     *
     * No s'espera cap excepció.
     */
    @Test
    fun `invoke completa sense errors amb credencials valides`() = runTest {
        // No llencem cap excepció → el mock retorna Unit per defecte

        // No hauria de llençar cap excepció
        deleteUserUseCase(token = "token_valid", name = "jesusramos")

        verify(userRepository).deleteUser("token_valid", "jesusramos")
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// UpdateUserUseCaseTest
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Tests per a [UpdateUserUseCase].
 *
 * Comprova que el cas d'ús delega correctament al repositori, que retorna
 * el [UserProfile] actualitzat i que gestiona els errors.
 *
 * @author Jesús Ramos
 */
class UpdateUserUseCaseTest {

    private lateinit var userRepository: UserRepository
    private lateinit var updateUserUseCase: UpdateUserUseCase

    /**
     * Inicialitza els mocks abans de cada test.
     */
    @Before
    fun setUp() {
        userRepository = mock()
        updateUserUseCase = UpdateUserUseCase(userRepository)
    }

    /**
     * Comprova que invoke() retorna el [UserProfile] actualitzat quan el repositori té èxit.
     */
    @Test
    fun `invoke retorna UserProfile actualitzat quan el repositori te exit`() = runTest {
        // Donat un perfil actualitzat que retorna el repositori
        val updatedProfile = UserProfile(
            id = 1L,
            name = "nounom",
            email = "nou@sportspot.cat",
            role = "user",
            active = true
        )
        whenever(
            userRepository.updateUser(
                token = "token_abc",
                name = "vellnom",
                newName = "nounom",
                newPassword = "novacontrasenya",
                newEmail = "nou@sportspot.cat"
            )
        ).thenReturn(updatedProfile)

        // Quan s'invoca el cas d'ús
        val result = updateUserUseCase(
            token = "token_abc",
            name = "vellnom",
            newName = "nounom",
            newPassword = "novacontrasenya",
            newEmail = "nou@sportspot.cat"
        )

        // Aleshores el resultat és el perfil actualitzat
        assertEquals(updatedProfile, result)
        assertEquals("nounom", result.name)
        assertEquals("nou@sportspot.cat", result.email)
    }

    /**
     * Comprova que invoke() crida el repositori amb tots els paràmetres correctament.
     */
    @Test
    fun `invoke crida el repositori amb els parametres correctes`() = runTest {
        val profile = UserProfile(1L, "nounom", "nou@a.cat", "user", true)
        whenever(
            userRepository.updateUser("token", "vell", "nounom", "pass", "nou@a.cat")
        ).thenReturn(profile)

        updateUserUseCase("token", "vell", "nounom", "pass", "nou@a.cat")

        // Verificació que el repositori rep exactament els mateixos paràmetres
        verify(userRepository).updateUser(
            token = "token",
            name = "vell",
            newName = "nounom",
            newPassword = "pass",
            newEmail = "nou@a.cat"
        )
    }

    /**
     * Comprova que invoke() propaga l'excepció quan el repositori falla.
     *
     * Per exemple, si el nou nom ja existeix al servidor.
     */
    @Test
    fun `invoke propaga l'excepcio si el repositori falla`() = runTest {
        // thenAnswer en lloc de thenThrow per compatibilitat amb suspend fun
        whenever(
            userRepository.updateUser(
                token = "token",
                name = "jesus",
                newName = "duplicat",
                newPassword = "",
                newEmail = "jesus@a.cat"
            )
        ).thenAnswer { throw Exception("El nom d'usuari ja existeix") }

        try {
            updateUserUseCase("token", "jesus", "duplicat", "", "jesus@a.cat")
            fail("S'hauria d'haver llençat una Exception")
        } catch (e: Exception) {
            assertEquals("El nom d'usuari ja existeix", e.message)
        }
    }

    /**
     * Comprova que invoke() funciona correctament quan la contrasenya és buida.
     *
     * Quan la contrasenya és buida, el repositori no hauria de modificar-la.
     * Aquesta lògica es gestiona al repositori, però el cas d'ús ha de
     * passar el valor buit sense modificar-lo.
     */
    @Test
    fun `invoke passa la contrasenya buida al repositori sense modificar-la`() = runTest {
        val profile = UserProfile(1L, "jesus", "jesus@a.cat", "user", true)
        whenever(
            userRepository.updateUser("token", "jesus", "jesus", "", "jesus@a.cat")
        ).thenReturn(profile)

        updateUserUseCase("token", "jesus", "jesus", "", "jesus@a.cat")

        // La contrasenya buida s'ha de passar tal qual al repositori
        verify(userRepository).updateUser("token", "jesus", "jesus", "", "jesus@a.cat")
    }
}