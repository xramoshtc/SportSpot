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
import com.example.sportspot.data.repository.WeatherRepository
import com.example.sportspot.domain.model.DayWeather


/**
 * ViewModel per la pantalla de detall d'una pista.
 *
 * Gestiona la càrrega de la pista seleccionada i la creació
 * de reserves per a l'usuari autenticat.
 *
 * @author Jesús Ramos
 *
 * @param repository Repositori que gestiona pistes i reserves.
 * @param dataStore Manager per accedir al token emmagatzemat localment.
 * @param weatherRepository Repositori que gestiona les consultes meteorològiques.
 */
class CourtDetailViewModel(
    private val repository: CourtRepository,
    private val dataStore: DataStoreManager,
    private val weatherRepository: WeatherRepository


) : ViewModel() {

    /**
     * Estat intern mutable de la UI.
     *
     * @author Jesús Ramos
     */
    private val _uiState = MutableStateFlow<CourtDetailUiState>(CourtDetailUiState.Idle)

    /**
     * Estat públic de la UI que la vista observa.
     *
     * @author Jesús Ramos
     */
    val uiState: StateFlow<CourtDetailUiState> = _uiState

    /**
     * Conjunt intern mutable de franges horàries ocupades.
     *
     * @author Jesús Ramos
     */
    private val _occupiedSlots = MutableStateFlow<Set<String>>(emptySet())

    /**
     * Conjunt públic de franges horàries ocupades per a la data seleccionada.
     *
     * @author Jesús Ramos
     */
    val occupiedSlots: StateFlow<Set<String>> = _occupiedSlots

    /**
     * Previsió meteorològica interna mutable per al dia seleccionat.
     *
     * @author Jesús Ramos
     */
    private val _weather = MutableStateFlow<DayWeather?>(null)

    /**
     * Previsió meteorològica pública per al dia seleccionat.
     *
     * @author Jesús Ramos
     */
    val weather: StateFlow<DayWeather?> = _weather

    /**
     * Indicador intern de si s'estan carregant les franges ocupades.
     *
     * @author Jesús Ramos
     */
    private val _loadingSlots = MutableStateFlow(false)

    /**
     * Indicador públic de si s'estan carregant les franges ocupades.
     *
     * @author Jesús Ramos
     */
    val loadingSlots: StateFlow<Boolean> = _loadingSlots

    /**
     * Carrega les dades d'una pista concreta de la llista de pistes disponibles.
     *
     * Obté totes les pistes i filtra per ID. Actualitza l'estat a
     * [CourtDetailUiState.Success] si la troba o [CourtDetailUiState.Error] si no.
     *
     * @author Jesús Ramos
     *
     * @param courtId Identificador de la pista a carregar.
     */
    fun loadCourt(courtId: Long) {
        viewModelScope.launch {
            _uiState.value = CourtDetailUiState.Loading
            try {
                val token = dataStore.tokenFlow.first() ?: throw Exception("Sense token")
                val courts = repository.getCourts(token)
                val court = courts.find { it.id == courtId }
                    ?: throw Exception("Pista no trobada")
                _uiState.value = CourtDetailUiState.Success(court)
            } catch (e: Exception) {
                _uiState.value = CourtDetailUiState.Error(e.message ?: "Error desconegut")
            }
        }
    }

    /**
     * Crea una reserva per a la pista i franja horària seleccionades.
     *
     * Llegeix el token del DataStore i fa la crida al repositori.
     * Actualitza l'estat a [CourtDetailUiState.BookingSuccess] si tot va bé
     * o a [CourtDetailUiState.BookingError] si hi ha un conflicte o error.
     *
     * @author Jesús Ramos
     *
     * @param courtId ID de la pista a reservar.
     * @param dateTime Data i hora en format ISO (YYYY-MM-DDTHH:MM).
     * @param durationHours Durada de la reserva en hores.
     */
    fun createBooking(courtId: Long, dateTime: String, durationHours: Int) {
        val currentCourt = when (val s = _uiState.value) {
            is CourtDetailUiState.Success -> s.court
            is CourtDetailUiState.BookingError -> s.court
            else -> return
        }
        viewModelScope.launch {
            try {
                val token = dataStore.tokenFlow.first() ?: throw Exception("Sense token")
                repository.createBooking(token, courtId, dateTime, durationHours)
                _uiState.value = CourtDetailUiState.BookingSuccess(currentCourt)
            } catch (e: Exception) {
                // Llegim el missatge exacte que retorna el backend
                val errorBody = (e as? retrofit2.HttpException)
                    ?.response()?.errorBody()?.string()
                val errorMessage = if (!errorBody.isNullOrBlank()) errorBody
                else e.message ?: "Error en la reserva"
                _uiState.value = CourtDetailUiState.BookingError(
                    court = currentCourt,
                    message = errorMessage
                )
            }
        }
    }

    /**
     * Carrega les franges horàries ocupades d'una pista per a un dia concret.
     *
     * @author Jesús Ramos
     *
     * @param courtId ID de la pista.
     * @param date Data en format ISO (YYYY-MM-DD).
     */
    fun loadOccupiedSlots(courtId: Long, date: String) {
        viewModelScope.launch {
            _loadingSlots.value = true
            _occupiedSlots.value = emptySet()
            try {
                val token = dataStore.tokenFlow.first() ?: return@launch
                val slots = repository.getOccupiedSlots(token, courtId, date)
                _occupiedSlots.value = slots
            } catch (e: Exception) {
                android.util.Log.e("OCCUPIED_SLOTS", "Error: ${e.message}", e)
                _occupiedSlots.value = emptySet()
            } finally {
                _loadingSlots.value = false
            }
        }
    }

    /**
     * Carrega la previsió meteorològica per a una ciutat i data concretes.
     *
     * S'executa cada vegada que l'usuari canvia el dia seleccionat.
     * No actualitza l'estat principal de la UI, té el seu propi StateFlow.
     *
     * @author Jesús Ramos
     *
     * @param city Nom de la ciutat de la pista.
     * @param date Data seleccionada en format ISO (YYYY-MM-DD).
     */
    fun loadWeather(city: String, date: String) {
        viewModelScope.launch {
            _weather.value = null
            try {
                _weather.value = weatherRepository.getWeatherForCityAndDate(city, date)
            } catch (e: Exception) {
                _weather.value = null
            }
        }
    }

    companion object {
        /**
         * Factory per crear una instància de [CourtDetailViewModel] amb les dependències.
         *
         * @author Jesús Ramos
         *
         * @param context Context necessari per crear el DataStore.
         * @return ViewModelProvider.Factory que crea CourtDetailViewModel.
         */
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val dataStore = DataStoreManager(context.applicationContext)
                    val repository = CourtRepository()
                    val weatherRepository = WeatherRepository()
                    return CourtDetailViewModel(repository, dataStore, weatherRepository) as T
                }
            }
        }
    }
}

/**
 * Estats possibles de la UI de detall de pista.
 *
 * - Idle: estat inicial.
 * - Loading: s'estan carregant les dades.
 * - Success: dades carregades correctament.
 * - BookingSuccess: reserva creada correctament.
 * - BookingError: error en crear la reserva.
 * - Error: error general de càrrega.
 *
 * @author Jesús Ramos
 */
sealed class CourtDetailUiState {
    object Idle : CourtDetailUiState()
    object Loading : CourtDetailUiState()
    data class Success(val court: Court) : CourtDetailUiState()
    data class BookingSuccess(val court: Court) : CourtDetailUiState()
    data class BookingError(val court: Court, val message: String) : CourtDetailUiState()
    data class Error(val message: String) : CourtDetailUiState()
}