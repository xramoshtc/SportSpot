package com.example.sportspot.ui.events

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.repository.CourtRepository
import com.example.sportspot.data.repository.EventRepository
import com.example.sportspot.domain.model.Court
import com.example.sportspot.domain.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel per la pantalla d'esdeveniments.
 *
 * Gestiona la càrrega del llistat d'esdeveniments, la creació de nous
 * esdeveniments, la inscripció i l'abandonament. Exposa l'estat de la UI
 * com a [StateFlow] per ser observat per la vista.
 *
 *
 * @param repository Repositori que gestiona les operacions sobre esdeveniments.
 * @param courtRepository Repositori que gestiona les operacions sobre pistes.
 * @param dataStore Manager per accedir al token i al nom d'usuari emmagatzemats localment.
 *
 * @author Jesús Ramos
 */
class EventViewModel(
    private val repository: EventRepository,
    private val courtRepository: CourtRepository,
    private val dataStore: DataStoreManager
) : ViewModel() {

    /**
     * Estat intern mutable de la UI principal (llistat d'esdeveniments).
     *
     * @author Jesús Ramos
     */
    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Idle)

    /**
     * Estat públic de la UI que la vista observa.
     *
     * Pot ser [EventUiState.Idle], [EventUiState.Loading], [EventUiState.Success]
     * o [EventUiState.Error].
     *
     * @author Jesús Ramos
     */
    val uiState: StateFlow<EventUiState> = _uiState

    /**
     * Estat intern de les accions puntuals sobre esdeveniments.
     *
     * Separat de [_uiState] per no substituir el llistat mentre es processa
     * una acció. Permet mostrar feedback (snackbar, diàleg) sense perdre
     * la llista que ja estava carregada.
     *
     * @author Jesús Ramos
     */
    private val _actionState = MutableStateFlow<EventActionState>(EventActionState.Idle)

    /**
     * Estat públic de les accions puntuals (join, leave, create).
     *
     * La vista l'observa per mostrar snackbars i tancar diàlegs un cop
     * l'acció ha finalitzat.
     *
     * @author Jesús Ramos
     */
    val actionState: StateFlow<EventActionState> = _actionState

    /**
     * Estat intern amb el nom de l'usuari autenticat.
     *
     * S'usa per comparar amb la llista de [Event.participantNames] i
     * determinar si l'usuari ja és participant d'un esdeveniment concret,
     * de manera que la UI pugui desactivar el botó "Apuntar-me" i mostrar
     * "Apuntat" en el seu lloc.
     *
     * @author Jesús Ramos
     */
    private val _currentUsername = MutableStateFlow<String?>(null)

    /**
     * Nom públic de l'usuari actual, observable per la vista.
     * Pot ser null si el nom encara no s'ha carregat o si no hi ha sessió.
     *
     * @author Jesús Ramos
     */
    val currentUsername: StateFlow<String?> = _currentUsername

    /**
     * Estat intern amb la llista de pistes disponibles.
     *
     * S'utilitza per omplir el desplegable de selecció de pista al diàleg
     * de creació d'un nou esdeveniment, evitant que l'usuari hagi d'introduir
     * l'ID de la pista manualment.
     *
     * @author Jesús Ramos
     */
    private val _courts = MutableStateFlow<List<Court>>(emptyList())

    /**
     * Llista pública de pistes, observable per la vista.
     *
     * @author Jesús Ramos
     */
    val courts: StateFlow<List<Court>> = _courts

    // Inicialització
    init {
        // Carreguem el nom d'usuari en crear el ViewModel perquè estigui
        // disponible abans que la UI intenti mostrar les targetes d'events.
        loadUsername()
    }

    /**
     * Carrega el nom de l'usuari autenticat des del DataStore.
     *
     * @author Jesús Ramos
     */
    private fun loadUsername() {
        viewModelScope.launch {
            try {
                _currentUsername.value = dataStore.usernameFlow.first()
            } catch (e: Exception) {
                // Si falla, la UI mostrarà el botó "Apuntar-me" per defecte
                _currentUsername.value = null
            }
        }
    }

    /**
     * Carrega la llista de pistes disponibles des del servidor.
     *
     * @author Jesús Ramos
     */
    private fun loadCourts() {
        viewModelScope.launch {
            try {
                val token = dataStore.tokenFlow.first() ?: return@launch
                _courts.value = courtRepository.getCourts(token)
            } catch (e: Exception) {
                // Si falla la càrrega de pistes, el desplegable quedarà buit.
                // No canviem l'estat principal per no interferir amb el llistat d'events.
                _courts.value = emptyList()
            }
        }
    }

    /**
     * Carrega la llista de tots els esdeveniments actius des del servidor.
     * També inicia la càrrega de pistes en paral·lel per tenir-les disponibles
     * quan l'usuari vulgui crear un nou esdeveniment.
     *
     * @author Jesús Ramos
     */
    fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = EventUiState.Loading
            try {
                val token = dataStore.tokenFlow.first()
                    ?: throw Exception("Sense token")
                val events = repository.getEvents(token)
                _uiState.value = EventUiState.Success(events)
            } catch (e: Exception) {
                _uiState.value = EventUiState.Error(e.message ?: "Error desconegut")
            }
        }
        //Carreguem les pistes en paral·lel per tenir-les llestes
        //quan l'usuari obri el diàleg de creació
        loadCourts()
    }

    /**
     * Crea un nou esdeveniment al servidor i recarrega el llistat.
     *
     * @param title Títol de l'esdeveniment.
     * @param courtId Identificador de la pista on es realitzarà.
     * @param dateTime Data i hora en format ISO-8601 (ex: "2026-05-10T10:00:00").
     *
     * @author Jesús Ramos
     */
    fun createEvent(title: String, courtId: Long, dateTime: String) {
        viewModelScope.launch {
            _actionState.value = EventActionState.Loading
            try {
                val token = dataStore.tokenFlow.first()
                    ?: throw Exception("Sense token")
                repository.createEvent(token, title, courtId, dateTime)
                _actionState.value = EventActionState.CreateSuccess
                //Recarreguem el llistat per mostrar el nou esdeveniment
                loadEvents()
            } catch (e: Exception) {
                _actionState.value = EventActionState.Error(e.message ?: "Error desconegut")
            }
        }
    }

    /**
     * Inscriu l'usuari autenticat a un esdeveniment.
     *
     * @param eventId Identificador de l'esdeveniment al qual s'apunta.
     *
     * @author Jesús Ramos
     */
    fun joinEvent(eventId: Long) {
        viewModelScope.launch {
            _actionState.value = EventActionState.Loading
            try {
                val token = dataStore.tokenFlow.first()
                    ?: throw Exception("Sense token")
                repository.joinEvent(token, eventId)
                _actionState.value = EventActionState.JoinSuccess
                // Recarreguem per veure el participant nou al llistat
                loadEvents()
            } catch (e: Exception) {
                _actionState.value = EventActionState.Error(e.message ?: "Error desconegut")
            }
        }
    }

    /**
     * Abandona o elimina un esdeveniment segons el rol de l'usuari.
     *
     * @param eventId Identificador de l'esdeveniment a abandonar o eliminar.
     *
     * @author Jesús Ramos
     */
    fun deleteOrLeaveEvent(eventId: Long) {
        viewModelScope.launch {
            _actionState.value = EventActionState.Loading
            try {
                val token = dataStore.tokenFlow.first()
                    ?: throw Exception("Sense token")
                repository.deleteOrLeaveEvent(token, eventId)
                _actionState.value = EventActionState.LeaveSuccess
                // Recarreguem el llistat per reflectir el canvi
                loadEvents()
            } catch (e: Exception) {
                _actionState.value = EventActionState.Error(e.message ?: "Error desconegut")
            }
        }
    }

    /**
     * Reinicia l'estat de l'acció a [EventActionState.Idle].
     *
     * @author Jesús Ramos
     */
    fun resetActionState() {
        _actionState.value = EventActionState.Idle
    }

    // Factory
    companion object {
        /**
         * Factory per crear una instància de [EventViewModel] amb totes les dependències.
         *
         * @param context Context necessari per crear el DataStore.
         * @return [ViewModelProvider.Factory] que crea [EventViewModel].
         *
         * @author Jesús Ramos
         */
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val dataStore = DataStoreManager(context.applicationContext)
                    val eventRepository = EventRepository()
                    val courtRepository = CourtRepository()
                    return EventViewModel(eventRepository, courtRepository, dataStore) as T
                }
            }
        }
    }
}

