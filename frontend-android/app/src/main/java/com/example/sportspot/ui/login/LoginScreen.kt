package com.example.sportspot.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportspot.domain.model.User
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.CircularProgressIndicator
import com.example.sportspot.R
import androidx.compose.ui.text.input.PasswordVisualTransformation

/**
 * Pantalla de login senzilla.
 *
 * Mostra dos camps (usuari i contrasenya), un botó per iniciar sessió i
 * gestiona l'estat de la UI a través del ViewModel.
 *
 * @author Jesús Ramos
 *
 * @param onLoginSuccess Funció que s'executa quan l'inici de sessió és correcte.
 *        Rep un objecte [User] amb la informació de la sessió.
 * @param onNavigateToRegister Funció per navegar a la pantalla de registre.
 */
@SuppressLint("ContextCastToActivity")
@Composable
fun LoginScreen(
    onLoginSuccess: (User) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // Context actual per crear el ViewModel amb la factory
    val context = LocalContext.current

    // Creem o obtenim el ViewModel amb la factory que necessita el context
    val vm: LoginViewModel = viewModel(
        factory = LoginViewModel.provideFactory(context)
    )

    // Observem l'estat de la UI exposat pel ViewModel
    val state by vm.uiState.collectAsState()

    // Estats locals per als camps de text
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Logo SportSpot
        Image(
            painter = painterResource(id = R.drawable.logo_complet),
            contentDescription = "SportSpot logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp)
                .padding(bottom = 24.dp)
        )

        // Camp per introduir l'usuari
        OutlinedTextField(
            value = user,
            onValueChange = { user = it },
            label = { Text("Usuari") },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Camp per introduir la contrasenya
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contrasenya") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botó que demana al ViewModel fer el login
        Button(
            onClick = { vm.login(user, password) },
            enabled = state !is LoginUiState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text("Iniciar sessió")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botó per navegar a la pantalla de registre
        TextButton(
            onClick = { onNavigateToRegister() }
        ) {
            Text(
                text = "Encara no tens compte? Registra't",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Mostrem missatges segons l'estat actual
        when (state) {
            is LoginUiState.Loading -> {
                Spacer(modifier = Modifier.height(12.dp))
                CircularProgressIndicator()
            }
            is LoginUiState.Error -> {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = (state as LoginUiState.Error).message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
            is LoginUiState.Success -> {
                val loggedUser = (state as LoginUiState.Success).user
                onLoginSuccess(loggedUser)
            }
            LoginUiState.Idle -> {}
        }

        // Botó per sortir de l'app
        val activity = LocalContext.current as? android.app.Activity

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                activity?.finish() },
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
