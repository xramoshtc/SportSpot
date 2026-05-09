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
import com.example.sportspot.ui.utils.courtTypeIcon
import java.util.Calendar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.Person

/**
 * Pantalla principal d'esdeveniments esportius.
 *
 * Mostra la llista d'esdeveniments actius amb targetes visuals que inclouen
 * la informació de la pista, la data, els participants i la barra d'ocupació.
 *
 * @author Jesús Ramos
 *
 * @param onBack Funció per tornar a la pantalla anterior.
 * @param viewModel ViewModel que gestiona l'estat i les accions.
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

    // Mapa de nom de pista → tipus, per obtenir la icona correcta
    val courtTypeMap = courts.associate { it.name to it.type }

    // Mapa de nom de pista → tipus, per obtenir la icona correcta
    val courtLocationMap = courts.associate { it.name to it.location }

    // Estat del filtre seleccionat — "Tots" per defecte
    var selectedFilter by remember { mutableStateOf("Apuntat") }

    // Opcions de filtre derivades de les pistes carregades + "Tots" al davant
    val filterOptions = remember(courts) {
        listOf("Apuntat", "Tots") + courts.map { it.type }.distinct().sorted()
    }

    // Carreguem els esdeveniments (i les pistes en paral·lel) en entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    // Canviem a la pestanya "Apuntat" en apuntar-se, i refresquem en desapuntar-se
    LaunchedEffect(actionState) {
        when (actionState) {
            is EventActionState.JoinSuccess -> {
                selectedFilter = "Apuntat"
                snackbarHostState.showSnackbar("T'has apuntat a l'esdeveniment!")
                viewModel.resetActionState()

            }
            is EventActionState.LeaveSuccess -> {
                selectedFilter = "Apuntat"
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
        //Botó flotant per crear un nou esdeveniment
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = Color(0xFF4F7AA3)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear esdeveniment",
                    tint = Color.White
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {

                //Carregant
                is EventUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF4F7AA3)
                    )
                }

                //Error
                is EventUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No s'han pogut carregar els esdeveniments",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadEvents() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4F7AA3),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Tornar a intentar", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                //Llista d'esdeveniments
                is EventUiState.Success -> {
                    val events = (uiState as EventUiState.Success).events

                    // Aplica el filtre pel tipus de pista o per inscripció, ordenats per data
                    val filteredEvents = when (selectedFilter) {
                        "Apuntat" -> events
                            .filter { event ->
                                currentUsername != null &&
                                        event.participantNames.contains(currentUsername)
                            }
                            .sortedBy { it.dateTime }
                        "Tots" -> events.sortedBy { it.dateTime }
                        else -> events
                            .filter { event ->
                                val type = courtTypeMap[event.courtName] ?: ""
                                type.contains(selectedFilter, ignoreCase = true) &&
                                        !(currentUsername != null && event.participantNames.contains(currentUsername))
                            }
                            .sortedBy { it.dateTime }
                    }

                    Column(modifier = Modifier.fillMaxSize()) {

                        // Franja de filtres horitzontal
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            filterOptions.forEach { filter ->
                                val isSelected = filter == selectedFilter
                                val isApuntat = filter == "Apuntat"
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
                                        selectedContainerColor = if (isApuntat) Color(0xFF2E7D32) else Color(0xFF4F7AA3),
                                        selectedLabelColor = Color.White,
                                        containerColor = if (isApuntat) Color(0xFF2E7D32).copy(alpha = 0.12f)
                                        else Color(0xFFE8F0F8),
                                        labelColor = if (isApuntat) Color(0xFF2E7D32) else Color(0xFF1C2B3A)
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isSelected,
                                        selectedBorderColor = Color.Transparent,
                                        borderColor = if (isApuntat) Color(0xFF2E7D32).copy(alpha = 0.5f)
                                        else Color(0xFF4F7AA3).copy(alpha = 0.4f)
                                    )
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            color = Color(0xFF1C2B3A).copy(alpha = 0.1f)
                        )

                        if (filteredEvents.isEmpty()) {
                            // Estat buit amb filtre actiu
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (selectedFilter == "Apuntat") "🏅" else "🏟️",
                                    style = MaterialTheme.typography.displayMedium
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = if (selectedFilter == "Apuntat")
                                        "Encara no estàs apuntat a cap esdeveniment."
                                    else if (selectedFilter == "Tots") "Cap esdeveniment actiu"
                                    else "Cap esdeveniment de $selectedFilter",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF1C2B3A).copy(alpha = 0.45f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                )
                                if (selectedFilter == "Apuntat") {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Cerca el teu esport favorit des de l'apartat superior i competeix amb altres.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF1C2B3A).copy(alpha = 0.35f),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 24.dp)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Crea el primer amb el botó +",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF1C2B3A).copy(alpha = 0.35f)
                                    )
                                }
                            }
                        } else {
                            // Llista d'esdeveniments filtrats
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                contentPadding = PaddingValues(vertical = 16.dp)
                            ) {
                                items(filteredEvents, key = { it.id }) { event ->
                                    EventCard(
                                        event = event,
                                        courtType = courtTypeMap[event.courtName] ?: "",
                                        courtLocation = courtLocationMap[event.courtName] ?: "",
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
                        containerColor = Color(0xFFE8F0F8)
                    ),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color(0xFF4F7AA3)
                        )
                        Text(
                            "Processant...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF1C2B3A)
                        )
                    }
                }
            }
        }
    }

    //Diàleg de confirmació d'inscripció
    eventToJoin?.let { event ->
        JoinConfirmDialog(
            event = event,
            courtType = courtTypeMap[event.courtName] ?: "",
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
            currentUsername = currentUsername,   // <- nou
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

/**
 * Targeta visual que representa un esdeveniment a la llista.
 *
 * @author Jesús Ramos
 *
 * @param event Dades de l'esdeveniment a mostrar.
 * @param courtLocation Localització de la pista, obtinguda del mapa de pistes carregades.
 * @param courtType Tipus de la pista, obtingut del mapa de pistes carregades.
 * @param currentUsername Nom de l'usuari autenticat, per detectar si ja és participant.
 * @param onJoin Funció que obre el diàleg de confirmació d'inscripció.
 * @param onLeave Funció que obre el diàleg de confirmació d'abandonament.
 */
