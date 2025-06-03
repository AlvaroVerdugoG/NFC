package com.example.nfc.view.forgetPassword.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nfc.R
import com.example.nfc.util.components.LoginTextField
import com.example.nfc.view.forgetPassword.viewModel.ForgetPasswordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPasswordScreen(
    forgetPasswordViewModel: ForgetPasswordViewModel,
    onItsDoneClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    val uiState by forgetPasswordViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.forget_password)
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.instruction_info),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            LoginTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                labelText = stringResource(R.string.email),
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    forgetPasswordViewModel.resetPassword(context, email)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.reset_password))
            }
            if (uiState.isSendIt) {
                AlertDialog(
                    onDismissRequest = {
                        forgetPasswordViewModel.resetItsSendIt()
                    },
                    title = { Text(text = stringResource(R.string.reset_password)) },
                    text = {
                        Text(
                            text = stringResource(R.string.instruction_text)
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onItsDoneClick()
                                forgetPasswordViewModel.resetItsSendIt()
                            }
                        ) {
                            Text(text = stringResource(R.string.its_done))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                forgetPasswordViewModel.resetPassword(context, email)
                            }
                        ) {
                            Text(text = stringResource(R.string.resend_email))
                        }
                    },
                )
            }
        }
    }
}
