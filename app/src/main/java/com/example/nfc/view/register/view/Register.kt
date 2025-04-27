package com.example.nfc.view.register.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nfc.R
import com.example.nfc.util.components.LoginTextField
import com.example.nfc.view.register.RegisterViewModel.RegisterViewModel

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    registerClick: () -> Unit,
    onSignInClick: () -> Unit,
    onErrorMessage: (String) -> Unit
) {
    val defaultPadding = dimensionResource(R.dimen.defaultPadding)
    val itemSpacing = dimensionResource(R.dimen.itemSpacing)

    var firstName by remember {
        mutableStateOf("")
    }
    var lastName by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var confirmPassword by remember {
        mutableStateOf("")
    }
    var passwordError by remember {
        mutableStateOf("")
    }
    val uiState by registerViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState.isSigIn) {
        if (uiState.isSigIn) {
            registerClick()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = uiState.errorMessage.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = uiState.errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Red
            )
        }
        LoginTextField(
            value = firstName,
            onValueChange = { firstName = it },
            labelText = "First Name",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(itemSpacing))
        LoginTextField(
            value = lastName,
            onValueChange = { lastName = it },
            labelText = "Last Name",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(itemSpacing))
        LoginTextField(
            value = email,
            onValueChange = { email = it },
            labelText = "Email",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(itemSpacing))
        LoginTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = registerViewModel.checkPassword(password, confirmPassword)
            },
            labelText = "Password",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            isPassword = true
        )
        Spacer(Modifier.height(itemSpacing))
        LoginTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                passwordError = registerViewModel.checkPassword(password, confirmPassword)
            },
            labelText = "Confirm Password",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            isPassword = true
        )
        AnimatedVisibility(
            visible = passwordError.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = passwordError,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
        Spacer(Modifier.height(itemSpacing))
        Button(
            onClick = {
                    registerViewModel.register(email = email, password = password)
            },
            enabled = passwordError.isEmpty() && email.isNotEmpty()
                    && firstName.isNotEmpty() && lastName.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }else {
                Text("Register")
            }
        }
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text("Already have an account?")
            TextButton(onClick = onSignInClick) {
                Text("Sign in")
            }
        }
    }

}
/*
@Preview(showSystemUi = true)
@Composable
fun SignUpPreview(){
    NFCTheme {
        SignUpScreen(onSignInClick = {},
            onSignUpClick = {})
    }
}*/