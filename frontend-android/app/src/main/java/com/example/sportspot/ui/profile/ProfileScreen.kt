package com.example.sportspot.ui.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
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

/**
 * Pantalla de perfil del client.
 *
 * Carrega les dades de l'usuari autenticat i permet modificar-les.
 *
 * @author Jesús Ramos
 *
 * @param onBack Funció que s'executa quan l'usuari vol tornar enrere.
 * @param onDeleteAccount Funció que s'executa quan el compte ha estat eliminat.
 * @param viewModel ViewModel que proveeix l'estat i les accions del perfil.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onDeleteAccount: () -> Unit,
    viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModel.provideFactory(LocalContext.current)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var originalName by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is ProfileUiState.Success && originalName.isNotEmpty()) {
            snackbarHostState.showSnackbar("Perfil actualitzat correctament!")
        }
    }

    LaunchedEffect(uiState) {
        when {
            uiState is ProfileUiState.Deleted -> onDeleteAccount()
            uiState is ProfileUiState.LoggedOut -> onDeleteAccount()
            uiState is ProfileUiState.Success && originalName.isEmpty() -> {
                val profile = (uiState as ProfileUiState.Success).profile
                name = profile.name
                email = profile.email
                originalName = profile.name
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "El meu perfil",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Avatar circular
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4F7AA3)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (uiState) {

                is ProfileUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF4F7AA3))
                    }
                }

                is ProfileUiState.LoggedOut -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF4F7AA3))
                    }
                }

                is ProfileUiState.Idle -> {}

                is ProfileUiState.Deleted -> {}

                is ProfileUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No s'ha pogut carregar el perfil",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.loadProfile() },
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

                is ProfileUiState.Success -> {

                    // Targeta del formulari
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE8F0F8)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Dades del compte",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1C2B3A)
                            )

                            HorizontalDivider(
                                color = Color(0xFF1C2B3A).copy(alpha = 0.1f)
                            )

                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Nom d'usuari", color = Color(0xFF4F7AA3), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)},
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
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Correu electrònic", color = Color(0xFF4F7AA3), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold) },
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
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Nova contrasenya", color = Color(0xFF4F7AA3), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4F7AA3),
                                    unfocusedBorderColor = Color(0xFF1C2B3A).copy(alpha = 0.3f),
                                    focusedTextColor = Color(0xFF1C2B3A),
                                    unfocusedTextColor = Color(0xFF1C2B3A)
                                )
                            )
                            Text(
                                text = "Deixa'l buit per no modificar la contrasenya",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1C2B3A).copy(alpha = 0.45f),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Botó guardar canvis
                    Button(
                        onClick = {
                            viewModel.updateUser(
                                currentName = originalName,
                                newName = name,
                                newPassword = password,
                                newEmail = email
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4F7AA3),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Guardar canvis", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botó eliminar compte
                    Button(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Text("Eliminar compte", fontWeight = FontWeight.Bold)
                    }

                    // Diàleg de confirmació d'eliminació
                    if (showDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteDialog = false },
                            containerColor = Color(0xFFE8F0F8),
                            title = {
                                Text(
                                    text = "Eliminar compte",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1C2B3A)
                                )
                            },
                            text = {
                                Text(
                                    text = "Estàs segur que vols eliminar el teu compte? Aquesta acció no es pot desfer.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF1C2B3A).copy(alpha = 0.75f)
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showDeleteDialog = false
                                        viewModel.deleteUser(originalName)
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
                                TextButton(onClick = { showDeleteDialog = false }) {
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
}