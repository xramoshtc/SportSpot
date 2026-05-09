package com.example.sportspot.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
            containerColor = Color(0xFFE8F0F8)
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = WeatherHelper.weatherEmoji(weather.weatherCode),
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = WeatherHelper.weatherDescription(weather.weatherCode),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C2B3A)
                    )
                    Text(
                        text = "${weather.tempMin}° — ${weather.tempMax}°C",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF1C2B3A).copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(
                color = Color(0xFF1C2B3A).copy(alpha = 0.1f)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherStat("💧", "${weather.precipitationProbability}%", "Pluja")
                WeatherStat("💨", "${weather.windspeedMax} km/h", "Vent")
                WeatherStat("🌡️", "${weather.tempMax}°C", "Màx.")
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
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = WeatherHelper.weatherEmoji(weather.weatherCode),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "${weather.tempMax}°C",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF1C2B3A)
        )
        Text(
            text = "💧 ${weather.precipitationProbability}%",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF1C2B3A)
        )
        Text(
            text = "💨 ${weather.windspeedMax} km/h",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF1C2B3A)
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
fun WeatherStat(emoji: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A6DB5)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF1C2B3A).copy(alpha = 0.55f)
        )
    }
}