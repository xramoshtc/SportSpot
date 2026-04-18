package com.example.sportspot.ui.profile

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * TEA3 - Pantalla de perfil del client.
 *
 * Carrega les dades de l'usuari autenticat i permet modificar-les.
 *
 * @author Jesús Ramos
 *
 * @param onBack Funció que s'executa quan l'usuari vol tornar enrere.
 * @param viewModel ViewModel que proveeix l'estat i les accions del perfil.
 */
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onDeleteAccount: () -> Unit,
    viewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModel.provideFactory(LocalContext.current)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    // Camps locals del formulari
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Nom original per enviar a la URL, no canvia encara que l'usuari editi el camp
    var originalName by remember { mutableStateOf("") }

    // Ccontrola la visibilitat del diàleg de confirmació
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Quan l'estat passa a Success per primera vegada, omplim els camps amb les dades actuals
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

    // Carreguem el perfil en obrir la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Icona d'usuari
        Box(
            modifier = Modifier
                .size(96.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "El meu perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Formulari, només visible quan tenim les dades carregades
        when (uiState) {

            is ProfileUiState.Loading -> {
                CircularProgressIndicator()
            }

            is ProfileUiState.LoggedOut -> {
                CircularProgressIndicator()
            }

            is ProfileUiState.Idle -> {
                // No fem res, LaunchedEffect ja ha llançat loadProfile()
            }

            is ProfileUiState.Deleted -> {
                // No fem res, LaunchedEffect ja ha llançat loadProfile()
            }

            is ProfileUiState.Error -> {
                Text(
                    text = "Error: ${(uiState as ProfileUiState.Error).message}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is ProfileUiState.Success -> {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom d'usuari") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correu electrònic") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Nova contrasenya") },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Deixa'l buit per no modificar la contrasenya",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Botó de guardar canvis
                Button(
                    onClick = {
                        Log.d("ProfileScreen", "originalName: $originalName name: $name")
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
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Guardar canvis")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botó per tornar enrere
                Button(
                    onClick = { onBack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text("Tornar")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botó per eliminar el compte
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
                    Text("Eliminar compte")
                }

                // Diàleg de confirmació abans d'eliminar
                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = {
                            Text("Eliminar compte")
                        },
                        text = {
                            Text("Estàs segur que vols eliminar el teu compte? Aquesta acció no es pot desfer.")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDeleteDialog = false
                                    viewModel.deleteUser(originalName)
                                }
                            ) {
                                Text(
                                    "Eliminar",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showDeleteDialog = false }
                            ) {
                                Text("Cancel·lar")
                            }
                        }
                    )
                }
            }
        }
    }
}