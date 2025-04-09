package com.example.nfc.view.route

import androidx.compose.runtime.Composable
import com.example.nfc.view.error.view.ErrorScreen
import com.example.nfc.view.home.view.HomeScreen

@Composable
fun RouteError(errorMessage: String) {
    ErrorScreen(errorMessage = errorMessage)
}