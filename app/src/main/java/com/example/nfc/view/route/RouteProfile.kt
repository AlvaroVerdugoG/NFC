package com.example.nfc.view.route

import androidx.compose.runtime.Composable
import com.example.nfc.view.home.view.HomeScreen
import com.example.nfc.view.profile.View.AccountScreen
import com.example.nfc.view.profile.viewModel.ProfileViewModel


@Composable
fun RouteProfile(
    accountViewModel: ProfileViewModel,
    onSignOutClicked: () -> Unit,
    onBackClick: () -> Unit
) {
    AccountScreen(
        accountViewModel = accountViewModel,
        onSignOutClicked = onSignOutClicked,
        onBackClick = onBackClick
    )
}