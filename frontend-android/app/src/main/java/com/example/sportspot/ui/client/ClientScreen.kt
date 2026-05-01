package com.example.sportspot.ui.client

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportspot.domain.model.Booking
import com.example.sportspot.domain.model.DayWeather
import com.example.sportspot.ui.bookings.formatDateTime
import com.example.sportspot.ui.weather.WeatherCompact
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

/**
 * Pantalla principal per a l'usuari client.
 *
 * Mostra la propera reserva amb previsió del temps i botons d'accés
 * ràpid a les funcionalitats principals: reservar pista, les meves reserves,
 * esdeveniments i el meu perfil.
 *
 * @param onLogout Funció que s'executa quan el logout ha finalitzat.
 * @param onNavigateToProfile Funció per navegar al perfil.
 * @param onNavigateToCourts Funció per navegar al llistat de pistes.
 * @param onNavigateToMyBookings Funció per navegar a les reserves.
 * @param onNavigateToEvents Funció per navegar als esdeveniments.
 * @param viewModel ViewModel que proveeix l'estat i les accions del client.
 *
 * @author Jesús Ramos
 */
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ContextCastToActivity")
@Composable
fun ClientScreen(
    viewModel: ClientViewModel = viewModel(
        factory = ClientViewModel.provideFactory(LocalContext.current)
    ),
    onLogout: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCourts: () -> Unit,
    onNavigateToMyBookings: () -> Unit,
    onNavigateToEvents: () -> Unit
) {
    val token by viewModel.token.collectAsState()
    val nextBooking by viewModel.nextBooking.collectAsState()
    val nextBookingWeather by viewModel.nextBookingWeather.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val activity = LocalContext.current as? android.app.Activity
    val username by viewModel.username.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadNextBooking()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //Capçalera: salutació + avatar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Hola! 👋 $username",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Usuari",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        //Targeta de propera reserva
        NextBookingCard(
            booking = nextBooking,
            weather = nextBookingWeather
        )

        Spacer(modifier = Modifier.height(24.dp))

        //Primera fila: Reservar pista + Les meves reserves
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Botó Reservar pista
            DashboardCard(
                onClick = onNavigateToCourts,
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.SportsTennis,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Reservar pista",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            }

            //Botó Les meves reserves
            DashboardCard(
                onClick = onNavigateToMyBookings,
                modifier = Modifier.weight(1f),
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Les meves reserves",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        //Segona fila: Esdeveniments + El meu perfil
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            //Botó Esdeveniments
            DashboardCard(
                onClick = onNavigateToEvents,
                modifier = Modifier.weight(1f),
                containerColor = Color(0xFF81C784)
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Esdeveniments",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.Center
                )
            }

            //Botó El meu perfil
            DashboardCard(
                onClick = onNavigateToProfile,
                modifier = Modifier.weight(1f),
                containerColor = Color(0xFFFFE082)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF795548) ,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "El meu perfil",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF795548),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        //Token de sessió
        Text(
            text = "Token",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = token ?: "Sense token",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.weight(1f))

        //Botó Tancar sessió
        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.logout()
                    onLogout()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Tancar sessió", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        //Botó Sortir
        Button(
            onClick = { activity?.finish() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text("Sortir", fontWeight = FontWeight.Bold)
        }
    }
}


// DashboardCard — component reutilitzable per als botons de la graella
/**
 * Targeta quadrada reutilitzable per als botons del dashboard del client.
 *
 *
 * @param onClick Acció en prémer la targeta.
 * @param modifier Modifier addicional (normalment weight).
 * @param containerColor Color de fons de la targeta.
 * @param content Contingut interior (icona + text).
 *
 * @author Jesús Ramos
 */
@Composable
fun DashboardCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: androidx.compose.ui.graphics.Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = content
        )
    }
}

// NextBookingCard - Targeta de propera reserva
/**
 * Targeta que mostra la propera reserva de l'usuari amb previsió del temps.
 * Si no hi ha cap reserva futura, mostra un missatge informatiu.
 *
 * @param booking Propera reserva de l'usuari, o null si no n'hi ha.
 * @param weather Previsió meteorològica de la reserva, o null si no disponible.
 *
 * @author Jesús Ramos
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NextBookingCard(
    booking: Booking?,
    weather: DayWeather?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Propera reserva",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (booking == null) {
                Text(
                    text = "No tens cap pista reservada",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            } else {
                Text(
                    text = booking.courtName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = booking.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatDateTime(booking.dateTime),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "${booking.durationMinutes} min",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                weather?.let {
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    WeatherCompact(weather = it)
                }
            }
        }
    }
}