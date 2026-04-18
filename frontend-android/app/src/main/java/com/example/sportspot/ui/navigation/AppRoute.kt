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

    /** Ruta de la pantalla de perfil del client. */
    object Profile : AppRoute("profile")

    /** Ruta de la pantalla de registre. */
    object Register : AppRoute("register")

    /** Ruta de la pantalla de llistat de pistes. */
    object Courts : AppRoute("courts")

    /** Ruta de la pantalla de detall d'una pista. Inclou l'ID com a paràmetre. */
    object CourtDetail : AppRoute("court/{courtId}") {
        fun createRoute(courtId: Long) = "court/$courtId"
    }
    /**R uta de la pantalla de les reserves de l'usuari. */
    object MyBookings : AppRoute("my-bookings")
    /** Ruta de la pantalla de gestió de pistes per a l'administrador. */
    object AdminCourts : AppRoute("admin-courts")
}