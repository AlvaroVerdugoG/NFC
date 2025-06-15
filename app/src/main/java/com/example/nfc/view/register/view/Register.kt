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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.nfc.R
import com.example.nfc.model.User
import com.example.nfc.util.components.AlertDialogs
import com.example.nfc.util.components.TextField
import com.example.nfc.view.register.viewModel.RegisterViewModel

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    registerClick: () -> Unit,
    onSignInClick: () -> Unit,
) {
    val defaultPadding = dimensionResource(R.dimen.defaultPadding)
    val itemSpacing = dimensionResource(R.dimen.itemSpacing)

    var user by remember { mutableStateOf(User()) }
    val context = LocalContext.current

    var passwordError by remember {
        mutableStateOf("")
    }
    val uiState by registerViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState.isSigIn) {
        if (uiState.isSigIn) {
            registerClick()
            registerViewModel.resetRegisterStatus()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        AnimatedVisibility(visible = uiState.errorMessage.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()) {
            Text(text = uiState.errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Red)
        }

        Spacer(Modifier.height(itemSpacing))

        TextField(value = user.name,
            onValueChange = { user = user.copy(name = it) },
            labelText = stringResource(R.string.first_name),
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(itemSpacing))

        TextField(value = user.lastName,
            onValueChange = { user = user.copy(lastName = it) },
            labelText = stringResource(R.string.last_name),
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(itemSpacing))

        TextField(value = user.email,
            onValueChange = { user = user.copy(email = it) },
            labelText = stringResource(R.string.email),
            modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(itemSpacing))

        TextField(value = user.password,
            onValueChange = {
                user = user.copy(password = it)
                passwordError =
                    registerViewModel.checkPassword(context, user.password, user.confirmPassword)
            },
            labelText = stringResource(R.string.password),
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            isPassword = true)

        Spacer(Modifier.height(itemSpacing))

        TextField(value = user.confirmPassword,
            onValueChange = {
                user = user.copy(confirmPassword = it)
                passwordError =
                    registerViewModel.checkPassword(context, user.password, user.confirmPassword)
            },
            labelText = stringResource(R.string.confirm_password),
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            isPassword = true)

        AnimatedVisibility(visible = passwordError.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()) {
            Text(text = passwordError,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red)
        }

        Spacer(Modifier.height(itemSpacing))

        Button(onClick = {
            registerViewModel.register(context, user)
        },
            enabled = passwordError.isEmpty() && user.isCorrect(),
            modifier = Modifier.fillMaxWidth()) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                Text(stringResource(R.string.register))
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {

            Text(stringResource(R.string.have_an_account))
            TextButton(onClick = onSignInClick) {
                Text(stringResource(R.string.sign_in))
            }
        }
        if (uiState.confirmEmail) {
            AlertDialogs(onDismissClick = {
                registerViewModel.resetConfirmEmail()
                registerViewModel.resetRegisterStatus()
            },
                title = context.getString(R.string.confirm_email_title),
                text = context.getString(R.string.confirm_email_text),
                confirmButtonClick = {
                    registerViewModel.signIn(context, user)

                },
                confirmButtonText = context.getString(R.string.its_done),
                dismissButtonClick = {
                    registerViewModel.resendVerificationEmail(context)
                },
                dismissButtonText = context.getString(R.string.resend_email))
        }
    }
}