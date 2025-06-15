package com.example.nfc.view.aboutInfo.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nfc.R
import com.example.nfc.view.aboutInfo.viewModel.AboutInfoViewModel
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutInfoScreen(aboutInfoViewModel: AboutInfoViewModel, onBackClick: () -> Unit) {
    val context = LocalContext.current
    val uiState by aboutInfoViewModel.uiState.collectAsState()

    aboutInfoViewModel.getAboutInfo(context)

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(R.string.about),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.forget_password))
                }
            },
        )
    }) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.name_of_the_app) + " " + uiState.aboutInfo?.appName)
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.version) + " " + uiState.aboutInfo?.version)
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.develop) + " " + uiState.aboutInfo?.develop)
        }
    }
}