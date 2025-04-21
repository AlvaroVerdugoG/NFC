package com.example.nfc.view.home.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfc.R
import com.example.nfc.data.services.MyHostApduService

@Composable
fun HomeScreen() {
    val context =
        LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),//Falta crear un archivo con la foto de una tarjeta
            contentDescription = "Virtual NFC card",
            modifier = Modifier
                .size(200.dp)
                .clickable {
                    MyHostApduService.isEnabled = true
                    Toast.makeText(
                        context,
                        "Now bring it close to the NFC reader to start communication.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Click on the card to activate communication.",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}