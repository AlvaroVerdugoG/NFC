package com.example.nfc.view.login.view

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.dimensionResource
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
    onErrorMessage: (String) -> Unit
) {
    val defaultPadding = dimensionResource(R.dimen.defaultPadding)
    val itemSpacing = dimensionResource(R.dimen.itemSpacing)
    var email by remember {
        mutableStateOf("")
    }
    var userPassword by remember {
        mutableStateOf("")
    }
    var checked by remember {
        mutableStateOf(false)
    }
    val uiState by loginViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = uiState.isSigIn) {
        if (uiState.isSigIn) {
            onLoginClick()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = defaultPadding),
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
            value = email,
            onValueChange = { email = it },
            labelText = "Email",
            leadingIcon = Icons.Default.Person,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(itemSpacing))
        LoginTextField(
            value = userPassword,
            onValueChange = { userPassword = it },
            labelText = "Password",
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
                    onCheckedChange = { checked = it }
                )
                Text(
                    text = "Remember me"
                )
            }
            TextButton(onClick = {}) {
                Text("Forgot Password?")
            }
        }
        Spacer(Modifier.height(itemSpacing))
        Button(onClick = {
            val user = User(email = email, password = userPassword)
            loginViewModel.signIn(user)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Login")
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }

        }
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Doesn't have an account?")
            TextButton(onClick = registerClick) {
                Text("Register")
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