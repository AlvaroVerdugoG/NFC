package com.example.nfc.view.route

import androidx.compose.runtime.Composable
import com.example.nfc.view.home.view.HomeScreen
import com.example.nfc.view.home.viewModel.HomeViewModel

@Composable
fun RouteHome(homeViewModel: HomeViewModel,
              onAccountClicked: () -> Unit,
              onFAQClick: () -> Unit,
              onAboutClick: () -> Unit,
              onDeleteAccountClick: () -> Unit) {
    HomeScreen(homeViewModel = homeViewModel,
        onAccountClicked = onAccountClicked,
        onFAQClick = onFAQClick,
        onDeleteAccountClick = onDeleteAccountClick,
        onAboutClick = onAboutClick)
}