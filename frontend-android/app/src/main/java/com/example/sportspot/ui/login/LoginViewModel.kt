package com.example.sportspot.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.repository.AuthRepository
import com.example.sportspot.domain.model.User
import com.example.sportspot.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lògica de la pantalla de login.
 *
 * Encapsula l'ús del cas d'ús [LoginUseCase] i exposa un estat de la UI
 * que la vista pot observar per mostrar càrrega, errors o l'usuari autenticat.
 *
 * @author Jesús Ramos
 *
 * @param loginUseCase Cas d'ús que realitza l'autenticació.
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    /**
     * Estat intern mutable de la UI.
     *
     * S'utilitza per actualitzar l'estat des de les coroutines del ViewModel.
     *
     */
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)

    /**
     * Estat públic de la UI que la vista observa.
     *
     * No permet modificació des de fora del ViewModel.
     */
    val uiState: StateFlow<LoginUiState> = _uiState

    /**
     * Inicia el procés de login amb les credencials proporcionades.
     *
     * Actualitza [_uiState] a Loading mentre dura l'operació, a Success si
     * l'autenticació és correcta o a Error si hi ha una excepció.
     *
     * @author Jesús Ramos
     *
     * @param user Nom d'usuari o identificador.
     * @param password Contrasenya en text pla.
     */
    fun login(user: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val loggedUser = loginUseCase(user, password)
                _uiState.value = LoginUiState.Success(loggedUser)
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "Error desconegut")
            }
        }
    }

    companion object {
        /**
         * Factory per crear una instància de [LoginViewModel] amb les dependències
         * necessàries (DataStore i Repositori).
         *
         * Aquesta factory facilita la creació del ViewModel des dels composables.
         *
         * @author Jesús Ramos
         *
         * @param context Context necessari per crear el DataStore.
         * @return ViewModelProvider.Factory que crea LoginViewModel.
         */
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val dataStore = DataStoreManager(context)
                    val repo = AuthRepository(dataStore)
                    val useCase = LoginUseCase(repo)
                    return LoginViewModel(useCase) as T
                }
            }
        }
    }
}

/**
 * Estats possibles de la UI de login.
 *
 * - Idle: estat inicial.
 * - Loading: s'està intentant autenticar.
 * - Success: autenticació correcta; conté l'usuari.
 * - Error: hi ha hagut un error; conté un missatge.
 */
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val user: User) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}