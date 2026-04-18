package com.example.sportspot.ui.client

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.repository.AuthRepository
import com.example.sportspot.data.repository.CourtRepository
import com.example.sportspot.data.repository.WeatherRepository
import com.example.sportspot.domain.model.Booking
import com.example.sportspot.domain.model.DayWeather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * ViewModel per la pantalla principal del client.
 *
 * Gestiona l'estat del token, les accions de sessió i la càrrega
 * de la propera reserva amb la seva previsió meteorològica.
 *
 * @author Jesús Ramos
 *
 * @param repository Repositori d'autenticació per fer logout.
 * @param courtRepository Repositori per obtenir les reserves de l'usuari.
 * @param weatherRepository Repositori per obtenir la previsió meteorològica.
 * @param dataStore Manager per accedir al token emmagatzemat localment.
 */
class ClientViewModel(
    private val repository: AuthRepository,
    private val courtRepository: CourtRepository,
    private val weatherRepository: WeatherRepository,
    dataStore: DataStoreManager
) : ViewModel() {

    /**
     * Token exposat com a StateFlow.
     *
     * @author Jesús Ramos
     */
    val token = dataStore.tokenFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )
    /**
     * Username exposat com a StateFlow.
     *
     * @author Jesús Ramos
     */
    val username = dataStore.usernameFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    /**
     * Propera reserva futura de l'usuari, o null si no n'hi ha.
     *
     * @author Jesús Ramos
     */
    private val _nextBooking = MutableStateFlow<Booking?>(null)
    val nextBooking: StateFlow<Booking?> = _nextBooking

    /**
     * Previsió meteorològica de la propera reserva, o null si no disponible.
     *
     * @author Jesús Ramos
     */
    private val _nextBookingWeather = MutableStateFlow<DayWeather?>(null)
    val nextBookingWeather: StateFlow<DayWeather?> = _nextBookingWeather

    /**
     * Carrega la propera reserva futura i la seva previsió del temps.
     *
     * Filtra les reserves passades i agafa la més pròxima en el temps.
     * Un cop obtinguda, carrega el temps per a la seva ubicació i data.
     *
     * @author Jesús Ramos
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadNextBooking() {
        viewModelScope.launch {
            try {
                val currentToken = token.first { !it.isNullOrBlank() } ?: return@launch
                val now = LocalDateTime.now()
                val bookings = courtRepository.getMyBookings(currentToken)

                // Filtrem les reserves futures i agafem la més pròxima
                val next = bookings
                    .filter { booking ->
                        try {
                            val dt = LocalDateTime.parse(
                                booking.dateTime,
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME
                            )
                            dt.isAfter(now)
                        } catch (e: Exception) {
                            false
                        }
                    }
                    .minByOrNull { it.dateTime }

                _nextBooking.value = next

                // Carreguem el temps per a la propera reserva si existeix
                next?.let { booking ->
                    val date = booking.dateTime.take(10)
                    _nextBookingWeather.value = weatherRepository.getWeatherForCityAndDate(
                        city = booking.location,
                        date = date
                    )
                }
            } catch (e: Exception) {
                _nextBooking.value = null
            }
        }
    }

    /**
     * Tanca la sessió de l'usuari.
     *
     * @author Jesús Ramos
     *
     * @return true si el logout ha anat bé, false en cas d'error o si no hi ha token.
     */
    suspend fun logout(): Boolean {
        val currentToken = token.value
        if (currentToken.isNullOrBlank()) return false
        return try {
            repository.logout(currentToken)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

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
                    val dataStore = DataStoreManager(context.applicationContext)
                    val repository = AuthRepository(dataStore)
                    val courtRepository = CourtRepository()
                    val weatherRepository = WeatherRepository()
                    return ClientViewModel(
                        repository = repository,
                        courtRepository = courtRepository,
                        weatherRepository = weatherRepository,
                        dataStore = dataStore
                    ) as T
                }
            }
        }
    }
}