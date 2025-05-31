package com.example.nfc.view.route

import androidx.compose.runtime.Composable
import com.example.nfc.view.forgetPassword.view.ForgetPasswordScreen
import com.example.nfc.view.forgetPassword.viewModel.ForgetPasswordViewModel

@Composable
fun RouteForgetPassword(
    forgetPasswordViewModel: ForgetPasswordViewModel,
    onItsDoneClick: () -> Unit,
    onBackClick: () -> Unit
){
    ForgetPasswordScreen(
        forgetPasswordViewModel = forgetPasswordViewModel,
        onItsDoneClick = onItsDoneClick,
        onBackClick = onBackClick
    )
}