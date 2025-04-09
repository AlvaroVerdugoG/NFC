package com.example.nfc.view.route

import androidx.compose.runtime.Composable
import com.example.nfc.view.register.SingUpViewModel.SignUpViewModel
import com.example.nfc.view.register.view.SignUpScreen

@Composable
fun RouteSignUp(
    signUpViewModel: SignUpViewModel,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onMessageError: (String) -> Unit
) {
    SignUpScreen(
        signUpViewModel = signUpViewModel,
        onSignUpClick = onSignUpClick,
        onSignInClick = onSignInClick,
        onErrorMessage = onMessageError
    )
}