package com.example.sportspot.ui.courts

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.repository.CourtRepository
import com.example.sportspot.domain.model.Court
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel per la pantalla de llistat de pistes.
 *
 * Gestiona la càrrega de pistes des del repositori i exposa
 * l'estat de la UI per ser observat per la vista.
 *
 * @author Jesús Ramos
 *
 * @param repository Repositori que gestiona les operacions sobre pistes.
 * @param dataStore Manager per accedir al token emmagatzemat localment.
 */
class CourtsViewModel(
    private val repository: CourtRepository,
    private val dataStore: DataStoreManager
) : ViewModel() {

    /**
     * Estat intern mutable de la UI.
     *
     * @author Jesús Ramos
     */
    private val _uiState = MutableStateFlow<CourtsUiState>(CourtsUiState.Idle)

    /**
     * Estat públic de la UI que la vista observa.
     *
     * @author Jesús Ramos
     */
    val uiState: StateFlow<CourtsUiState> = _uiState

    /**
     * Carrega la llista de pistes disponibles des del servidor.
     *
     * Llegeix el token del DataStore i fa la crida al repositori.
     * Actualitza l'estat a [CourtsUiState.Success] si tot va bé,
     * o a [CourtsUiState.Error] si hi ha un problema.
     *
     * @author Jesús Ramos
     */
    fun loadCourts() {
        viewModelScope.launch {
            _uiState.value = CourtsUiState.Loading
            try {
                val token = dataStore.tokenFlow.first() ?: throw Exception("Sense token")
                val courts = repository.getCourts(token)
                _uiState.value = CourtsUiState.Success(courts)
            } catch (e: Exception) {
                _uiState.value = CourtsUiState.Error(e.message ?: "Error desconegut")
            }
        }
    }

    companion object {
        /**
         * Factory per crear una instància de [CourtsViewModel] amb les dependències.
         *
         * @author Jesús Ramos
         *
         * @param context Context necessari per crear el DataStore.
         * @return ViewModelProvider.Factory que crea CourtsViewModel.
         */
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val dataStore = DataStoreManager(context.applicationContext)
                    val repository = CourtRepository()
                    return CourtsViewModel(repository, dataStore) as T
                }
            }
        }
    }
}

/**
 * Estats possibles de la UI de llistat de pistes.
 *
 * - Idle: estat inicial, abans de carregar res.
 * - Loading: s'estan carregant les pistes.
 * - Success: càrrega correcta; conté la llista de pistes.
 * - Error: hi ha hagut un error; conté un missatge.
 *
 * @author Jesús Ramos
 */
sealed class CourtsUiState {
    object Idle : CourtsUiState()
    object Loading : CourtsUiState()
    data class Success(val courts: List<Court>) : CourtsUiState()
    data class Error(val message: String) : CourtsUiState()
}