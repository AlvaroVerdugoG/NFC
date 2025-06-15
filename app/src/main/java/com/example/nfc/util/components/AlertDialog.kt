package com.example.nfc.util.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.nfc.R

@Composable
fun AlertDialogs(onDismissClick: () -> Unit,
                 title: String,
                 text: String,
                 confirmButtonClick: () -> Unit,
                 confirmButtonText: String,
                 dismissButtonClick: () -> Unit,
                 dismissButtonText: String) {
    AlertDialog(
        onDismissRequest = onDismissClick,
        title = { Text(text = title) },
        text = {
            Text(text = text)
        },
        confirmButton = {
            TextButton(onClick = confirmButtonClick) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = dismissButtonClick) {
                Text(text = dismissButtonText)
            }
        },
    )
}