package com.example.sportspot.data.local

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests instrumentats per DataStoreManager.
 *
 */
@RunWith(AndroidJUnit4::class)
class DataStoreManagerTest {

    // Instància del manager que provarem
    private lateinit var manager: DataStoreManager

    @Before
    fun setUp() {
        // Agafem el context real que proporciona el framework de tests d'Android
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        // Creem el DataStoreManager amb el context real
        manager = DataStoreManager(context)
    }

    // -------------------------------------------------------------------------
    // Token
    // -------------------------------------------------------------------------

    @Test
    fun saveToken_i_tokenFlow_retorna_el_valor_guardat() = runTest {
        // Guardem un token i comprovem que el flow retorna el mateix valor
        manager.saveToken("tok123")
        val result = manager.tokenFlow.first()
        assertEquals("tok123", result)
    }

    @Test
    fun clearToken_fa_que_tokenFlow_retorni_null() = runTest {
        // Guardem un token, el netegem i comprovem que ara és null
        manager.saveToken("tok123")
        manager.clearToken()
        val result = manager.tokenFlow.first()
        assertNull(result)
    }

    @Test
    fun tokenFlow_inicial_es_null_si_no_hi_ha_res_guardat() = runTest {
        // Ens assegurem que si no hi ha res guardat, el tokenFlow és null
        manager.clearToken() // neteja per si hi ha dades de tests anteriors
        val result = manager.tokenFlow.first()
        assertNull(result)
    }

    // -------------------------------------------------------------------------
    // Rol
    // -------------------------------------------------------------------------

    @Test
    fun saveRole_i_roleFlow_retorna_el_valor_guardat() = runTest {
        // Guardem un rol i comprovem que el flow retorna el valor guardat
        manager.saveRole("admin")
        val result = manager.roleFlow.first()
        assertEquals("admin", result)
    }

    @Test
    fun clearRole_fa_que_roleFlow_retorni_null() = runTest {
        // Guardem un rol, el netegem i comprovem que ara és null
        manager.saveRole("client")
        manager.clearRole()
        val result = manager.roleFlow.first()
        assertNull(result)
    }

    // -------------------------------------------------------------------------
    // Token + Rol conjuntament
    // -------------------------------------------------------------------------

    @Test
    fun guardar_token_i_rol_i_esborrar_ambdos_deixa_tot_null() = runTest {
        // Guardem token i rol, després els esborrem i comprovem que tot queda null
        manager.saveToken("tok123")
        manager.saveRole("admin")

        manager.clearToken()
        manager.clearRole()

        assertNull(manager.tokenFlow.first())
        assertNull(manager.roleFlow.first())
    }

    @Test
    fun sobreescriure_token_retorna_el_nou_valor() = runTest {
        // Guardem un token, el sobreescrivim i comprovem que el flow té el darrer valor
        manager.saveToken("primer")
        manager.saveToken("segon")
        val result = manager.tokenFlow.first()
        assertEquals("segon", result)
    }
}
