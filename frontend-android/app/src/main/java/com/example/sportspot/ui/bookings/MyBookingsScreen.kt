package com.example.sportspot.ui.bookings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportspot.domain.model.Booking
import com.example.sportspot.domain.model.DayWeather
import com.example.sportspot.ui.weather.WeatherCompact
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Pantalla que mostra totes les reserves de l'usuari autenticat.
 *
 * Permet consultar, modificar i cancel·lar reserves existents,
 * ordenades per data de més recent a més antiga.
 *
 * @author Jesús Ramos
 *
 * @param onBack Funció per tornar a la pantalla anterior.
 * @param viewModel ViewModel que proveeix l'estat i les accions sobre reserves.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    onBack: () -> Unit,
    viewModel: MyBookingsViewModel = viewModel(
        factory = MyBookingsViewModel.provideFactory(LocalContext.current)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val weatherMap by viewModel.weatherMap.collectAsState()

    var bookingToEdit by remember { mutableStateOf<Booking?>(null) }
    var bookingToDelete by remember { mutableStateOf<Booking?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadBookings()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Les meves reserves",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Tornar")
                    }
                }
            )
        }
    ) { innerPadding ->

        when (uiState) {

            is MyBookingsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MyBookingsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as MyBookingsUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is MyBookingsUiState.Success -> {
                // Ordenem per data de més recent a més antiga
                val bookings = (uiState as MyBookingsUiState.Success).bookings
                    .sortedBy { it.dateTime }

                if (bookings.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tens cap reserva activa.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(bookings, key = { it.id }) { booking ->
                            BookingItem(
                                booking = booking,
                                weather = weatherMap[booking.id],
                                onEdit = { bookingToEdit = booking },
                                onDelete = { bookingToDelete = booking }
                            )
                        }
                    }
                }
            }

            is MyBookingsUiState.Idle -> {}
        }
    }

    // Diàleg de confirmació de cancel·lació
    bookingToDelete?.let { booking ->
        AlertDialog(
            onDismissRequest = { bookingToDelete = null },
            title = { Text("Cancel·lar reserva") },
            text = {
                Text(
                    "Vols cancel·lar la reserva de ${booking.courtName} " +
                            "el ${formatDateTime(booking.dateTime)}?"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteBooking(booking.id)
                    bookingToDelete = null
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { bookingToDelete = null }) {
                    Text("Cancel·lar")
                }
            }
        )
    }

    // Diàleg de modificació de reserva
    bookingToEdit?.let { booking ->
        EditBookingDialog(
            booking = booking,
            onDismiss = { bookingToEdit = null },
            onConfirm = { newDateTime, newDuration ->
                viewModel.updateBooking(booking.id, newDateTime, newDuration)
                bookingToEdit = null
            }
        )
    }
}

/**
 * Component que representa una reserva dins de la llista.
 *
 * Mostra la informació de la reserva, la previsió del temps
 * i botons per modificar-la o cancel·lar-la.
 *
 * @author Jesús Ramos
 *
 * @param booking Dades de la reserva a mostrar.
 * @param weather Previsió meteorològica del dia de la reserva, o null si no disponible.
 * @param onEdit Funció que s'executa en prémer el botó d'edició.
 * @param onDelete Funció que s'executa en prémer el botó d'eliminació.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingItem(
    booking: Booking,
    weather: DayWeather?,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Capçalera: nom pista + botons d'acció
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
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
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Modificar reserva",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Cancel·lar reserva",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(10.dp))

            // Data i durada
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

            // Previsió del temps si disponible
            weather?.let {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(10.dp))
                WeatherCompact(weather = it)
            }
        }
    }
}

/**
 * Diàleg per modificar la data i hora d'una reserva existent.
 *
 * Mostra els valors actuals pre-omplerts. La durada és sempre 60 minuts
 * i no es pot modificar. Permet seleccionar qualsevol hora de 08:00 a 23:00.
 *
 * @author Jesús Ramos
 *
 * @param booking Reserva que es vol modificar.
 * @param onDismiss Funció que s'executa en tancar el diàleg sense confirmar.
 * @param onConfirm Funció que s'executa en confirmar amb la nova data i hora.
 */
@Composable
fun EditBookingDialog(
    booking: Booking,
    onDismiss: () -> Unit,
    onConfirm: (newDateTime: String, newDuration: Int) -> Unit
) {
    val initialDate = booking.dateTime.take(10)
    val initialHour = booking.dateTime.substring(11, 13).toIntOrNull() ?: 8

    var dateText by remember { mutableStateOf(initialDate) }
    var selectedHour by remember { mutableIntStateOf(initialHour) }

    // Totes les hores disponibles de 08:00 a 23:00
    val allHours = (8..23).toList()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Modificar reserva",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // Camp de data
                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text("Data (YYYY-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Selector d'hora — totes les hores en LazyRow
                Column {
                    Text(
                        text = "Hora seleccionada: %02d:00".format(selectedHour),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(allHours) { hour ->
                            FilterChip(
                                selected = selectedHour == hour,
                                onClick = { selectedHour = hour },
                                label = {
                                    Text(
                                        text = "%02d:00".format(hour),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (selectedHour == hour)
                                            FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }

                // Camp de durada deshabilitat — sempre 60 minuts
                OutlinedTextField(
                    value = "60",
                    onValueChange = {},
                    label = { Text("Durada (minuts)") },
                    enabled = false,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val newDateTime = "${dateText}T%02d:00".format(selectedHour)
                onConfirm(newDateTime, 60)
            }) {
                Text(
                    "Guardar",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel·lar")
            }
        }
    )
}

/**
 * Formata una cadena ISO de data i hora a un format llegible en català.
 *
 * Per exemple: "2026-04-20T10:00:00" → "20/04/2026 a les 10:00"
 *
 * @author Jesús Ramos
 *
 * @param dateTime Cadena en format ISO.
 * @return Cadena formatada per mostrar a la UI.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(dateTime: String): String {
    return try {
        val dt = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'a les' HH:mm"))
    } catch (e: Exception) {
        dateTime
    }
}