package com.example.sportspot.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.example.sportspot.domain.model.Court
import com.example.sportspot.ui.utils.courtTypeIcon

// Tipus de pista disponibles per al selector del formulari
private val COURT_TYPES = listOf("Tennis", "Pàdel", "Bàsquet", "Futbol")

/**
 * Pantalla de gestió de pistes per a l'administrador.
 *
 * Mostra la llista de totes les pistes i permet crear-ne de noves,
 * modificar les existents i eliminar-les.
 *
 * @author Jesús Ramos
 *
 * @param onBack Funció per tornar a la pantalla anterior.
 * @param viewModel ViewModel que proveeix l'estat i les accions sobre pistes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCourtsScreen(
    onBack: () -> Unit,
    viewModel: AdminCourtsViewModel = viewModel(
        factory = AdminCourtsViewModel.provideFactory(LocalContext.current)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    // Pista seleccionada per modificar (null = cap)
    var courtToEdit by remember { mutableStateOf<Court?>(null) }

    // Pista seleccionada per eliminar (null = cap)
    var courtToDelete by remember { mutableStateOf<Court?>(null) }

    // Controla la visibilitat del diàleg de creació
    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadCourts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gestió de pistes",
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = Color(0xFF4F7AA3)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear pista",
                    tint = Color.White
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        when (uiState) {

            is AdminCourtsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF4F7AA3))
                }
            }

            is AdminCourtsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = (uiState as AdminCourtsUiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
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

            is AdminCourtsUiState.Success -> {
                val courts = (uiState as AdminCourtsUiState.Success).courts

                if (courts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "🏟️",
                                style = MaterialTheme.typography.displayMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No hi ha pistes creades encara.",
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
                        contentPadding = PaddingValues(top = 20.dp, bottom = 100.dp)
                    ) {
                        items(courts, key = { it.id }) { court ->
                            AdminCourtItem(
                                court = court,
                                onEdit = { courtToEdit = court },
                                onDelete = { courtToDelete = court }
                            )
                        }
                    }
                }
            }

            is AdminCourtsUiState.Idle -> {}
        }
    }

    // Diàleg de confirmació d'eliminació
    courtToDelete?.let { court ->
        AlertDialog(
            onDismissRequest = { courtToDelete = null },
            containerColor = Color(0xFFE8F0F8),
            title = {
                Text(
                    text = "Eliminar pista",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C2B3A)
                )
            },
            text = {
                Text(
                    text = "Vols eliminar la pista ${court.name}? Aquesta acció no es pot desfer.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1C2B3A).copy(alpha = 0.75f)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteCourt(court.id)
                        courtToDelete = null
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
                TextButton(onClick = { courtToDelete = null }) {
                    Text(
                        "Cancel·lar",
                        color = Color(0xFF4F7AA3),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        )
    }

    // Diàleg de modificació de pista
    courtToEdit?.let { court ->
        CourtFormDialog(
            title = "Modificar pista",
            initialName = court.name,
            initialType = court.type,
            initialPrice = court.pricePerHour.toString(),
            initialLocation = court.location,
            initialCapacity = court.capacity.toString(),
            onDismiss = { courtToEdit = null },
            onConfirm = { name, type, price, location, capacity ->
                viewModel.updateCourt(court.id, name, type, price, location, capacity)
                courtToEdit = null
            }
        )
    }

    // Diàleg de creació de pista
    if (showCreateDialog) {
        CourtFormDialog(
            title = "Nova pista",
            initialName = "",
            initialType = "",
            initialPrice = "",
            initialLocation = "",
            initialCapacity = "",
            onDismiss = { showCreateDialog = false },
            onConfirm = { name, type, price, location, capacity ->
                viewModel.createCourt(name, type, price, location, capacity)
                showCreateDialog = false
            }
        )
    }
}

/**
 * Component que representa una pista dins de la llista d'administració.
 *
 * Mostra el nom, tipus, localització i preu, amb botons per editar i eliminar.
 *
 * @author Jesús Ramos
 *
 * @param court Dades de la pista a mostrar.
 * @param onEdit Funció que s'executa en prémer el botó d'edició.
 * @param onDelete Funció que s'executa en prémer el botó d'eliminació.
 */
