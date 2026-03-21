package com.example.sportspot.ui.navigation

sealed class AppRoute(val route: String) {
    object Login : AppRoute("login")
    object Admin : AppRoute("admin")
    object Client : AppRoute("client")
}