package com.example.nfc.view.route

import androidx.compose.runtime.Composable
import com.example.nfc.view.login.view.LoginScreen
import com.example.nfc.view.login.viewModel.LoginViewModel
import com.example.nfc.view.screen.Screen

@Composable
fun RouteLogIn(
    loginViewModel: LoginViewModel,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onMessageError: (String) -> Unit
) {
    LoginScreen(
        loginViewModel = loginViewModel,
        onLoginClick = onLoginClick,
        onSignUpClick = onSignUpClick,
        onErrorMessage = onMessageError
    )
}