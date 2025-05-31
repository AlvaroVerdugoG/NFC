package com.example.nfc.view.login.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import com.example.nfc.util.components.LoginTextField
import com.example.nfc.view.login.viewModel.LoginViewModel


@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onLoginClick: () -> Unit,
    registerClick: () -> Unit,
    onForgetPasswordClick: () -> Unit
) {
    val context = LocalContext.current
    val uiState by loginViewModel.uiState.collectAsState()

    loginViewModel.getUserEmail()
    val defaultPadding = dimensionResource(R.dimen.defaultPadding)
    val itemSpacing = dimensionResource(R.dimen.itemSpacing)
    var user by remember { mutableStateOf(User(email = uiState.email)) }
    var checked by remember {
        mutableStateOf(loginViewModel.getBoolean())
    }


    LaunchedEffect(key1 = uiState.isSigIn) {
        if (uiState.isSigIn) {
            onLoginClick()
            loginViewModel.resetLoginStatus()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(all = defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = stringResource(R.string.welcome_text),
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF004D40)
        )

        Spacer(modifier = Modifier.height(38.dp))

        AnimatedVisibility(
            visible = uiState.errorMessage.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = uiState.errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFD32F2F)
            )
        }
        Spacer(Modifier.height(itemSpacing))
        LoginTextField(
            value = user.email,
            onValueChange = { user = user.copy(email = it) },
            labelText = stringResource(R.string.email),
            leadingIcon = Icons.Default.Person,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(itemSpacing))
        LoginTextField(
            value = user.password,
            onValueChange = { user = user.copy(password = it) },
            labelText = stringResource(R.string.password),
            leadingIcon = Icons.Default.Lock,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            isPassword = true
        )
        Spacer(Modifier.height(itemSpacing))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        loginViewModel.setUserEmailPreferences(user.email, checked)
                    }
                )
                Text(
                    text = stringResource(R.string.remember_me)
                )
            }
            TextButton(onClick = onForgetPasswordClick) {
                Text(stringResource(R.string.forgot_password))
            }
        }
        Spacer(Modifier.height(itemSpacing))
        Button(
            onClick = {
                loginViewModel.signIn(context, user)

            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF0077CC)
            )
        ) {
            Text(stringResource(R.string.sign_in))
            if (uiState.isSigIn) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            }

        }
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.account_question))
            TextButton(onClick = registerClick) {
                Text(stringResource(R.string.register))
            }
        }
    }
}

//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Preview(showSystemUi = true)
//@Composable
//fun LoginScreenPreview() {
//    NFCTheme {
//        Scaffold {
//            LoginScreen(
//                onLoginClick = { }
//            ){}
//        }
//    }
//}