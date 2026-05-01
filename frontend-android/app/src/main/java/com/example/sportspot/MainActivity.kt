package com.example.sportspot

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import com.example.sportspot.ui.admin.AdminCourtsScreen
import com.example.sportspot.ui.admin.AdminScreen
import com.example.sportspot.ui.bookings.MyBookingsScreen
import com.example.sportspot.ui.client.ClientScreen
import com.example.sportspot.ui.courts.CourtDetailScreen
import com.example.sportspot.ui.courts.CourtsScreen
import com.example.sportspot.ui.login.LoginScreen
import com.example.sportspot.ui.navigation.AppRoute
import com.example.sportspot.ui.profile.ProfileScreen
import com.example.sportspot.ui.register.RegisterScreen
import com.example.sportspot.ui.session.SessionViewModel
import com.example.sportspot.ui.theme.SportSpotTheme
import com.example.sportspot.ui.events.EventsScreen

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
    @RequiresApi(Build.VERSION_CODES.O)
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

                // Observem el token i el rol com un State
                val token by sessionVm.token.collectAsState()
                val role by sessionVm.role.collectAsState()
                android.util.Log.d("SESSION", "token=$token role=$role")

                if (token == SessionViewModel.LOADING || role == SessionViewModel.LOADING) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    val startDestination = when {
                        token.isNullOrBlank() -> AppRoute.Login.route
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
                            LoginScreen(
                                onLoginSuccess = { user ->
                                    if (user.role == "admin") {
                                        navController.navigate(AppRoute.Admin.route)
                                    } else {
                                        navController.navigate(AppRoute.Client.route)
                                    }
                                },
                                onNavigateToRegister = {
                                    navController.navigate(AppRoute.Register.route)
                                }
                            )
                        }
                        // Pantalla d'administració. Al fer logout es torna a la pantalla de login
                        // i s'esborra l'historial de navegació per evitar tornar enrere.
                        composable(AppRoute.Admin.route) {
                            AdminScreen(
                                onLogout = {
                                    navController.navigate(AppRoute.Login.route) {
                                        popUpTo(0)
                                    }
                                },
                                onNavigateToAdminCourts = {
                                    navController.navigate(AppRoute.AdminCourts.route)
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
                                onNavigateToProfile = {
                                    navController.navigate(AppRoute.Profile.route)
                                },
                                onNavigateToCourts = {
                                    navController.navigate(AppRoute.Courts.route)
                                },
                                onNavigateToMyBookings = {
                                    navController.navigate(AppRoute.MyBookings.route)
                                },
                                onNavigateToEvents = {
                                    navController.navigate(AppRoute.Events.route)
                                }
                            )
                        }
                        // TEA3 - Pantalla de perfil. Al guardar o tornar, es torna a la pantalla del client.
                        composable(AppRoute.Profile.route) {
                            ProfileScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                                onDeleteAccount = {
                                    navController.navigate(AppRoute.Login.route) {
                                        popUpTo(0)
                                    }
                                }
                            )
                        }
                        // TEA3 - Pantalla de registre. Al completar, torna al login.
                        composable(AppRoute.Register.route) {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    navController.navigate(AppRoute.Login.route) {
                                        popUpTo(0)
                                    }
                                },
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(AppRoute.Courts.route) {
                            CourtsScreen(
                                onCourtSelected = { courtId ->
                                    navController.navigate(AppRoute.CourtDetail.createRoute(courtId))
                                },
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(AppRoute.CourtDetail.route) { backStackEntry ->
                            val courtId = backStackEntry.arguments?.getString("courtId")?.toLongOrNull() ?: return@composable
                            CourtDetailScreen(
                                courtId = courtId,
                                onBack = { navController.popBackStack() },
                                onBookingConfirmed = {
                                    navController.navigate(AppRoute.Courts.route) {
                                        popUpTo(AppRoute.Courts.route) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(AppRoute.MyBookings.route) {
                            MyBookingsScreen(
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable(AppRoute.AdminCourts.route) {
                            AdminCourtsScreen(
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable(AppRoute.Events.route) {
                            EventsScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}