@Composable
fun EventCard(
    event: Event,
    courtLocation: String,
    courtType: String,
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
        else               -> Color(0xFF4F7AA3)
    }

    val occupancyFraction = if (event.maxCapacity > 0)
        event.currentParticipants.toFloat() / event.maxCapacity.toFloat()
    else 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F0F8)
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
                    color = Color(0xFF1C2B3A),
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
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF57C00),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
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
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            //Pista — icona dinàmica segons el tipus de pista
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = courtTypeIcon(courtType),
                    contentDescription = null,
                    tint = Color(0xFF4F7AA3),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = event.courtName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF4F7AA3),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            //Localització
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF4F7AA3),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = courtLocation,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF4F7AA3),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            //Data i hora
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = Color(0xFF1C2B3A).copy(alpha = 0.45f),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = formatEventDateTime(event.dateTime),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1C2B3A).copy(alpha = 0.65f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            //Organitzador
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF1C2B3A).copy(alpha = 0.45f),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Organitzat per ${event.organizerName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1C2B3A).copy(alpha = 0.55f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFF1C2B3A).copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(12.dp))

            //Participants
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = Color(0xFF1C2B3A).copy(alpha = 0.55f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${event.currentParticipants}/${event.maxCapacity} participants",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1C2B3A)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            //Noms dels participants separats per punt mig
            if (event.participantNames.isNotEmpty()) {
                Text(
                    text = event.participantNames.joinToString(" · "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1C2B3A).copy(alpha = 0.45f),
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
                    .height(7.dp),
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
                            Color(0xFF4F7AA3).copy(alpha = 0.7f)
                        else
                            Color(0xFF4F7AA3),
                        contentColor = Color.White,
                        disabledContainerColor = if (isParticipant)
                            Color(0xFF4F7AA3).copy(alpha = 0.15f)
                        else
                            Color(0xFF1C2B3A).copy(alpha = 0.12f),
                        disabledContentColor = Color(0xFF1C2B3A).copy(alpha = 0.4f)
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
                    // Si l'usuari és l'organitzador, el botó mostra "Eliminar" en lloc d'"Abandonar"
                    val isOrganizer = currentUsername == event.organizerName
                    OutlinedButton(
                        onClick = onLeave,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(
                            text = if (isOrganizer) "Eliminar" else "Abandonar",
                            fontWeight = FontWeight.Bold
                        )
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
 * @param courtType Tipus de la pista, obtingut del mapa de pistes carregades.
 * @param onConfirm Funció que s'executa quan l'usuari confirma la inscripció.
 * @param onDismiss Funció que s'executa quan l'usuari cancel·la.
 *
 * @author Jesús Ramos
 */
@Composable
fun JoinConfirmDialog(
    event: Event,
    courtType: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFE8F0F8),
        icon = {
            Text(text = "🏅", style = MaterialTheme.typography.headlineMedium)
        },
        title = {
            Text(
                text = "Confirmar inscripció",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C2B3A)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "T'estàs apuntant a:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1C2B3A).copy(alpha = 0.65f)
                )
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C2B3A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = courtTypeIcon(courtType),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFF4F7AA3)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = event.courtName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF4F7AA3)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CalendarMonth, contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFF1C2B3A).copy(alpha = 0.45f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = formatEventDateTime(event.dateTime),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF1C2B3A).copy(alpha = 0.65f)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Group, contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color(0xFF1C2B3A).copy(alpha = 0.45f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Queden ${event.maxCapacity - event.currentParticipants} places lliures",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF1C2B3A).copy(alpha = 0.65f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
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
                Text("Cancel·lar", color = Color(0xFF4F7AA3), fontWeight = FontWeight.Medium)
            }
        }
    )
}

