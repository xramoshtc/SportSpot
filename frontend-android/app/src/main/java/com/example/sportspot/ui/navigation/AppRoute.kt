package com.example.sportspot.ui.navigation

/**
 * Rutes de l'aplicació per a la navegació.
 *
 * Cada objecte representa una ruta única que s'utilitza amb el NavController.
 *
 * @author Jesús Ramos
 *
 * @property route Cadena que identifica la ruta (s'utilitza per navegar).
 */
sealed class AppRoute(val route: String) {
    /** Ruta de la pantalla de login. */
    object Login : AppRoute("login")

    /** Ruta de la pantalla d'administració. */
    object Admin : AppRoute("admin")

    /** Ruta de la pantalla del client. */
    object Client : AppRoute("client")

    /** TEA3 - Ruta de la pantalla de perfil del client. */
    object Profile : AppRoute("profile")
}