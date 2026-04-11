package com.example.sportspot.ui.client

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
class ClientViewModelTest {

    // Dispatcher de proves per a corrutines
    private val dispatcher = UnconfinedTestDispatcher()

    // Mocks que utilitzarem als tests
    private lateinit var mockRepo: AuthRepository
    private lateinit var mockDataStore: DataStoreManager

    @Before
    fun setUp() {
        // Posem el dispatcher de test com a Main perquè les corrutines funcionin bé
        Dispatchers.setMain(dispatcher)
        // Mocks relaxats per no haver d'estubear-ho tot
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
     */
    private fun buildViewModel(tokenValue: String?): ClientViewModel {
        // Li diem al mock que retorni un flow amb el valor que volem provar
        coEvery { mockDataStore.tokenFlow } returns flowOf(tokenValue)

        // Creem el ViewModel amb els mocks
        val vm = ClientViewModel(mockRepo, mockDataStore)

        // Substituïm el StateFlow intern per un que ja té el valor correcte
        // Això evita haver d'arrencar un collector per rebre l'emissió inicial
        val fakeTokenFlow = MutableStateFlow(tokenValue)
        val field = ClientViewModel::class.java.getDeclaredField("token")
        field.isAccessible = true
        field.set(vm, fakeTokenFlow)

        return vm
    }

    @Test
    fun `logout amb token valid retorna true`() = runTest {
        // Preparo el ViewModel amb un token vàlid
        val vm = buildViewModel("tok999")

        // Cridem logout i guardem el resultat
        val result = vm.logout()

        // Ha de tornar true i s'ha de cridar el repositori amb el token
        assertTrue(result)
        coVerify { mockRepo.logout("tok999") }
    }

    @Test
    fun `logout amb token buit retorna false`() = runTest {
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
    }

    @Test
    fun `logout amb error de xarxa retorna false`() = runTest {
        // Tenim token però el repositori llença una excepció (simulem timeout)
        val vm = buildViewModel("tok999")
        coEvery { mockRepo.logout(any()) } throws Exception("Timeout")

        val result = vm.logout()

        // Si l'API falla, el ViewModel ha de retornar false (no petar el test)
        assertFalse(result)
    }
}
