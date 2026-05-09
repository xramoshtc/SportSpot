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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportspot.domain.model.Booking
import com.example.sportspot.domain.model.DayWeather
import com.example.sportspot.ui.weather.WeatherCompact
import com.example.sportspot.ui.utils.courtTypeIcon
import kotlinx.coroutines.launch
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
    val courts by viewModel.courts.collectAsState()

    // Mapa de nom de pista → tipus, per obtenir la icona correcta
    val courtTypeMap = courts.associate { it.name to it.type }
    // Mapa de nom de pista → id, per carregar les franges ocupades
    val courtIdMap = courts.associate { it.name to it.id }

    var bookingToEdit by remember { mutableStateOf<Booking?>(null) }
    var bookingToDelete by remember { mutableStateOf<Booking?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val actionResult by viewModel.actionResult.collectAsState()

    val occupiedSlots by viewModel.occupiedSlots.collectAsState()
    val loadingSlots by viewModel.loadingSlots.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBookings()
    }

    LaunchedEffect(actionResult) {
        actionResult?.let { result ->
            if (result == "ok") {
                snackbarHostState.showSnackbar("Reserva modificada correctament!")
            } else if (result.startsWith("error:")) {
                val message = result.removePrefix("error:")
                snackbarHostState.showSnackbar(message)
            }
            viewModel.resetActionResult()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Les meves reserves",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1C2B3A)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Tornar",
                            tint = Color(0xFF1C2B3A)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        when (uiState) {

            is MyBookingsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF4F7AA3))
                }
            }

            is MyBookingsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No s'han pogut carregar les reserves",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadBookings() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4F7AA3),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Tornar a intentar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            is MyBookingsUiState.Success -> {
                val bookings = (uiState as MyBookingsUiState.Success).bookings
                    .sortedBy { it.dateTime }

                if (bookings.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "📅",
                                style = MaterialTheme.typography.displayMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No tens cap reserva activa",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF1C2B3A).copy(alpha = 0.45f)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 20.dp)
                    ) {
                        items(bookings, key = { it.id }) { booking ->
                            BookingItem(
                                booking = booking,
                                courtType = courtTypeMap[booking.courtName] ?: "",
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
            containerColor = Color(0xFFE8F0F8),
            title = {
                Text(
                    text = "Cancel·lar reserva",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C2B3A)
                )
            },
            text = {
                Text(
                    text = "Vols cancel·lar la reserva de ${booking.courtName} " +
                            "el ${formatDateTime(booking.dateTime)}?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1C2B3A).copy(alpha = 0.75f)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteBooking(booking.id)
                        bookingToDelete = null
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Reserva cancel·lada correctament.")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Eliminar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { bookingToDelete = null }) {
                    Text(
                        "Cancel·lar",
                        color = Color(0xFF4F7AA3),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        )
    }

    // Diàleg de modificació de reserva
    bookingToEdit?.let { booking ->
        val courtId = courtIdMap[booking.courtName]

        EditBookingDialog(
            booking = booking,
            courtType = courtTypeMap[booking.courtName] ?: "",
            occupiedSlots = occupiedSlots,
            loadingSlots = loadingSlots,
            onDateChanged = { date ->
                // Recarreguem les franges quan canvia el dia
                courtId?.let { viewModel.loadOccupiedSlots(it, date) }
            },
            onDismiss = {
                bookingToEdit = null
                viewModel.resetOccupiedSlots()
            },
            onConfirm = { newDateTime, newDuration ->
                viewModel.updateBooking(booking.id, newDateTime, newDuration)
                bookingToEdit = null
                viewModel.resetOccupiedSlots()
            }
        )

        // Carreguem les franges del dia inicial de la reserva en obrir el diàleg
        LaunchedEffect(booking.id) {
            courtId?.let {
                val initialDate = booking.dateTime.take(10)
                viewModel.loadOccupiedSlots(it, initialDate)
            }
        }
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
 * @param courtType Tipus de la pista, obtingut del mapa de pistes carregades.
 * @param weather Previsió meteorològica del dia de la reserva, o null si no disponible.
 * @param onEdit Funció que s'executa en prémer el botó d'edició.
 * @param onDelete Funció que s'executa en prémer el botó d'eliminació.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingItem(
    booking: Booking,
    courtType: String,
    weather: DayWeather?,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F0F8)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Capçalera: nom pista + botons d'acció
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = courtTypeIcon(courtType),
                            contentDescription = null,
                            tint = Color(0xFF4F7AA3),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = booking.courtName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1C2B3A)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = booking.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF4F7AA3)
                    )
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Modificar reserva",
                            tint = Color(0xFF4F7AA3)
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
            HorizontalDivider(color = Color(0xFF1C2B3A).copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(10.dp))

            // Data i durada
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDateTime(booking.dateTime),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A3A5A)
                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2E86DE).copy(alpha = 0.15f)
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "${booking.durationHours}h",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1A3A5A),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Previsió del temps si disponible
            weather?.let {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFF1C2B3A).copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(10.dp))
                WeatherCompact(weather = it)
            } ?: run {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFF1C2B3A).copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "⏳ Previsió del temps encara no disponible",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1C2B3A)
                )
            }
        }
    }
}

