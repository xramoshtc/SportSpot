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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportspot.ui.utils.courtTypeIcon
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
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsState()
    val weather by viewModel.weather.collectAsState()
    val occupiedSlots by viewModel.occupiedSlots.collectAsState()
    val loadingSlots by viewModel.loadingSlots.collectAsState()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedSlot by remember { mutableStateOf<String?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }



    val today = LocalDate.now()

    LaunchedEffect(uiState, selectedDate) {
        if (uiState is CourtDetailUiState.BookingSuccess) {
            snackbarHostState.showSnackbar("Reserva confirmada!")
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
            // Carreguem les franges ocupades per al dia seleccionat
            viewModel.loadOccupiedSlots(
                courtId = it.id,
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
                    Text(
                        text = title,
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

            is CourtDetailUiState.Loading, CourtDetailUiState.Idle -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF4F7AA3))
                }
            }

            is CourtDetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No s'ha pogut carregar la pista",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadCourt(courtId) },
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
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 20.dp)
                ) {

                    // Targeta d'informació de la pista
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8F0F8)
                            ),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Esquerra: tipus i localització
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = courtTypeIcon(court.type),
                                            contentDescription = null,
                                            tint = Color(0xFF4F7AA3),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = court.type,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4F7AA3)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = Color(0xFF1C2B3A).copy(alpha = 0.45f)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = court.location,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color(0xFF1C2B3A).copy(alpha = 0.55f)
                                        )
                                    }
                                }

                                // Dreta: preu destacat
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFF2E86DE).copy(alpha = 0.15f)
                                    ),
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        text = "${court.pricePerHour}€/h",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1A3A5A),
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
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1C2B3A)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        androidx.compose.foundation.lazy.LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val days = (0..6).map { today.plusDays(it.toLong()) }
                            items(days) { date ->
                                val isSelected = date == selectedDate
                                val isToday = date == today
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedDate = date
                                        selectedSlot = null
                                    },
                                    label = {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = if (isToday) "Avui"
                                                else date.dayOfWeek.getDisplayName(
                                                    TextStyle.SHORT, Locale("ca")
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
                                        containerColor = Color(0xFFE8F0F8),
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

                    // Previsió del temps
                    item {
                        weather?.let {
                            WeatherCard(weather = it)
                        } ?: Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8F0F8)
                            ),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = Color(0xFF4F7AA3)
                                )
                                Text(
                                    text = "Carregant el temps...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF1C2B3A).copy(alpha = 0.55f)
                                )
                            }
                        }
                    }

                    // Títol de la graella de franges horàries
                    item {
                        Text(
                            text = "Selecciona una franja horària",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1C2B3A)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        if (loadingSlots) {
                            // Missatge de càrrega mentre s'obtenen les franges ocupades
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE8F0F8)
                                ),
                                elevation = CardDefaults.cardElevation(2.dp),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp,
                                        color = Color(0xFF4F7AA3)
                                    )
                                    Text(
                                        text = "Carregant franges disponibles...",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color(0xFF1C2B3A).copy(alpha = 0.55f)
                                    )
                                }
                            }
                        } else {
                            val slots = (8..23).map { hour -> "%02d:00".format(hour) }
                            slots.chunked(4).forEach { rowSlots ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowSlots.forEach { slot ->
                                        SlotGridItem(
                                            slot = slot,
                                            isSelected = slot == selectedSlot,
                                            isOccupied = occupiedSlots.contains(slot),
                                            onClick = {
                                                if (!occupiedSlots.contains(slot)) selectedSlot = slot
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    repeat(4 - rowSlots.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
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
                                style = MaterialTheme.typography.bodyMedium,
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
                                containerColor = Color(0xFF4F7AA3),
                                contentColor = Color.White,
                                disabledContainerColor = Color(0xFF1C2B3A).copy(alpha = 0.12f),
                                disabledContentColor = Color(0xFF1C2B3A).copy(alpha = 0.4f)
                            )
                        ) {
                            Text(
                                text = if (selectedSlot != null)
                                    "Reservar ${selectedSlot}h"
                                else
                                    "Selecciona una franja",
                                style = MaterialTheme.typography.titleMedium,
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
                        containerColor = Color(0xFFE8F0F8),
                        title = {
                            Text(
                                text = "Confirmar reserva",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1C2B3A)
                            )
                        },
                        text = {
                            Text(
                                text = "Vols reservar ${court.name} el " +
                                        "${selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} " +
                                        "de ${selectedSlot}h a ${"%02d:00".format(hour + 1)}h?",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF1C2B3A).copy(alpha = 0.75f)
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showConfirmDialog = false
                                    viewModel.createBooking(
                                        courtId = court.id,
                                        dateTime = dateTimeIso,
                                        durationHours = 1
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4F7AA3),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Confirmar", fontWeight = FontWeight.Bold)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showConfirmDialog = false }) {
                                Text(
                                    "Cancel·lar",
                                    color = Color(0xFF4F7AA3),
                                    fontWeight = FontWeight.Medium
                                )
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
 * Les franges ocupades es mostren en vermell i no es poden seleccionar.
 *
 * @author Jesús Ramos
 *
 * @param slot Hora d'inici de la franja (ex: "10:00").
 * @param isSelected Indica si aquesta franja està seleccionada.
 * @param isOccupied Indica si aquesta franja ja està reservada.
 * @param onClick Funció que s'executa en prémer la franja.
 * @param modifier Modifier addicional per controlar l'amplada dins la graella.
 */
@Composable
fun SlotGridItem(
    slot: String,
    isSelected: Boolean,
    isOccupied: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = when {
        isOccupied -> Color(0xFFFFCDD2)
        isSelected -> Color(0xFF4F7AA3)
        else       -> Color(0xFFE8F0F8)
    }
    val textColor = when {
        isOccupied -> Color(0xFFB71C1C)
        isSelected -> Color.White
        else       -> Color(0xFF1C2B3A)
    }

    Card(
        onClick = onClick,
        enabled = !isOccupied,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(if (isSelected) 6.dp else 2.dp),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.height(56.dp)
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
                    color = textColor
                )
                Text(
                    text = if (isOccupied) "Ocupat"
                    else "%02d:00".format(slot.take(2).toInt() + 1),
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor.copy(alpha = if (isOccupied) 0.8f else 0.75f)
                )
            }
        }
    }
}