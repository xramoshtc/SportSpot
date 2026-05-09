package com.example.sportspot.ui.bookings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.repository.CourtRepository
import com.example.sportspot.domain.model.Booking
import com.example.sportspot.domain.model.Court
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.example.sportspot.data.repository.WeatherRepository
import com.example.sportspot.domain.model.DayWeather

/**
 * ViewModel per la pantalla de les reserves de l'usuari.
 *
 * Gestiona la càrrega, modificació i cancel·lació de reserves
 * i exposa l'estat de la UI per ser observat per la vista.
 *
 * @author Jesús Ramos
 *
 * @param repository Repositori que gestiona les operacions sobre reserves.
 * @param dataStore Manager per accedir al token emmagatzemat localment.
 */
class MyBookingsViewModel(
    private val repository: CourtRepository,
    private val dataStore: DataStoreManager,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    /**
     * Estat intern mutable de la UI.
     *
     * @author Jesús Ramos
     */
    private val _uiState = MutableStateFlow<MyBookingsUiState>(MyBookingsUiState.Idle)

    /**
     * Estat públic de la UI que la vista observa.
     *
     * @author Jesús Ramos
     */
    val uiState: StateFlow<MyBookingsUiState> = _uiState

    private val _weatherMap = MutableStateFlow<Map<Long, DayWeather>>(emptyMap())

    /**
     * Mapa públic de previsió meteorològica per ID de reserva.
     *
     * @author Jesús Ramos
     */
    val weatherMap: StateFlow<Map<Long, DayWeather>> = _weatherMap

    /**
     * Llista interna mutable de pistes disponibles.
     *
     * @author Jesús Ramos
     */
    private val _courts = MutableStateFlow<List<Court>>(emptyList())

    /**
     * Resultat intern mutable de l'última acció sobre una reserva.
     *
     * @author Jesús Ramos
     */
    private val _actionResult = MutableStateFlow<String?>(null)

    /**
     * Resultat públic de l'última acció sobre una reserva.
     *
     * Conté "ok" si ha anat bé o "error:missatge" si ha fallat.
     *
     * @author Jesús Ramos
     */
    val actionResult: StateFlow<String?> = _actionResult

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
     * Llista de pistes disponibles, observable per la vista.
     *
     * S'utilitza per construir el mapa de tipus de pista i mostrar
     * la icona correcta a cada reserva.
     *
     * @author Jesús Ramos
     */
    val courts: StateFlow<List<Court>> = _courts

    /**
     * Carrega totes les reserves de l'usuari autenticat.
     *
     * Llegeix el token del DataStore i fa la crida al repositori.
     *
     * @author Jesús Ramos
     */
    fun loadBookings() {
        viewModelScope.launch {
            _uiState.value = MyBookingsUiState.Loading
            try {
                val token = dataStore.tokenFlow.first() ?: throw Exception("Sense token")
                val bookings = repository.getMyBookings(token)
                _uiState.value = MyBookingsUiState.Success(bookings)
                loadWeatherForBookings(bookings)
            } catch (e: Exception) {
                _uiState.value = MyBookingsUiState.Error(e.message ?: "Error desconegut")
            }
        }
        // Carreguem les pistes en paral·lel per tenir el mapa de tipus disponible
        loadCourts()
    }

    /**
     * Cancel·la una reserva pel seu ID i recarrega la llista.
     *
     * @author Jesús Ramos
     *
     * @param bookingId ID de la reserva a cancel·lar.
     */
    fun deleteBooking(bookingId: Long) {
        viewModelScope.launch {
            try {
                val token = dataStore.tokenFlow.first() ?: throw Exception("Sense token")
                repository.deleteBooking(token, bookingId)
                loadBookings()
            } catch (e: Exception) {
                _uiState.value = MyBookingsUiState.Error(e.message ?: "Error en cancel·lar")
            }
        }
    }

    /**
     * Modifica l'horari o durada d'una reserva existent i recarrega la llista.
     *
     * @author Jesús Ramos
     *
     * @param bookingId ID de la reserva a modificar.
     * @param newDateTime Nova data i hora en format ISO.
     * @param newDurationHours Nova durada en hores.
     */
    fun updateBooking(bookingId: Long, newDateTime: String, newDuration: Int) {
        viewModelScope.launch {
            try {
                val token = dataStore.tokenFlow.first() ?: throw Exception("Sense token")
                repository.updateBooking(token, bookingId, newDateTime, newDuration)
                _actionResult.value = "ok"
                loadBookings()
            } catch (e: Exception) {
                val errorBody = (e as? retrofit2.HttpException)
                    ?.response()?.errorBody()?.string()
                val errorMessage = if (!errorBody.isNullOrBlank()) errorBody
                else e.message ?: "Error en modificar"
                _actionResult.value = "error:$errorMessage"
            }
        }
    }

    /**
     * Reinicia el resultat de l'última acció sobre una reserva.
     *
     * @author Jesús Ramos
     */
    fun resetActionResult() {
        _actionResult.value = null
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
                _courts.value = repository.getCourts(token)
            } catch (e: Exception) {
                // Si falla la càrrega de pistes, el mapa de tipus quedarà buit
                // i es mostrarà la icona per defecte a cada reserva
            }
        }
    }

    /**
     * Obté les franges horàries ocupades d'una pista per a un dia concret.
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
                _occupiedSlots.value = emptySet()
            } finally {
                _loadingSlots.value = false
            }
        }
    }

    /**
     * Reinicia les franges ocupades quan es tanca el diàleg d'edició.
     *
     * @author Jesús Ramos
     */
    fun resetOccupiedSlots() {
        _occupiedSlots.value = emptySet()
        _loadingSlots.value = false
    }

    /**
     * Carrega la previsió meteorològica per a cada reserva de la llista.
     *
     * Cada reserva té una ciutat i una data, es fan les crides en paral·lel
     * i s'actualitza el mapa de temps amb els resultats obtinguts.
     *
     * @author Jesús Ramos
     *
     * @param bookings Llista de reserves per a les quals cal obtenir el temps.
     */
    private fun loadWeatherForBookings(bookings: List<Booking>) {
        viewModelScope.launch {
            val map = mutableMapOf<Long, DayWeather>()
            bookings.forEach { booking ->
                try {
                    val date = booking.dateTime.take(10)
                    val weather = weatherRepository.getWeatherForCityAndDate(
                        city = booking.location,
                        date = date
                    )
                    if (weather != null) map[booking.id] = weather
                } catch (e: Exception) {
                    // Si falla una reserva concreta, continuem amb les altres
                }
            }
            _weatherMap.value = map
        }
    }

    companion object {
        /**
         * Factory per crear una instància de [MyBookingsViewModel] amb les dependències.
         *
         * @author Jesús Ramos
         *
         * @param context Context necessari per crear el DataStore.
         * @return ViewModelProvider.Factory que crea MyBookingsViewModel.
         */
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val dataStore = DataStoreManager(context.applicationContext)
                    val repository = CourtRepository()
                    val weatherRepository = WeatherRepository()
                    return MyBookingsViewModel(repository, dataStore, weatherRepository) as T
                }
            }
        }
    }
}

/**
 * Estats possibles de la UI de les reserves de l'usuari.
 *
 * - Idle: estat inicial.
 * - Loading: s'estan carregant les reserves.
 * - Success: càrrega correcta; conté la llista de reserves.
 * - Error: hi ha hagut un error; conté un missatge.
 *
 * @author Jesús Ramos
 */
sealed class MyBookingsUiState {
    object Idle : MyBookingsUiState()
    object Loading : MyBookingsUiState()
    data class Success(val bookings: List<Booking>) : MyBookingsUiState()
    data class Error(val message: String) : MyBookingsUiState()
}