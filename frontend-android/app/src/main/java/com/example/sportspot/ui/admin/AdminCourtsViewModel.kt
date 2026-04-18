package com.example.sportspot.ui.admin

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
 * ViewModel per la pantalla de gestió de pistes de l'administrador.
 *
 * Gestiona la càrrega, creació, modificació i eliminació de pistes
 * i exposa l'estat de la UI per ser observat per la vista.
 *
 * @author Jesús Ramos
 *
 * @param repository Repositori que gestiona les operacions sobre pistes.
 * @param dataStore Manager per accedir al token emmagatzemat localment.
 */
class AdminCourtsViewModel(
    private val repository: CourtRepository,
    private val dataStore: DataStoreManager
) : ViewModel() {

    /**
     * Estat intern mutable de la UI.
     *
     * @author Jesús Ramos
     */
    private val _uiState = MutableStateFlow<AdminCourtsUiState>(AdminCourtsUiState.Idle)

    /**
     * Estat públic de la UI que la vista observa.
     *
     * @author Jesús Ramos
     */
    val uiState: StateFlow<AdminCourtsUiState> = _uiState

    /**
     * Carrega la llista de totes les pistes disponibles.
     *
     * @author Jesús Ramos
     */
    fun loadCourts() {
        viewModelScope.launch {
            _uiState.value = AdminCourtsUiState.Loading
            try {
                val token = dataStore.tokenFlow.first() ?: throw Exception("Sense token")
                val courts = repository.getCourts(token)
                _uiState.value = AdminCourtsUiState.Success(courts)
            } catch (e: Exception) {
                _uiState.value = AdminCourtsUiState.Error(e.message ?: "Error desconegut")
            }
        }
    }

    /**
     * Crea una nova pista i recarrega la llista.
     *
     * @author Jesús Ramos
     *
     * @param name Nom de la nova pista.
     * @param type Tipus d'esport.
     * @param price Preu per hora en euros.
     * @param location Localització de la pista.
     */
    fun createCourt(name: String, type: String, price: Double, location: String) {
        viewModelScope.launch {
            try {
                val token = dataStore.tokenFlow.first() ?: throw Exception("Sense token")
                repository.createCourt(token, name, type, price, location)
                loadCourts()
            } catch (e: Exception) {
                _uiState.value = AdminCourtsUiState.Error(e.message ?: "Error en crear la pista")
            }
        }
    }

    /**
     * Modifica una pista existent i recarrega la llista.
     *
     * @author Jesús Ramos
     *
     * @param id Identificador de la pista a modificar.
     * @param name Nou nom de la pista.
     * @param type Nou tipus d'esport.
     * @param price Nou preu per hora en euros.
     * @param location Nova localització de la pista.
     */
    fun updateCourt(id: Long, name: String, type: String, price: Double, location: String) {
        viewModelScope.launch {
            try {
                val token = dataStore.tokenFlow.first() ?: throw Exception("Sense token")
                repository.updateCourt(token, id, name, type, price, location)
                loadCourts()
            } catch (e: Exception) {
                _uiState.value = AdminCourtsUiState.Error(e.message ?: "Error en modificar la pista")
            }
        }
    }

    /**
     * Elimina una pista i recarrega la llista.
     *
     * @author Jesús Ramos
     *
     * @param id Identificador de la pista a eliminar.
     */
    fun deleteCourt(id: Long) {
        viewModelScope.launch {
            try {
                val token = dataStore.tokenFlow.first() ?: throw Exception("Sense token")
                repository.deleteCourt(token, id)
                loadCourts()
            } catch (e: Exception) {
                _uiState.value = AdminCourtsUiState.Error(e.message ?: "Error en eliminar la pista")
            }
        }
    }

    companion object {
        /**
         * Factory per crear una instància de [AdminCourtsViewModel] amb les dependències.
         *
         * @author Jesús Ramos
         *
         * @param context Context necessari per crear el DataStore.
         * @return ViewModelProvider.Factory que crea AdminCourtsViewModel.
         */
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val dataStore = DataStoreManager(context.applicationContext)
                    val repository = CourtRepository()
                    return AdminCourtsViewModel(repository, dataStore) as T
                }
            }
        }
    }
}

/**
 * Estats possibles de la UI de gestió de pistes.
 *
 * - Idle: estat inicial.
 * - Loading: s'estan carregant les pistes.
 * - Success: operació correcta; conté la llista de pistes.
 * - Error: hi ha hagut un error; conté un missatge.
 *
 * @author Jesús Ramos
 */
sealed class AdminCourtsUiState {
    object Idle : AdminCourtsUiState()
    object Loading : AdminCourtsUiState()
    data class Success(val courts: List<Court>) : AdminCourtsUiState()
    data class Error(val message: String) : AdminCourtsUiState()
}