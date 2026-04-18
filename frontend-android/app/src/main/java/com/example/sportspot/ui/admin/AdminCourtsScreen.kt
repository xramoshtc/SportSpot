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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportspot.domain.model.Court

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
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Tornar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear pista",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->

        when (uiState) {

            is AdminCourtsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is AdminCourtsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as AdminCourtsUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
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
                        Text(
                            text = "No hi ha pistes creades encara.",
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
            title = { Text("Eliminar pista") },
            text = {
                Text("Vols eliminar la pista ${court.name}? Aquesta acció no es pot desfer.")
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteCourt(court.id)
                    courtToDelete = null
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { courtToDelete = null }) {
                    Text("Cancel·lar")
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
            onDismiss = { courtToEdit = null },
            onConfirm = { name, type, price, location ->
                viewModel.updateCourt(court.id, name, type, price, location)
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
            onDismiss = { showCreateDialog = false },
            onConfirm = { name, type, price, location ->
                viewModel.createCourt(name, type, price, location)
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = court.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = court.type,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = court.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "${court.pricePerHour}€/h",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
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
                        tint = MaterialTheme.colorScheme.primary
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
 * @param onDismiss Funció que s'executa en tancar sense confirmar.
 * @param onConfirm Funció que s'executa en confirmar amb les dades introduïdes.
 */
@Composable
fun CourtFormDialog(
    title: String,
    initialName: String,
    initialType: String,
    initialPrice: String,
    initialLocation: String,
    onDismiss: () -> Unit,
    onConfirm: (name: String, type: String, price: Double, location: String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var type by remember { mutableStateOf(initialType) }
    var price by remember { mutableStateOf(initialPrice) }
    var location by remember { mutableStateOf(initialLocation) }
    var priceError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom de la pista") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Tipus (Pàdel, Tennis...)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Localització") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it
                        priceError = false
                    },
                    label = { Text("Preu per hora (€)") },
                    singleLine = true,
                    isError = priceError,
                    supportingText = {
                        if (priceError) Text(
                            "Introdueix un preu vàlid",
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val parsedPrice = price.replace(",", ".").toDoubleOrNull()
                if (parsedPrice == null) {
                    priceError = true
                    return@TextButton
                }
                onConfirm(name, type, parsedPrice, location)
            }) {
                Text(
                    text = "Confirmar",
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