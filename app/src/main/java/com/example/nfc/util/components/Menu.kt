// ProfileDropdownMenu.kt
package com.example.nfc.view.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.nfc.R

@Composable
fun Menu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onFAQClick: () -> Unit,
    onAboutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
) {
    DropdownMenu(expanded = expanded,
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight(),
        onDismissRequest = onDismissRequest,
        offset = DpOffset(x = (-0).dp, y = 0.dp)) {
        DropdownMenuItem(text = { Text(stringResource(R.string.faq)) }, onClick = onFAQClick)
        DropdownMenuItem(text = { Text(stringResource(R.string.about)) }, onClick = onAboutClick)
        DropdownMenuItem(text = { Text(stringResource(R.string.delete_account)) },
            onClick = onDeleteAccountClick)
    }
}