/**
 * Diàleg de confirmació d'abandonament o eliminació d'un esdeveniment.
 *
 * @author Jesús Ramos
 *
 * @param event Esdeveniment que es vol abandonar o eliminar.
 * @param currentUsername Nom de l'usuari autenticat, per determinar si és l'organitzador.
 * @param onConfirm Funció que s'executa quan l'usuari confirma l'acció.
 * @param onDismiss Funció que s'executa quan l'usuari cancel·la.
 */
@Composable
fun LeaveConfirmDialog(
    event: Event,
    currentUsername: String?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    //Si l'usuari és l'únic participant, abandonar eliminarà l'event
    val isOnlyParticipant = event.currentParticipants == 1
    // Si l'usuari és l'organitzador, l'acció sempre elimina l'event
    val isOrganizer = currentUsername == event.organizerName

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFE8F0F8),
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
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C2B3A)
            )
        },
        text = {
            Text(
                text = when {
                    isOnlyParticipant ->
                        "Ets l'únic participant de \"${event.title}\". Si abandones, l'esdeveniment s'eliminarà definitivament. Vols continuar?"
                    isOrganizer ->
                        "Ets l'organitzador de \"${event.title}\". Si l'elimines, tots els participants perdran la inscripció. Vols continuar?"
                    else ->
                        "Estàs a punt d'abandonar \"${event.title}\". Segur que vols continuar?"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF1C2B3A).copy(alpha = 0.75f)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(
                    text = when {
                        isOrganizer -> "Eliminar"
                        isOnlyParticipant -> "Eliminar"
                        else -> "Abandonar"
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel·lar", color = Color(0xFF4F7AA3), fontWeight = FontWeight.Medium)
            }
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
        containerColor = Color(0xFFE8F0F8),
        icon = {
            Text(text = "🏟️", style = MaterialTheme.typography.headlineMedium)
        },
        title = {
            Text(
                text = "Nou esdeveniment",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C2B3A)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                //Títol
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; errorMsg = null },
                    label = { Text("Títol de l'esdeveniment", color = Color(0xFF4F7AA3)) },
                    placeholder = { Text("Ex: Torneig de Pàdel") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4F7AA3),
                        unfocusedBorderColor = Color(0xFF1C2B3A).copy(alpha = 0.3f),
                        focusedTextColor = Color(0xFF1C2B3A),
                        unfocusedTextColor = Color(0xFF1C2B3A)
                    )
                )

                // Desplegable: Selecció de pista
                if (courts.isEmpty()) {
                    Text(
                        text = "No s'han pogut carregar les pistes.",
                        style = MaterialTheme.typography.bodyMedium,
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
                            label = { Text("Pista", color = Color(0xFF4F7AA3)) },
                            placeholder = { Text("Selecciona una pista") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = dropdownExpanded
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
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Color(0xFF1C2B3A)
                                            )
                                            Text(
                                                text = "${court.type} · ${court.location} · Capacitat: ${court.capacity}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color(0xFF1C2B3A).copy(alpha = 0.55f)
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
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF4F7AA3)
                    )
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
                            "Seleccionar data i hora",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                //Missatge d'error de validació
                errorMsg?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
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
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4F7AA3),
                    contentColor = Color.White
                )
            ) {
                Text("Crear", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel·lar", color = Color(0xFF4F7AA3), fontWeight = FontWeight.Medium)
            }
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