@Composable
fun AdminCourtItem(
    court: Court,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0F8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = courtTypeIcon(court.type),
                        contentDescription = null,
                        tint = Color(0xFF4F7AA3),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = court.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1C2B3A)
                    )
                }
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
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = court.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF1C2B3A).copy(alpha = 0.55f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF2E86DE).copy(alpha = 0.15f)
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "${court.pricePerHour}€/h",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A3A5A),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF4F7AA3).copy(alpha = 0.12f)
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "${court.capacity} places",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1C2B3A).copy(alpha = 0.65f),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Modificar pista",
                        tint = Color(0xFF4F7AA3)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar pista",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * Diàleg reutilitzable per crear o modificar una pista esportiva.
 *
 * S'utilitza tant per al formulari de creació com per al de modificació,
 * pre-omplint els camps amb els valors inicials quan es modifica.
 *
 * @author Jesús Ramos
 *
 * @param title Títol del diàleg.
 * @param initialName Valor inicial del camp nom.
 * @param initialType Valor inicial del camp tipus.
 * @param initialPrice Valor inicial del camp preu.
 * @param initialLocation Valor inicial del camp localització.
 * @param initialCapacity Valor inicial del camp capacitat.
 * @param onDismiss Funció que s'executa en tancar sense confirmar.
 * @param onConfirm Funció que s'executa en confirmar amb les dades introduïdes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourtFormDialog(
    title: String,
    initialName: String,
    initialType: String,
    initialPrice: String,
    initialLocation: String,
    initialCapacity: String,
    onDismiss: () -> Unit,
    onConfirm: (name: String, type: String, price: Double, location: String, capacity: Int) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var type by remember { mutableStateOf(initialType) }
    var price by remember { mutableStateOf(initialPrice) }
    var location by remember { mutableStateOf(initialLocation) }
    var capacity by remember { mutableStateOf(initialCapacity) }
    var priceError by remember { mutableStateOf(false) }
    var capacityError by remember { mutableStateOf(false) }

    // Controla la visibilitat del desplegable de tipus de pista
    var typeDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFE8F0F8),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C2B3A)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom de la pista", color = Color(0xFF4F7AA3)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4F7AA3),
                        unfocusedBorderColor = Color(0xFF1C2B3A).copy(alpha = 0.3f),
                        focusedTextColor = Color(0xFF1C2B3A),
                        unfocusedTextColor = Color(0xFF1C2B3A)
                    )
                )

                // Selector de tipus de pista amb desplegable
                ExposedDropdownMenuBox(
                    expanded = typeDropdownExpanded,
                    onExpandedChange = { typeDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = type,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipus de pista", color = Color(0xFF4F7AA3)) },
                        placeholder = { Text("Selecciona un tipus") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = typeDropdownExpanded
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4F7AA3),
                            unfocusedBorderColor = Color(0xFF1C2B3A).copy(alpha = 0.3f),
                            focusedTextColor = Color(0xFF1C2B3A),
                            unfocusedTextColor = Color(0xFF1C2B3A)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = typeDropdownExpanded,
                        onDismissRequest = { typeDropdownExpanded = false }
                    ) {
                        COURT_TYPES.forEach { courtType ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = courtTypeIcon(courtType),
                                            contentDescription = null,
                                            tint = Color(0xFF4F7AA3),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = courtType,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color(0xFF1C2B3A)
                                        )
                                    }
                                },
                                onClick = {
                                    type = courtType
                                    typeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Localització", color = Color(0xFF4F7AA3)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4F7AA3),
                        unfocusedBorderColor = Color(0xFF1C2B3A).copy(alpha = 0.3f),
                        focusedTextColor = Color(0xFF1C2B3A),
                        unfocusedTextColor = Color(0xFF1C2B3A)
                    )
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it
                        priceError = false
                    },
                    label = { Text("Preu per hora (€)", color = Color(0xFF4F7AA3)) },
                    singleLine = true,
                    isError = priceError,
                    supportingText = {
                        if (priceError) Text(
                            "Introdueix un preu vàlid",
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4F7AA3),
                        unfocusedBorderColor = Color(0xFF1C2B3A).copy(alpha = 0.3f),
                        focusedTextColor = Color(0xFF1C2B3A),
                        unfocusedTextColor = Color(0xFF1C2B3A)
                    )
                )

                // Camp de capacitat màxima de la pista
                OutlinedTextField(
                    value = capacity,
                    onValueChange = {
                        capacity = it
                        capacityError = false
                    },
                    label = { Text("Capacitat (jugadors)", color = Color(0xFF4F7AA3)) },
                    singleLine = true,
                    isError = capacityError,
                    supportingText = {
                        if (capacityError) Text(
                            "Introdueix una capacitat vàlida",
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4F7AA3),
                        unfocusedBorderColor = Color(0xFF1C2B3A).copy(alpha = 0.3f),
                        focusedTextColor = Color(0xFF1C2B3A),
                        unfocusedTextColor = Color(0xFF1C2B3A)
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedPrice = price.replace(",", ".").toDoubleOrNull()
                    val parsedCapacity = capacity.toIntOrNull()
                    if (parsedPrice == null) {
                        priceError = true
                        return@Button
                    }
                    if (parsedCapacity == null) {
                        capacityError = true
                        return@Button
                    }
                    onConfirm(name, type, parsedPrice, location, parsedCapacity)
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