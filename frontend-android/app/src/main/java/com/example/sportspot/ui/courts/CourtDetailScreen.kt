package com.example.sportspot.ui.courts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportspot.ui.weather.WeatherCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * Pantalla de detall d'una pista esportiva.
 *
 * Mostra la informació de la pista i permet seleccionar un dia i
 * una franja horària per fer una reserva.
 *
 * @author Jesús Ramos
 *
 * @param courtId Identificador de la pista a mostrar.
 * @param onBack Funció per tornar a la pantalla anterior.
 * @param onBookingConfirmed Funció que s'executa quan la reserva s'ha confirmat.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtDetailScreen(
    courtId: Long,
    onBack: () -> Unit,
    onBookingConfirmed: () -> Unit,
    viewModel: CourtDetailViewModel = viewModel(
        factory = CourtDetailViewModel.provideFactory(LocalContext.current)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val weather by viewModel.weather.collectAsState()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedSlot by remember { mutableStateOf<String?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState, selectedDate) {
        if (uiState is CourtDetailUiState.BookingSuccess) {
            onBookingConfirmed()
            return@LaunchedEffect
        }
        val court = when (val s = uiState) {
            is CourtDetailUiState.Success -> s.court
            is CourtDetailUiState.BookingError -> s.court
            is CourtDetailUiState.BookingSuccess -> s.court
            else -> null
        }
        court?.let {
            viewModel.loadWeather(
                city = it.location,
                date = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            )
        }
    }

    LaunchedEffect(courtId) {
        viewModel.loadCourt(courtId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = (uiState as? CourtDetailUiState.Success)?.court?.name ?: "Detall"
                    Text(title)
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

            is CourtDetailUiState.Loading, CourtDetailUiState.Idle -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is CourtDetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as CourtDetailUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is CourtDetailUiState.Success,
            is CourtDetailUiState.BookingSuccess,
            is CourtDetailUiState.BookingError -> {

                val court = when (val s = uiState) {
                    is CourtDetailUiState.Success -> s.court
                    is CourtDetailUiState.BookingSuccess -> s.court
                    is CourtDetailUiState.BookingError -> s.court
                    else -> return@Scaffold
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {

                    // Targeta d'informació de la pista
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Esquerra: tipus i localització
                                Column {
                                    Text(
                                        text = court.type,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp),
                                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = court.location,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                }

                                // Dreta: preu destacat
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                    ),
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        text = "${court.pricePerHour}€/h",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Selector de dia
                    item {
                        Text(
                            text = "Selecciona un dia",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        androidx.compose.foundation.lazy.LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val days = (0..6).map { LocalDate.now().plusDays(it.toLong()) }
                            items(days) { date ->
                                val isSelected = date == selectedDate
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedDate = date
                                        selectedSlot = null
                                    },
                                    label = {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.padding(vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = date.dayOfWeek
                                                    .getDisplayName(TextStyle.SHORT, Locale("ca")),
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Text(
                                                text = date.dayOfMonth.toString(),
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }

                    // Previsió del temps
                    item {
                        weather?.let {
                            WeatherCard(weather = it)
                        } ?: Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }

                    // Títol de la graella de franges horàries
                    item {
                        Text(
                            text = "Selecciona una franja horària",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Graella 4 columnes
                        val slots = (8..23).map { hour ->
                            "%02d:00".format(hour)
                        }

                        // Usem LazyVerticalGrid dins d'un Box amb alçada fixa
                        // perquè LazyColumn no admet LazyVerticalGrid directament
                        val rowCount = Math.ceil(slots.size / 4.0).toInt()
                        val gridHeight = (rowCount * 72).dp

                        Box(modifier = Modifier.height(gridHeight)) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(4),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                userScrollEnabled = false
                            ) {
                                items(slots) { slot ->
                                    val isSelected = slot == selectedSlot
                                    SlotGridItem(
                                        slot = slot,
                                        isSelected = isSelected,
                                        onClick = { selectedSlot = slot }
                                    )
                                }
                            }
                        }
                    }

                    // Botó de reserva
                    item {
                        Spacer(modifier = Modifier.height(8.dp))

                        if (uiState is CourtDetailUiState.BookingError) {
                            Text(
                                text = (uiState as CourtDetailUiState.BookingError).message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        Button(
                            onClick = { if (selectedSlot != null) showConfirmDialog = true },
                            enabled = selectedSlot != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Text(
                                text = if (selectedSlot != null)
                                    "Reservar ${selectedSlot}h"
                                else
                                    "Selecciona una franja",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // Diàleg de confirmació
                if (showConfirmDialog && selectedSlot != null) {
                    val hour = selectedSlot!!.take(2).toInt()
                    val dateTimeIso = selectedDate.format(
                        DateTimeFormatter.ISO_LOCAL_DATE
                    ) + "T%02d:00".format(hour)

                    AlertDialog(
                        onDismissRequest = { showConfirmDialog = false },
                        title = { Text("Confirmar reserva") },
                        text = {
                            Text(
                                "Vols reservar ${court.name} el " +
                                        "${selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} " +
                                        "de ${selectedSlot}h a ${"%02d:00".format(hour + 1)}h?"
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                showConfirmDialog = false
                                viewModel.createBooking(
                                    courtId = court.id,
                                    dateTime = dateTimeIso,
                                    durationMinutes = 60
                                )
                            }) {
                                Text("Confirmar", color = MaterialTheme.colorScheme.primary)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showConfirmDialog = false }) {
                                Text("Cancel·lar")
                            }
                        }
                    )
                }
            }
        }
    }
}

/**
 * Component que representa una franja horària dins de la graella.
 *
 * Mostra l'hora de inici de la franja i es ressalta quan està seleccionada.
 *
 * @author Jesús Ramos
 *
 * @param slot Hora d'inici de la franja (ex: "10:00").
 * @param isSelected Indica si aquesta franja està seleccionada.
 * @param onClick Funció que s'executa en prémer la franja.
 */
@Composable
fun SlotGridItem(
    slot: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surface

    val contentColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurface

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(if (isSelected) 6.dp else 2.dp),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = slot,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Text(
                    text = "%02d:00".format(slot.take(2).toInt() + 1),
                    style = MaterialTheme.typography.labelSmall,
                    color = contentColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}