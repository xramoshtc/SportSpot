package com.example.sportspot.ui.courts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportspot.domain.model.Court
import com.example.sportspot.ui.utils.courtTypeIcon

/**
 * Tipus de filtre disponibles per a la llista de pistes.
 *
 * "Tots" representa l'opció sense filtre actiu.
 *
 * @author Jesús Ramos
 */
private val FILTER_OPTIONS = listOf("Tots", "Tennis", "Pàdel", "Bàsquet", "Futbol")

/**
 * Pantalla que mostra la llista de pistes esportives disponibles.
 * Carrega les pistes en entrar i permet navegar al detall de cada una.
 *
 * @author Jesús Ramos
 *
 * @param onCourtSelected Funció que s'executa quan l'usuari selecciona una pista.
 * @param onBack Funció per tornar a la pantalla anterior.
 * @param viewModel ViewModel que proveeix l'estat i les pistes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtsScreen(
    onCourtSelected: (Long) -> Unit,
    onBack: () -> Unit,
    viewModel: CourtsViewModel = viewModel(
        factory = CourtsViewModel.provideFactory(LocalContext.current)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf("Tots") }

    LaunchedEffect(Unit) {
        viewModel.loadCourts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pistes disponibles",
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
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        when (uiState) {

            is CourtsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF4F7AA3))
                }
            }

            is CourtsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No s'han pogut carregar les pistes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadCourts() },
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

            is CourtsUiState.Success -> {
                val courts = (uiState as CourtsUiState.Success).courts

                // Aplica el filtre: si és "Tots" mostra totes, sinó filtra per tipus
                val filteredCourts = if (selectedFilter == "Tots") {
                    courts
                } else {
                    courts.filter { it.type.contains(selectedFilter, ignoreCase = true) }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Franja de filtres horitzontal
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FILTER_OPTIONS.forEach { filter ->
                            val isSelected = filter == selectedFilter
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedFilter = filter },
                                label = {
                                    Text(
                                        text = filter,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
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

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Color(0xFF1C2B3A).copy(alpha = 0.1f)
                    )

                    if (filteredCourts.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "🏟️",
                                    style = MaterialTheme.typography.displayMedium
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No hi ha pistes de $selectedFilter disponibles",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF1C2B3A).copy(alpha = 0.45f)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            items(filteredCourts) { court ->
                                CourtItem(
                                    court = court,
                                    onClick = { onCourtSelected(court.id) }
                                )
                            }
                        }
                    }
                }
            }

            is CourtsUiState.Idle -> {}
        }
    }
}

/**
 * Component que representa una pista dins de la llista.
 * Mostra el nom, tipus, localització i preu per hora de la pista.
 *
 * @author Jesús Ramos
 *
 * @param court Dades de la pista a mostrar.
 * @param onClick Funció que s'executa en prémer la targeta.
 */
@Composable
fun CourtItem(
    court: Court,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F0F8)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icona esquerra
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .padding(end = 0.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = courtTypeIcon(court.type),
                    contentDescription = null,
                    tint = Color(0xFF4F7AA3),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Nom, tipus i ubicació
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = court.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C2B3A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = court.type,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4F7AA3)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF1C2B3A).copy(alpha = 0.45f),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = court.location,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF1C2B3A).copy(alpha = 0.45f)
                    )
                }
            }

            // Preu destacat a la dreta
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2E86DE).copy(alpha = 0.15f)
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "${court.pricePerHour}€/h",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A3A5A),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}