/**
 * Estats possibles de la UI principal d'esdeveniments (llistat).
 *
 * - [Idle]: estat inicial, abans de fer cap càrrega.
 * - [Loading]: s'està carregant la llista del servidor.
 * - [Success]: llista carregada correctament; conté la llista d'[Event].
 * - [Error]: error durant la càrrega; conté el missatge d'error.
 *
 * @author Jesús Ramos
 */
sealed class EventUiState {
    object Idle    : EventUiState()
    object Loading : EventUiState()
    data class Success(val events: List<Event>) : EventUiState()
    data class Error(val message: String)       : EventUiState()
}

/**
 * Estats possibles de les accions puntuals sobre esdeveniments.
 *
 * - [Idle]: cap acció en curs.
 * - [Loading]: acció en procés (mostra un indicador sobre la pantalla).
 * - [JoinSuccess]: inscripció realitzada correctament.
 * - [LeaveSuccess]: abandonament o eliminació correcta.
 * - [CreateSuccess]: nou esdeveniment creat correctament; la UI ha de tancar el diàleg.
 * - [Error]: l'acció ha fallat; conté el missatge d'error del servidor.
 *
 * @author Jesús Ramos
 */
sealed class EventActionState {
    object Idle          : EventActionState()
    object Loading       : EventActionState()
    object JoinSuccess   : EventActionState()
    object LeaveSuccess  : EventActionState()
    object CreateSuccess : EventActionState()
    data class Error(val message: String) : EventActionState()
}