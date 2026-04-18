package com.example.sportspot.ui.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportspot.R
import kotlinx.coroutines.delay

/**
 * TEA3 - Pantalla de registre d'un nou usuari client.
 *
 * Mostra un formulari per crear un nou compte de tipus client.
 *
 * @author Jesús Ramos
 *
 * @param onRegisterSuccess Funció que s'executa quan el registre és correcte.
 * @param onBack Funció que s'executa quan l'usuari vol tornar enrere.
 * @param viewModel ViewModel que proveeix l'estat i les accions de registre.
 */
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModel.provideFactory()
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Quan el registre és correcte, redirigim al login
    LaunchedEffect(uiState) {
        if (uiState is RegisterUiState.Success) {
            delay(1500L)
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Logo SportSpot
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "SportSpot logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Crear compte",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp)
        )

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
            label = { Text("Contrasenya") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.register(name, password, email) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text("Crear compte")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onBack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Tornar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrem missatges segons l'estat actual
        when (uiState) {
            is RegisterUiState.Loading -> CircularProgressIndicator()
            is RegisterUiState.Error -> Text(
                text = (uiState as RegisterUiState.Error).message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
            is RegisterUiState.Success -> Text(
                text = "Usuari donat d'alta correctament!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            else -> {}
        }
    }
}