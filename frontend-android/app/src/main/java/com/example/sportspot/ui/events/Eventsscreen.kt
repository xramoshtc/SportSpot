package com.example.sportspot.ui.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportspot.domain.model.Court
import com.example.sportspot.domain.model.Event
import java.util.Calendar

/**
 * Pantalla principal d'esdeveniments esportius.
 *
 * Mostra la llista d'esdeveniments actius amb targetes visuals que inclouen
 * la informació de la pista, la data, els participants i la barra d'ocupació.
 *
 * @param onBack Funció per tornar a la pantalla anterior.
 * @param viewModel ViewModel que gestiona l'estat i les accions.
 *
 * @author Jesús Ramos
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    onBack: () -> Unit,
    viewModel: EventViewModel = viewModel(
        factory = EventViewModel.provideFactory(LocalContext.current)
    )
) {
    val uiState         by viewModel.uiState.collectAsState()
    val actionState     by viewModel.actionState.collectAsState()
    val currentUsername by viewModel.currentUsername.collectAsState()
    val courts          by viewModel.courts.collectAsState()

    // Estat del diàleg de confirmació d'inscripció
    var eventToJoin  by remember { mutableStateOf<Event?>(null) }

    // Estat del diàleg d'abandonar/eliminar
    var eventToLeave by remember { mutableStateOf<Event?>(null) }

    // Estat del diàleg de creació d'un nou esdeveniment
    var showCreateDialog by remember { mutableStateOf(false) }

    // Snackbar per mostrar missatges de resultat de les accions
    val snackbarHostState = remember { SnackbarHostState() }

    // Carreguem els esdeveniments (i les pistes en paral·lel) en entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    // Gestionem els resultats de les accions (join, leave, create)
    LaunchedEffect(actionState) {
        when (actionState) {
            is EventActionState.JoinSuccess -> {
                snackbarHostState.showSnackbar("T'has apuntat a l'esdeveniment!")
                viewModel.resetActionState()
            }
            is EventActionState.LeaveSuccess -> {
                snackbarHostState.showSnackbar("Has abandonat l'esdeveniment.")
                viewModel.resetActionState()
            }
            is EventActionState.CreateSuccess -> {
                showCreateDialog = false
                snackbarHostState.showSnackbar("Esdeveniment creat correctament!")
                viewModel.resetActionState()
            }
            is EventActionState.Error -> {
                val msg = (actionState as EventActionState.Error).message
                snackbarHostState.showSnackbar("Error: $msg")
                viewModel.resetActionState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Esdeveniments",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Tornar")
                    }
                }
            )
        },
        //Botó flotant per crear un nou esdeveniment
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear esdeveniment",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {

                //Carregant
                is EventUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                //Error
                is EventUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No s'han pogut carregar els esdeveniments",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.loadEvents() }) {
                            Text("Tornar a intentar")
                        }
                    }
                }

                //Llista d'esdeveniments
                is EventUiState.Success -> {
                    val events = (uiState as EventUiState.Success).events

                    if (events.isEmpty()) {
                        // Estat buit. cap esdeveniment actiu
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🏟️",
                                style = MaterialTheme.typography.displayMedium
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Cap esdeveniment actiu",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Crea el primer amb el botó +",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            items(events, key = { it.id }) { event ->
                                EventCard(
                                    event = event,
                                    currentUsername = currentUsername,
                                    onJoin  = { eventToJoin  = event },
                                    onLeave = { eventToLeave = event }
                                )
                            }
                            //Espai extra al final per no tapar l'últim element amb el FAB
                            item { Spacer(modifier = Modifier.height(80.dp)) }
                        }
                    }
                }

                is EventUiState.Idle -> {}
            }

            //Indicador de càrrega d'acció (join/leave/create en procés)
            AnimatedVisibility(
                visible = actionState is EventActionState.Loading,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Text("Processant...", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }

    //Diàleg de confirmació d'inscripció
    eventToJoin?.let { event ->
        JoinConfirmDialog(
            event = event,
            onConfirm = {
                viewModel.joinEvent(event.id)
                eventToJoin = null
            },
            onDismiss = { eventToJoin = null }
        )
    }

    //Diàleg d'abandonar / eliminar
    eventToLeave?.let { event ->
        LeaveConfirmDialog(
            event = event,
            onConfirm = {
                viewModel.deleteOrLeaveEvent(event.id)
                eventToLeave = null
            },
            onDismiss = { eventToLeave = null }
        )
    }

    //Diàleg de creació d'esdeveniment
    if (showCreateDialog) {
        CreateEventDialog(
            courts = courts,
            onConfirm = { title, courtId, dateTime ->
                viewModel.createEvent(title, courtId, dateTime)
            },
            onDismiss = { showCreateDialog = false }
        )
    }
}


// EventCard
/**
 * Targeta visual que representa un esdeveniment a la llista.
 *
 * @param event Dades de l'esdeveniment a mostrar.
 * @param currentUsername Nom de l'usuari autenticat, per detectar si ja és participant.
 * @param onJoin Funció que obre el diàleg de confirmació d'inscripció.
 * @param onLeave Funció que obre el diàleg de confirmació d'abandonament.
 *
 * @author Jesús Ramos
 */
