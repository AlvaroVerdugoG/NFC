package com.example.nfc.view.home.view

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DensitySmall
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nfc.R
import com.example.nfc.util.components.CardNFC
import com.example.nfc.view.components.Menu
import com.example.nfc.view.home.viewModel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onAccountClicked: () -> Unit,
    onFAQClick: () -> Unit,
    onAboutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    val context =
        LocalContext.current
    val uiState by homeViewModel.uiState.collectAsState()

    homeViewModel.fetchUserData(context)

    Box(
        modifier = Modifier.pointerInput(Unit) {
            detectHorizontalDragGestures { change, dragAmount ->
                if (!menuExpanded) {
                    menuExpanded = true
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onAccountClicked() }) {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = stringResource(R.string.account_icon)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Filled.DensitySmall,
                                contentDescription = stringResource(R.string.menu_icon)
                            )
                        }
                        Menu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            onDeleteAccountClick = onDeleteAccountClick,
                            onFAQClick = onFAQClick,
                            onAboutClick = onAboutClick
                        )
                    }
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
                Box(
                    modifier = Modifier
                        .size(400.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = {
                                    homeViewModel.enableNFC()
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.NFC_communication_text_info),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                ,
                                onPress = {
                                    tryAwaitRelease()
                                    isPressed = !isPressed
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Crossfade(
                        targetState = isPressed,
                        animationSpec = tween(durationMillis = 500)
                    ) { state ->
                        if (!uiState.isLoading) {
                            CardNFC(
                                state,
                                uiState.user!!.name,
                                uiState.user!!.profilePhotoUrl,
                                uiState.user!!.lastName,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.NFC_communication_text),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}