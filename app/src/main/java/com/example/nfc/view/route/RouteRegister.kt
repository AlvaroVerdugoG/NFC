package com.example.nfc.view.route

import androidx.compose.runtime.Composable
import com.example.nfc.view.register.viewModel.RegisterViewModel
import com.example.nfc.view.register.view.RegisterScreen

@Composable
fun RouteRegister(registerViewModel: RegisterViewModel,
                  registerClick: () -> Unit,
                  onSignInClick: () -> Unit,){
    RegisterScreen(registerViewModel = registerViewModel,
        registerClick = registerClick,
        onSignInClick = onSignInClick)
}