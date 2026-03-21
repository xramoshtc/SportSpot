package com.example.sportspot.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sportspot.domain.model.User
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun LoginScreen(
    onLoginSuccess: (User) -> Unit
) {
    val context = LocalContext.current
    val vm: LoginViewModel = viewModel(
        factory = LoginViewModel.provideFactory(context)
    )

    val state by vm.uiState.collectAsState()

    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {

        TextField(
            value = user,
            onValueChange = { user = it },
            label = { Text("Usuari") }
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contrassenya") }
        )

        Button(onClick = { vm.login(user, password) }) {
            Text("Iniciar sessió")
        }

        when (state) {
            is LoginUiState.Loading -> Text("Carregant...")
            is LoginUiState.Error -> Text("Error: ${(state as LoginUiState.Error).message}")
            is LoginUiState.Success -> {
                val loggedUser = (state as LoginUiState.Success).user
                onLoginSuccess(loggedUser)
            }
            LoginUiState.Idle -> {}
        }
    }
}