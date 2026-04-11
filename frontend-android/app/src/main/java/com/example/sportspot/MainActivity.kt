package com.example.sportspot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sportspot.ui.admin.AdminScreen
import com.example.sportspot.ui.client.ClientScreen
import com.example.sportspot.ui.login.LoginScreen
import com.example.sportspot.ui.navigation.AppRoute
import com.example.sportspot.ui.profile.ProfileScreen
import com.example.sportspot.ui.session.SessionViewModel
import com.example.sportspot.ui.theme.SportSpotTheme

/**
 * Activitat principal de l'aplicació.
 *
 * Conté la configuració de Compose i la navegació entre pantalles (login, admin, client).
 * S'encarrega de decidir a quina pantalla navegar després d'un login segons el rol de l'usuari.
 *
 * @author Jesús Ramos
 *
 */
class MainActivity : ComponentActivity() {

    /**
     * Punt d'entrada de l'activitat.
     *
     * S'activa l'edge-to-edge, es configura el tema i es crea el NavController per gestionar
     * la navegació entre les rutes definides a l'aplicació.
     *
     * @author Jesús Ramos
     *
     * @param savedInstanceState Estat prèviament guardat de l'activitat (pot ser null).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SportSpotTheme {

                // NavController per gestionar la navegació entre pantalles
                val navController = rememberNavController()

                // Aquest ViewModel llegeix el token guardat al DataStore i permet saber si
                // l'usuari ja havia iniciat sessió abans de tancar l'app.
                val sessionVm: SessionViewModel = viewModel(
                    factory = SessionViewModel.provideFactory(this)
                )

                // Observem el token com un State
                val token by sessionVm.token.collectAsState()

                val role by sessionVm.role.collectAsState()

                // Determinem la pantalla inicial de l'aplicació segons si hi ha sessió activa
                val startDestination = when {
                    token == null -> AppRoute.Login.route
                    role == "admin" -> AppRoute.Admin.route
                    role == "user" -> AppRoute.Client.route
                    else -> AppRoute.Login.route
                }

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {

                    // Pantalla de login. Quan l'usuari es logueja, es comprova el rol
                    // i es navega a la pantalla corresponent.
                    composable(AppRoute.Login.route) {
                        LoginScreen { user ->
                            if (user.role == "admin") {
                                navController.navigate(AppRoute.Admin.route)
                            } else {
                                navController.navigate(AppRoute.Client.route)
                            }
                        }
                    }

                    // Pantalla d'administració. Al fer logout es torna a la pantalla de login
                    // i s'esborra l'historial de navegació per evitar tornar enrere.
                    composable(AppRoute.Admin.route) {
                        AdminScreen(
                            onLogout = {
                                navController.navigate(AppRoute.Login.route) {
                                    popUpTo(0)
                                }
                            }
                        )
                    }

                    // Pantalla del client. Com en admin, al fer logout es torna al login
                    // i s'esborra l'historial de navegació per evitar tornar enrere.
                    composable(AppRoute.Client.route) {
                        ClientScreen(
                            onLogout = {
                                navController.navigate(AppRoute.Login.route) {
                                    popUpTo(0)
                                }
                            },
                            onNavigateToProfile = {   // TEA3 - nova navegació
                                navController.navigate(AppRoute.Profile.route)
                            }
                        )
                    }

                    // TEA3 - Pantalla de perfil. Al guardar o tornar, es torna a la pantalla del client.
                    composable(AppRoute.Profile.route) {
                        ProfileScreen(
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }


                }
            }
        }
    }
}