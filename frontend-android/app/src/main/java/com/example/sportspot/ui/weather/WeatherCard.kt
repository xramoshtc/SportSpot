package com.example.sportspot.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sportspot.domain.model.DayWeather
import com.example.sportspot.domain.model.WeatherHelper

/**
 * Targeta gran amb la previsió meteorològica d'un dia.
 *
 * S'utilitza a la pantalla de detall de pista per mostrar el temps
 * del dia seleccionat abans de confirmar la reserva.
 *
 * @author Jesús Ramos
 *
 * @param weather Dades meteorològiques del dia a mostrar.
 */
@Composable
fun WeatherCard(weather: DayWeather) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = WeatherHelper.weatherEmoji(weather.weatherCode),
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = WeatherHelper.weatherDescription(weather.weatherCode),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "${weather.tempMin}° — ${weather.tempMax}°C",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherStat(
                    emoji = "💧",
                    value = "${weather.precipitationProbability}%",
                    label = "Pluja"
                )
                WeatherStat(
                    emoji = "💨",
                    value = "${weather.windspeedMax} km/h",
                    label = "Vent"
                )
                WeatherStat(
                    emoji = "🌡️",
                    value = "${weather.tempMax}°C",
                    label = "Màx."
                )
            }
        }
    }
}

/**
 * Versió compacta del temps per mostrar dins d'una targeta de reserva.
 *
 * Mostra emoji, temperatura màxima i velocitat del vent en una sola fila.
 *
 * @author Jesús Ramos
 *
 * @param weather Dades meteorològiques del dia a mostrar.
 */
@Composable
fun WeatherCompact(weather: DayWeather) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = WeatherHelper.weatherEmoji(weather.weatherCode),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "${weather.tempMax}°C",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "💧 ${weather.precipitationProbability}%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = "💨 ${weather.windspeedMax} km/h",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

/**
 * Component auxiliar que mostra una estadística meteorològica amb emoji i etiqueta.
 *
 * @author Jesús Ramos
 *
 * @param emoji Emoji representatiu de l'estadística.
 * @param value Valor de l'estadística.
 * @param label Etiqueta descriptiva.
 */
@Composable
fun WeatherStat(
    emoji: String,
    value: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}