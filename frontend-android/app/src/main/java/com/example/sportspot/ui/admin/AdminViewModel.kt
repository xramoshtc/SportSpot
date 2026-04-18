package com.example.sportspot.ui.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel per la pantalla d'administració.
 *
 * Gestiona l'estat del token i les accions relacionades amb la sessió.
 *
 * @author Jesús Ramos
 *
 * @param repository Repositori d'autenticació per fer logout.
 * @param dataStore Manager per accedir al token emmagatzemat localment.
 */
class AdminViewModel(
    private val repository: AuthRepository,
    dataStore: DataStoreManager
) : ViewModel() {

    /**
     * Token exposat com a StateFlow.
     *
     * S'utilitza `stateIn` per convertir el Flow del DataStore en un StateFlow
     * que es pot observar des de la UI. El valor inicial és una cadena buida.
     *
     * @author Jesús Ramos
     *
     */
    val token = dataStore.tokenFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    /**
     * Username exposat com a StateFlow.
     *
     * S'utilitza `stateIn` per convertir el Flow del DataStore en un StateFlow
     * que es pot observar des de la UI. El valor inicial és una cadena buida.
     *
     * @author Jesús Ramos
     *
     */
    val username = dataStore.usernameFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    companion object {
        /**
         * Factory per crear una instància d'AdminViewModel amb les dependències
         * necessàries (DataStore i Repositori).
         *
         * @author Jesús Ramos
         *
         * @param context Context de l'aplicació necessari per crear el DataStore.
         * @return ViewModelProvider.Factory que crea AdminViewModel.
         */
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {

                    val dataStore = DataStoreManager(context.applicationContext)
                    val repository = AuthRepository(dataStore)

                    return AdminViewModel(
                        repository = repository,
                        dataStore = dataStore
                    ) as T
                }
            }
        }
    }

    /**
     * Tanca la sessió de l'usuari.
     *
     * Llegeix el token actual; si està buit retorna false. Si hi ha token,
     * intenta fer la crida de logout al repositori i esborra el token local.
     *
     * @author Jesús Ramos
     *
     * @return true si el logout ha anat bé, false en cas d'error o si no hi ha token.
     */
    suspend fun logout(): Boolean {
        val currentToken = token.value

        if (currentToken.isNullOrBlank()) {
            return false
        }

        return try {
            repository.logout(currentToken)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}

