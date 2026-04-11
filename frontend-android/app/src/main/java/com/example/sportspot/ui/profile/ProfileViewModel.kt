package com.example.sportspot.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.repository.UserRepository
import com.example.sportspot.domain.model.UserProfile
import com.example.sportspot.domain.usecase.UpdateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * TEA3 - ViewModel per la pantalla de perfil del client.
 *
 * Gestiona la càrrega del perfil de l'usuari autenticat i les accions
 * de modificació.
 *
 * @author Jesús Ramos
 *
 * @param updateUserUseCase Cas d'ús per modificar les dades de l'usuari.
 * @param dataStore Manager per accedir al token emmagatzemat localment.
 */
class ProfileViewModel(
    private val updateUserUseCase: UpdateUserUseCase,
    private val repository: UserRepository,
    private val dataStore: DataStoreManager
) : ViewModel() {

    /**
     * Estat intern mutable de la UI.
     */
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)

    /**
     * Estat públic de la UI que la vista observa.
     */
    val uiState: StateFlow<ProfileUiState> = _uiState

    /**
     * TEA3 - Carrega el perfil de l'usuari autenticat en iniciar la pantalla.
     *
     * Llegeix el token del DataStore i fa la crida al repositori.
     * Actualitza l'estat a [ProfileUiState.Success] si tot va bé,
     * o a [ProfileUiState.Error] si hi ha un problema.
     *
     * @author Jesús Ramos
     *
     */
    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val token = dataStore.tokenFlow.first() ?: throw Exception("Sense token")
                val profile = repository.getMyProfile(token)
                _uiState.value = ProfileUiState.Success(profile)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Error desconegut")
            }
        }
    }

    /**
     * TEA3 - Modifica les dades de l'usuari autenticat.
     *
     * Llegeix el token del DataStore, construeix la petició i crida
     * el cas d'ús. Actualitza l'estat segons el resultat.
     *
     * @author Jesús Ramos
     *
     * @param currentName Nom actual de l'usuari (s'utilitza a la URL).
     * @param newName Nou nom d'usuari.
     * @param newPassword Nova contrasenya.
     * @param newEmail Nou correu electrònic.
     */
    fun updateUser(
        currentName: String,
        newName: String,
        newPassword: String,
        newEmail: String
    ) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val token = dataStore.tokenFlow.first() ?: throw Exception("Sense token")
                val updatedProfile = updateUserUseCase(
                    token = token,
                    name = currentName,
                    newName = newName,
                    newPassword = newPassword,
                    newEmail = newEmail
                )
                _uiState.value = ProfileUiState.Success(updatedProfile)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Error desconegut")
            }
        }
    }

    companion object {
        /**
         * TEA3 - Factory per crear una instància de [ProfileViewModel] amb les dependències.
         *
         * @author Jesús Ramos
         *
         * @param context Context necessari per crear el DataStore.
         * @return ViewModelProvider.Factory que crea ProfileViewModel.
         */
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val dataStore = DataStoreManager(context)
                    val repository = UserRepository()
                    val useCase = UpdateUserUseCase(repository)
                    return ProfileViewModel(
                        updateUserUseCase = useCase,
                        repository = repository,
                        dataStore = dataStore
                    ) as T
                }
            }
        }
    }
}

/**
 * TEA3 - Estats possibles de la UI de perfil.
 *
 * - Idle: estat inicial, abans de carregar res.
 * - Loading: s'està carregant o modificant el perfil.
 * - Success: operació correcta; conté el perfil actualitzat.
 * - Error: hi ha hagut un error; conté un missatge.
 */
sealed class ProfileUiState {
    object Idle : ProfileUiState()
    object Loading : ProfileUiState()
    data class Success(val profile: UserProfile) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}