package com.example.nfc.view.deleteAccount.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nfc.R
import com.example.nfc.util.components.DeleteReasons
import com.example.nfc.util.components.TextField
import com.example.nfc.view.deleteAccount.viewModel.DeleteAccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountScreen(deleteAccountViewModel: DeleteAccountViewModel,
                        onDeleteClick: () -> Unit,
                        onBackClick: () -> Unit) {
    val context = LocalContext.current
    val uiState by deleteAccountViewModel.uiState.collectAsState()
    val reasonOptions = listOf(
        context.getString(R.string.not_satisfied),
        context.getString(R.string.security_issues),
        context.getString(R.string.alternative),
        context.getString(R.string.others),
    )
    val selectedOptions = remember { mutableStateMapOf<String, Boolean>() }
    reasonOptions.forEach { option ->
        if (selectedOptions[option] == null) {
            selectedOptions[option] = false
        }
    }
    var additionalOptions by remember { mutableStateOf("") }

    LaunchedEffect(key1 = uiState.isDelete) {
        if (uiState.isDelete) {
            onDeleteClick()
            deleteAccountViewModel.resetVariables()
        }
    }
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(R.string.delete_account),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)
            },
            navigationIcon = {
                IconButton(onClick = { onBackClick() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.account_icon))
                }
            },
        )
    }) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.reasons))
            Spacer(modifier = Modifier.height(16.dp))
            reasonOptions.forEach { option ->
                DeleteReasons(selectedOptions[option] ?: false, option) {
                    selectedOptions[option] = !selectedOptions[option]!!
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
            if (selectedOptions[context.getString(R.string.others)] == true) {
                TextField(value = additionalOptions,
                    onValueChange = { additionalOptions = it },
                    labelText = stringResource(R.string.details),
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                val reasons = selectedOptions.filter { it.value }.keys.toMutableList()
                if (additionalOptions.isNotEmpty()) {
                    reasons.add(additionalOptions)
                }
                deleteAccountViewModel.saveDeleteAccountReason(reasons, context)
            }, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.confirm_selections))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                deleteAccountViewModel.deleteAccount(context)
            }, enabled = uiState.isReasonsSelected, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.delete_account))
            }
        }

    }
}
