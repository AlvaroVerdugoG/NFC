package com.example.nfc.view.screen

sealed class Screen(val route: String) {
    data object Login : Screen("Login")
    data object Register : Screen("Register")
    data object Home : Screen("Home")
    data object Profile : Screen("Profile")
    data object ForgetPassword : Screen("ForgerPassword")
    data object Delete : Screen("Delete")
    data object FAQ : Screen("Faq")
    data object About : Screen("About")
}