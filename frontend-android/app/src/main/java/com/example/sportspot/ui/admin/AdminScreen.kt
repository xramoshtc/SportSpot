package com.example.sportspot.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun AdminScreen(
    viewModel: AdminViewModel = viewModel(
        factory = AdminViewModel.provideFactory(LocalContext.current)
    ),
    onLogout: () -> Unit
)
 {
    val token by viewModel.token.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Admin Icon",
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Administrador",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Token:",
            style = MaterialTheme.typography.labelLarge
        )

        Text(
            text = token ?: "Sin token",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Zona d'administració",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Per afegir més endavant...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val coroutineScope = rememberCoroutineScope()

        Button(
            onClick = {
                coroutineScope.launch {
                    val success = viewModel.logout()
                    if (success) {
                        onLogout()
                    } else {
                        // Aquí podríamos mostrar un Snackbar más adelante
                        println("Error al tancar la sessió")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tancar sessió")
        }

    }
}
