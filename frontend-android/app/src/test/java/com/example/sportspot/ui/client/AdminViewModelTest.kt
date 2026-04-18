package com.example.sportspot.ui.admin

import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class martaAdminViewModelTest {

    // Dispatcher de proves per a corrutines (no fem servir el Main real)
    private val dispatcher = UnconfinedTestDispatcher()

    // Mocks que utilitzarem als tests
    private lateinit var mockRepo: AuthRepository
    private lateinit var mockDataStore: DataStoreManager

    @Before
    fun setUp() {
        // Posem el dispatcher de test com a Main perquè les corrutines funcionin bé
        Dispatchers.setMain(dispatcher)
        // Mocks relaxats
        mockRepo = mockk(relaxed = true)
        mockDataStore = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        // Restablim el Main
        Dispatchers.resetMain()
    }

    /**
     * Crea el ViewModel i substitueix el camp 'token' (StateFlow) directament
     * via reflexió, evitant el problema de stateIn() que no emet sense col·lector.
     *
     * Nota: això és un truc per als tests, a l'app real no tocaríem camps privats així.
     */
    private fun buildViewModel(tokenValue: String?): AdminViewModel {
        // El tokenFlow ha d'existir perquè el constructor no peti
        // Li diem al mock que retorni un flow amb el valor que volem provar
        coEvery { mockDataStore.tokenFlow } returns flowOf(tokenValue)

        // Creem el ViewModel amb els mocks
        val vm = AdminViewModel(mockRepo, mockDataStore)

        // Substituïm el StateFlow intern per un que ja té el valor correcte
        // Això evita haver d'arrencar un collector per rebre l'emissió inicial
        val fakeTokenFlow = MutableStateFlow(tokenValue)
        val field = AdminViewModel::class.java.getDeclaredField("token")
        field.isAccessible = true
        field.set(vm, fakeTokenFlow)

        return vm
    }

    @Test
    fun `logout amb token valid crida el repositori i retorna true`() = runTest {
        // Preparo el ViewModel amb un token vàlid
        val vm = buildViewModel("tok123")

        // Cridem logout i guardem el resultat
        val result = vm.logout()

        // Ha de tornar true i s'ha de cridar el repositori amb el token
        assertTrue(result)
        coVerify { mockRepo.logout("tok123") }
    }

    @Test
    fun `logout amb token buit retorna false sense cridar l'API`() = runTest {
        // Token buit (string buit)
        val vm = buildViewModel("")

        val result = vm.logout()

        // Ha de tornar false i no s'ha de cridar l'API
        assertFalse(result)
        coVerify(exactly = 0) { mockRepo.logout(any()) }
    }

    @Test
    fun `logout amb token null retorna false`() = runTest {
        // Token null (no hi ha token)
        val vm = buildViewModel(null)

        val result = vm.logout()

        // Ha de tornar false i tampoc cridar l'API
        assertFalse(result)
        coVerify(exactly = 0) { mockRepo.logout(any()) }
    }

    @Test
    fun `logout quan l'API llença excepció retorna false`() = runTest {
        // Tenim token però el repositori llença una excepció (simulem error de xarxa)
        val vm = buildViewModel("tok123")
        coEvery { mockRepo.logout(any()) } throws Exception("Xarxa no disponible")

        val result = vm.logout()

        // Si l'API falla, el ViewModel ha de retornar false (no petar el test)
        assertFalse(result)
    }
}