/**
 * Diàleg per modificar la data i hora d'una reserva existent.
 *
 * Mostra els valors actuals pre-omplerts. La durada és sempre 60 minuts
 * i no es pot modificar. Permet seleccionar entre els pròxims 7 dies
 * i qualsevol hora de 08:00 a 23:00.
 *
 * @author Jesús Ramos
 *
 * @param booking Reserva que es vol modificar.
 * @param courtType Tipus de la pista, obtingut del mapa de pistes carregades.
 * @param occupiedSlots Conjunt de franges horàries ja ocupades per a la data seleccionada.
 * @param loadingSlots Indica si s'estan carregant les franges ocupades.
 * @param onDateChanged Funció que s'executa quan l'usuari canvia la data seleccionada.
 * @param onDismiss Funció que s'executa en tancar el diàleg sense confirmar.
 * @param onConfirm Funció que s'executa en confirmar amb la nova data i hora.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditBookingDialog(
    booking: Booking,
    courtType: String,
    occupiedSlots: Set<String>,
    loadingSlots: Boolean,
    onDateChanged: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (newDateTime: String, newDuration: Int) -> Unit
) {
    val initialDate = booking.dateTime.take(10)
    val initialHour = booking.dateTime.substring(11, 13).toIntOrNull() ?: 8
    val initialDuration = booking.durationHours

    // Generem els pròxims 7 dies a partir d'avui (avui inclòs)
    val today = java.time.LocalDate.now()
    val availableDates = (0..6).map { today.plusDays(it.toLong()) }

    // Si la data de la reserva és dins els pròxims 7 dies, la preseleccionem
    val initialLocalDate = runCatching {
        java.time.LocalDate.parse(initialDate)
    }.getOrNull()
    val preselectedDate = if (initialLocalDate != null && availableDates.contains(initialLocalDate))
        initialLocalDate else today

    var selectedDate by remember { mutableStateOf(preselectedDate) }
    var selectedHour by remember { mutableIntStateOf(initialHour) }
    var selectedDuration by remember { mutableIntStateOf(initialDuration) }

    // Totes les hores disponibles de 08:00 a 23:00
    val allHours = (8..23).toList()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFE8F0F8),
        title = {
            // Capçalera amb icona del tipus de pista i nom de la reserva
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = courtTypeIcon(courtType),
                    contentDescription = null,
                    tint = Color(0xFF4F7AA3),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Modificar reserva",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C2B3A)
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // Selector de data — pròxims 7 dies en LazyRow desplaçable
                Column {
                    Text(
                        text = "Data",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1C2B3A)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(availableDates) { date ->
                            val isSelected = selectedDate == date
                            val isToday = date == today
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedDate = date
                                    onDateChanged(date.toString())
                                },
                                label = {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = if (isToday) "Avui"
                                            else date.format(
                                                java.time.format.DateTimeFormatter
                                                    .ofPattern("EEE", java.util.Locale("ca"))
                                            ).replaceFirstChar { it.uppercase() },
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = if (isSelected) FontWeight.Bold
                                            else FontWeight.Normal
                                        )
                                        Text(
                                            text = date.dayOfMonth.toString(),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold
                                            else FontWeight.Normal
                                        )
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF4F7AA3),
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White,
                                    labelColor = Color(0xFF1C2B3A)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    selectedBorderColor = Color.Transparent,
                                    borderColor = Color(0xFF4F7AA3).copy(alpha = 0.4f)
                                )
                            )
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFF1C2B3A).copy(alpha = 0.1f))

                // Selector d'hora — totes les hores en LazyRow
                Column {
                    Text(
                        text = "Hora seleccionada: %02d:00".format(selectedHour),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1C2B3A)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (loadingSlots) {
                        // Missatge de càrrega mentre s'obtenen les franges ocupades
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = Color(0xFF4F7AA3)
                            )
                            Text(
                                text = "Carregant franges disponibles...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF1C2B3A).copy(alpha = 0.55f)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(allHours) { hour ->
                            val isSelected = selectedHour == hour
                            val slotKey = "%02d:00".format(hour)
                            // Excloem la franja actual de la reserva per permetre confirmar sense canvis
                            val isOccupied = occupiedSlots.contains(slotKey) &&
                                    slotKey != "%02d:00".format(initialHour)

                            FilterChip(
                                selected = isSelected,
                                onClick = { if (!isOccupied) selectedHour = hour },
                                enabled = !isOccupied,
                                label = {
                                    Text(
                                        text = "%02d:00".format(hour),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold
                                        else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF4F7AA3),
                                    selectedLabelColor = Color.White,
                                    containerColor = if (isOccupied) Color(0xFFFFCDD2)
                                    else Color.White,
                                    labelColor = if (isOccupied) Color(0xFFB71C1C)
                                    else Color(0xFF1C2B3A),
                                    disabledContainerColor = Color(0xFFFFCDD2),
                                    disabledLabelColor = Color(0xFFB71C1C)
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = !isOccupied,
                                    selected = isSelected,
                                    selectedBorderColor = Color.Transparent,
                                    borderColor = if (isOccupied) Color(0xFFB71C1C).copy(alpha = 0.4f)
                                    else Color(0xFF4F7AA3).copy(alpha = 0.4f),
                                    disabledBorderColor = Color(0xFFB71C1C).copy(alpha = 0.4f),
                                    disabledSelectedBorderColor = Color.Transparent
                                )
                            )
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFF1C2B3A).copy(alpha = 0.1f))

                // Resum de la nova data i hora seleccionades
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2E86DE).copy(alpha = 0.1f)
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "Nova data: ${selectedDate.format(
                            java.time.format.DateTimeFormatter.ofPattern(
                                "dd/MM/yyyy", java.util.Locale("ca")
                            )
                        )} a les %02d:00".format(selectedHour),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1A3A5A),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }

                // Camp de durada deshabilitat — mostra la durada de la reserva
                OutlinedTextField(
                    value = selectedDuration.toString(),
                    onValueChange = {},
                    label = {
                        Text(
                            "Durada (hores)",
                            color = Color(0xFF1C2B3A).copy(alpha = 0.45f)
                        )
                    },
                    enabled = false,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color(0xFF1C2B3A).copy(alpha = 0.2f),
                        disabledTextColor = Color(0xFF1C2B3A).copy(alpha = 0.45f),
                        disabledLabelColor = Color(0xFF1C2B3A).copy(alpha = 0.45f)
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newDateTime = "${selectedDate}T%02d:00:00".format(selectedHour)
                    onConfirm(newDateTime, selectedDuration)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4F7AA3),
                    contentColor = Color.White
                )
            ) {
                Text("Guardar", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Cancel·lar",
                    color = Color(0xFF4F7AA3),
                    fontWeight = FontWeight.Medium
                )
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