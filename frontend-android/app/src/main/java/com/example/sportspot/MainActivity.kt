package com.example.sportspot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sportspot.domain.model.User
import com.example.sportspot.ui.admin.AdminScreen
import com.example.sportspot.ui.client.ClientScreen
import com.example.sportspot.ui.login.LoginScreen
import com.example.sportspot.ui.navigation.AppRoute
import com.example.sportspot.ui.theme.SportSpotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SportSpotTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = AppRoute.Login.route
                ) {

                    composable(AppRoute.Login.route) {
                        LoginScreen { user ->
                            if (user.role == "ADMIN") {
                                navController.navigate(AppRoute.Admin.route)
                            } else {
                                navController.navigate(AppRoute.Client.route)
                            }
                        }
                    }

                    composable(AppRoute.Admin.route) {
                        AdminScreen(
                            onLogout = {
                                navController.navigate(AppRoute.Login.route) {
                                    popUpTo(0)   // Limpia el backstack
                                }
                            }
                        )
                    }

                    composable(AppRoute.Client.route) {
                        ClientScreen(
                            onLogout = {
                                navController.navigate(AppRoute.Login.route) {
                                    popUpTo(0)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}