@Composable
fun EventCard(
    event: Event,
    currentUsername: String?,
    onJoin: () -> Unit,
    onLeave: () -> Unit
) {
    // Determinem si l'usuari actual ja és participant d'aquest event
    val isParticipant = currentUsername != null &&
            event.participantNames.contains(currentUsername)

    // Color de la barra de progrés segons l'ocupació
    val progressColor = when {
        event.isFull       -> MaterialTheme.colorScheme.error
        event.isAlmostFull -> Color(0xFFF57C00) // taronja
        else               -> MaterialTheme.colorScheme.primary
    }

    val occupancyFraction = if (event.maxCapacity > 0)
        event.currentParticipants.toFloat() / event.maxCapacity.toFloat()
    else 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            //Capçalera: títol + badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // Badge "🔥 Quasi ple!" quan queda exactament 1 plaça lliure
                if (event.isAlmostFull) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF57C00).copy(alpha = 0.15f)
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "🔥 Quasi ple!",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF57C00),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Badge "Complet" quan l'event és ple
                if (event.isFull) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "Complet",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            //Pista
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SportsTennis,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = event.courtName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            //Data i hora
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = formatEventDateTime(event.dateTime),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            //Organitzador
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Organitzat per ${event.organizerName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(12.dp))

            //Participants
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${event.currentParticipants}/${event.maxCapacity} participants",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            //Noms dels participants separats per punt mig
            if (event.participantNames.isNotEmpty()) {
                Text(
                    text = event.participantNames.joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            //Barra de progrés d'ocupació
            LinearProgressIndicator(
                progress = { occupancyFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = progressColor,
                trackColor = progressColor.copy(alpha = 0.15f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            //Botons d'acció
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Botó Apuntar-me / Apuntat / Complet
                // Es desactiva si l'event és ple O si l'usuari ja hi és inscrit
                Button(
                    onClick = onJoin,
                    enabled = !event.isFull && !isParticipant,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isParticipant)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        else
                            MaterialTheme.colorScheme.primary,
                        disabledContainerColor = if (isParticipant)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                ) {
                    //L'icona i el text canvien depenent de l'estat
                    if (isParticipant) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = when {
                            isParticipant -> "Apuntat ✓"
                            event.isFull  -> "Complet"
                            else          -> "Apuntar-me"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }

                //Botó Abandonar, només visible si l'usuari és participant
                if (isParticipant) {
                    OutlinedButton(
                        onClick = onLeave,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Abandonar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// JoinConfirmDialog
/**
 * Diàleg de confirmació d'inscripció a un esdeveniment.
 *
 * @param event Esdeveniment al qual es vol apuntar l'usuari.
 * @param onConfirm Funció que s'executa quan l'usuari confirma la inscripció.
 * @param onDismiss Funció que s'executa quan l'usuari cancel·la.
 *
 * @author Jesús Ramos
 */
@Composable
fun JoinConfirmDialog(
    event: Event,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Text(text = "🏅", style = MaterialTheme.typography.headlineMedium)
        },
        title = {
            Text(text = "Confirmar inscripció", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "T'estàs apuntant a:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.SportsTennis, contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = event.courtName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CalendarMonth, contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = formatEventDateTime(event.dateTime),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Group, contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Queden ${event.maxCapacity - event.currentParticipants} places lliures",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirmar", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel·lar") }
        }
    )
}

// LeaveConfirmDialog
/**
 * Diàleg de confirmació d'abandonament o eliminació d'un esdeveniment.
 *
 * @param event Esdeveniment que es vol abandonar o eliminar.
 * @param onConfirm Funció que s'executa quan l'usuari confirma l'acció.
 * @param onDismiss Funció que s'executa quan l'usuari cancel·la.
 *
 * @author Jesús Ramos
 */
@Composable
fun LeaveConfirmDialog(
    event: Event,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    //Si l'usuari és l'únic participant, abandonar eliminarà l'event
    val isOnlyParticipant = event.currentParticipants == 1

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Text(
                text = if (isOnlyParticipant) "⚠️" else "🚪",
                style = MaterialTheme.typography.headlineMedium
            )
        },
        title = {
            Text(
                text = if (isOnlyParticipant) "Eliminar esdeveniment"
                else "Abandonar esdeveniment",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = if (isOnlyParticipant)
                    "Ets l'únic participant de \"${event.title}\". Si abandones, l'esdeveniment s'eliminarà definitivament. Vols continuar?"
                else
                    "Estàs a punt d'abandonar \"${event.title}\". Segur que vols continuar?",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = if (isOnlyParticipant) "Eliminar" else "Abandonar",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel·lar") }
        }
    )
}

// CreateEventDialog
/**
 * Diàleg per crear un nou esdeveniment.
 *
 * @param courts Llista de pistes disponibles per al desplegable.
 * @param onConfirm Funció que s'executa amb el títol, l'ID de pista i la data/hora triats.
 * @param onDismiss Funció que s'executa quan l'usuari cancel·la.
 *
 * @author Jesús Ramos
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventDialog(
    courts: List<Court>,
    onConfirm: (title: String, courtId: Long, dateTime: String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var title            by remember { mutableStateOf("") }
    var selectedCourt    by remember { mutableStateOf<Court?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var selectedDate     by remember { mutableStateOf("") }
    var selectedTime     by remember { mutableStateOf("") }
    var errorMsg         by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Text(text = "🏟️", style = MaterialTheme.typography.headlineMedium)
        },
        title = {
            Text(text = "Nou esdeveniment", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                //Títol
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; errorMsg = null },
                    label = { Text("Títol de l'esdeveniment") },
                    placeholder = { Text("Ex: Torneig de Pàdel") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Desplegable: Selecció de pista
                if (courts.isEmpty()) {
                    Text(
                        text = "No s'han pogut carregar les pistes.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    ExposedDropdownMenuBox(
                        expanded = dropdownExpanded,
                        onExpandedChange = { dropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedCourt?.let {
                                "${it.name} — ${it.type}"
                            } ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Pista") },
                            placeholder = { Text("Selecciona una pista") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = dropdownExpanded
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            courts.forEach { court ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = court.name,
                                                fontWeight = FontWeight.Medium,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Text(
                                                text = "${court.type} · ${court.location} · Capacitat: ${court.capacity}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                                    .copy(alpha = 0.6f)
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedCourt = court
                                        dropdownExpanded = false
                                        errorMsg = null
                                    }
                                )
                            }
                        }
                    }
                }

                //Selector DatePickerDialog natiu d'Androi de data i hora
                OutlinedButton(
                    onClick = {
                        val cal = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                selectedDate = "%04d-%02d-%02d".format(year, month + 1, day)
                                //Un cop triada la data, obrim el selector d'hora
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        selectedTime = "%02d:%02d".format(hour, minute)
                                        errorMsg = null
                                    },
                                    cal.get(Calendar.HOUR_OF_DAY),
                                    cal.get(Calendar.MINUTE),
                                    true // format 24h
                                ).show()
                            },
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                        ).apply {
                            //No permet seleccionar dates passades
                            datePicker.minDate = System.currentTimeMillis()
                        }.show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty())
                            "$selectedDate a les $selectedTime"
                        else
                            "Seleccionar data i hora"
                    )
                }

                //Missatge d'error de validació
                errorMsg?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    //Validació dels camps abans d'enviar
                    when {
                        title.isBlank() -> {
                            errorMsg = "El títol no pot estar buit."
                        }
                        selectedCourt == null -> {
                            errorMsg = "Selecciona una pista."
                        }
                        selectedDate.isEmpty() || selectedTime.isEmpty() -> {
                            errorMsg = "Selecciona la data i l'hora."
                        }
                        else -> {
                            // onstruïm el format ISO-8601 esperat pel servidor
                            val dateTime = "${selectedDate}T${selectedTime}:00"
                            onConfirm(title.trim(), selectedCourt!!.id, dateTime)
                        }
                    }
                }
            ) {
                Text("Crear", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel·lar") }
        }
    )
}

// Helpers
/**
 * Formata una cadena de data i hora ISO-8601 en un format llegible per l'usuari.
 *
 * @param dateTime Cadena de data i hora en format ISO-8601.
 * @return Cadena formatada per mostrar a la UI.
 *
 * @author Jesús Ramos
 */
fun formatEventDateTime(dateTime: String): String {
    return try {
        val parts = dateTime.split("T")
        if (parts.size < 2) return dateTime
        val dateParts = parts[0].split("-")
        val timePart  = parts[1].take(5)
        if (dateParts.size < 3) return dateTime
        "${dateParts[2]}/${dateParts[1]}/${dateParts[0]} a les $timePart"
    } catch (e: Exception) {
        dateTime
    }
}