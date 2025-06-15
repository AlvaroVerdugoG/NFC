package com.example.nfc.view.route

import androidx.compose.runtime.Composable
import com.example.nfc.view.aboutInfo.view.AboutInfoScreen
import com.example.nfc.view.aboutInfo.viewModel.AboutInfoViewModel

@Composable
fun RouteAbout(aboutInfoViewModel: AboutInfoViewModel, onBackClick: () -> Unit) {
    AboutInfoScreen(aboutInfoViewModel = aboutInfoViewModel, onBackClick = onBackClick)
}