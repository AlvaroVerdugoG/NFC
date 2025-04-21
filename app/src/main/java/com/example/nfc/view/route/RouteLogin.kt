package com.example.nfc.view.route

import androidx.compose.runtime.Composable
import com.example.nfc.view.login.view.LoginScreen
import com.example.nfc.view.login.viewModel.LoginViewModel

@Composable
fun RouteLogIn(
    loginViewModel: LoginViewModel,
    onLoginClick: () -> Unit,
    registerClick: () -> Unit,
    onMessageError: (String) -> Unit
) {
    LoginScreen(
        loginViewModel = loginViewModel,
        onLoginClick = onLoginClick,
        registerClick = registerClick,
        onErrorMessage = onMessageError
    )
}