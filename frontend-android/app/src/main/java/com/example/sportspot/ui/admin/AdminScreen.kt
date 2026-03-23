package com.example.sportspot.ui.admin

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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

/**
 * Pantalla d'administració.
 *
 * Mostra informació bàsica de l'usuari administrador i permet tancar la sessió.
 *
 * @author Jesús Ramos
 *
 * @param viewModel ViewModel que proveeix el token i les accions de logout.
 * @param onLogout Funció que s'executa quan el logout ha estat correcte
 */
@SuppressLint("ContextCastToActivity")
@Composable
fun AdminScreen(
    viewModel: AdminViewModel = viewModel(
        factory = AdminViewModel.provideFactory(LocalContext.current)
    ),
    onLogout: () -> Unit
) {
    // Observem el token des del ViewModel; pot ser null si no hi ha sessió.
    val token by viewModel.token.collectAsState()

    // Scope per cridar suspensions
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Icona d'usuari amb fons circular (visual més net)
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Admin Icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Títol de la pantalla
        Text(
            text = "Administrador",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Etiqueta i valor del token
        Text(
            text = "Token",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = token ?: "Sense token",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Card amb contingut d'administració (placeholder)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Zona d'administració",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Per afegir més endavant...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botó per tancar sessió (CTA estilitzat amb el tema)
        Button(
            onClick = {
                coroutineScope.launch {
                    // Es crida al ViewModel per fer logout; si té èxit, es crida onLogout
                    val success = viewModel.logout()
                    if (success) {
                        onLogout()
                    } else {
                        println("Error al tancar la sessió")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text("Tancar sessió")
        }

        // Botó per sortir de l'app
        val activity = LocalContext.current as? android.app.Activity

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { activity?.finish() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text("Sortir")
        }
    }
}
