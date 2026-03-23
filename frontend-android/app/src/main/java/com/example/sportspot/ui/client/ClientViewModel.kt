package com.example.sportspot.ui.client

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel per la pantalla del client.
 *
 * Gestiona l'estat del token i les accions relacionades amb la sessió.
 *
 * @author Jesús Ramos
 *
 * @param repository Repositori d'autenticació per fer logout.
 * @param dataStore Manager per accedir al token emmagatzemat localment.
 */
class ClientViewModel(
    private val repository: AuthRepository,
    dataStore: DataStoreManager
) : ViewModel() {

    /**
     * Token exposat com a StateFlow.
     *
     * @author Jesús Ramos
     *
     * Converteix el Flow del DataStore en un StateFlow observable per la UI.
     * El valor inicial és una cadena buida quan no hi ha token.
     */
    val token = dataStore.tokenFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    companion object {
        /**
         * Factory per crear una instància de ClientViewModel amb les dependències.
         *
         * @author Jesús Ramos
         *
         * @param context Context necessari per crear el DataStore.
         * @return ViewModelProvider.Factory que crea ClientViewModel.
         */
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {

                    val dataStore = DataStoreManager(context)
                    val repository = AuthRepository(dataStore)

                    return ClientViewModel(
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
     * intenta fer la crida de logout al repositori i retorna true si tot va bé.
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