package com.example.sportspot.ui.session

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sportspot.data.local.DataStoreManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


/**
 * ViewModel encarregat de gestionar l'estat de la sessió de l'usuari.
 *
 * Aquest ViewModel llegeix el token guardat al DataStore i l'exposa com un
 * estat observable perquè la MainActivity pugui decidir si l'usuari ja està
 * autenticat o ha de veure la pantalla de login.
 *
 * @author Jesús Ramos
 *
 * @property dataStore Instància del DataStoreManager per accedir al token guardat.
 */
class SessionViewModel(private val dataStore: DataStoreManager) : ViewModel() {

    /**
     * Flux observable que conté el token actual de sessió.
     *
     * - Si hi ha un token guardat, emetrà el seu valor.
     * - Si no hi ha token, emetrà `null`.
     *
     */
    val token = dataStore.tokenFlow
        .stateIn(viewModelScope,SharingStarted.Eagerly,null)

    val role = dataStore.roleFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun saveSession(token: String, role: String) {
        viewModelScope.launch {
            dataStore.saveToken(token)
            dataStore.saveRole(role)
        }
    }


    /**
     * Factory per crear instàncies del SessionViewModel.
     *
     * Necessària perquè el ViewModel rep un paràmetre (DataStoreManager),
     * i per tant no es pot crear amb el constructor per defecte.
     *
     * @author Jesús Ramos
     *
     * @param context Context necessari per inicialitzar el DataStoreManager.
     */
    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SessionViewModel(DataStoreManager(context)) as T
                }
            }
    }
}
