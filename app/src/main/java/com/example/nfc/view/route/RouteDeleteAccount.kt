package com.example.nfc.view.route

import androidx.compose.runtime.Composable
import com.example.nfc.view.deleteAccount.view.DeleteAccountScreen
import com.example.nfc.view.deleteAccount.viewModel.DeleteAccountViewModel

@Composable
fun RouteDeleteAccount(deleteAccountViewModel: DeleteAccountViewModel,
                       onDeleteClick: () -> Unit,
                       onBackClick: () -> Unit) {
    DeleteAccountScreen(deleteAccountViewModel = deleteAccountViewModel,
        onDeleteClick = onDeleteClick,
        onBackClick = onBackClick)
}