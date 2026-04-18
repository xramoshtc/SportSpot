package com.example.sportspot.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sportspot.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel per la pantalla de registre del client.
 *
 * Gestiona la lògica de creació d'un nou usuari de tipus client.
 *
 * @author Jesús Ramos
 *
 */
class RegisterViewModel(
    private val repository: UserRepository
) : ViewModel() {

    /**
     * Estat intern mutable de la UI.
     */
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)

    /**
     * Estat públic de la UI que la vista observa.
     */
    val uiState: StateFlow<RegisterUiState> = _uiState

    /**
     * Executa el registre d'un nou usuari client.
     *
     *
     * @author Jesús Ramos
     *
     * @param name Nom d'usuari.
     * @param password Contrasenya.
     * @param email Correu electrònic.
     */
    fun register(name: String, password: String, email: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            try {
                repository.registerUser(name, password, email)
                _uiState.value = RegisterUiState.Success
            } catch (e: Exception) {
                _uiState.value = RegisterUiState.Error(e.message ?: "Error desconegut")
            }
        }
    }

    companion object {
        fun provideFactory(): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val repository = UserRepository()
                    return RegisterViewModel(repository) as T
                }
            }
        }
    }
}

/**
 * Estats possibles de la UI de registre.
 *
 * - Idle: estat inicial.
 * - Loading: s'està processant el registre.
 * - Success: registre correcte.
 * - Error: hi ha hagut un error; conté un missatge.
 */
sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

