package com.example.nfc.view.home.view

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DensitySmall
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfc.R
import com.example.nfc.view.components.Menu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var isPressed by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    val context =
        LocalContext.current
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
                            text = "NFC",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "Account Icon"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Filled.DensitySmall,
                                contentDescription = "Menu Icon"
                            )
                        }
                        Menu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
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
                                    //MyHostApduService.isEnabled = true
                                    Toast.makeText(
                                        context,
                                        "Ahora acerca la tarjeta al lector NFC para iniciar la comunicación.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onPress = {
                                    tryAwaitRelease()
                                    isPressed = !isPressed
                                }
                            )
                        },
                ) {
                    // Con Crossfade se hace una transición suave entre los dos estados
                    Crossfade(
                        targetState = isPressed,
                        animationSpec = tween(durationMillis = 500)
                    ) { state ->
                        if (!state) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_nfc_card),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Text(
                                    "NFC",
                                    fontSize = 24.sp,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        } else {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_nfc_card_background),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "nombre: ",
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(8.dp)
                                    )
                                    Text(
                                        text = "apellido:",
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(8.dp)
                                    )
                                }

                            }
                        }

                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Click on the card to see your data or keep the click to activate communication.",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}