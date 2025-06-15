package com.example.nfc.util.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nfc.R

@Composable
fun CardNFC(isPressed: Boolean, name: String, photo: String, lastName: String) {
    if (!isPressed) {
        Box(modifier = Modifier
            .size(300.dp, 200.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .background(Color(0x66FFFFFF))
            .border(2.dp, Color.Cyan, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.TopStart) {
            Column(modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {
                if (photo.isNotEmpty()) {
                    AsyncImage(
                        model = Uri.parse(photo),
                        contentDescription = stringResource(R.string.profile_photo),
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .align(Alignment.Start),
                        contentScale = ContentScale.FillBounds,

                        )
                } else {
                    Spacer(modifier = Modifier.height(80.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = stringResource(R.string.first_name_card) + " " + name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Start))

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = stringResource(R.string.last_name_card) + " " + lastName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Start))
            }
        }

    } else {
        Box(modifier = Modifier
            .size(300.dp, 200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
            .border(2.dp, Color.DarkGray, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(R.string.card_transport_text),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    Icon(imageVector = Icons.Default.DirectionsBus,
                        contentDescription = stringResource(R.string.card_transport_icon1),
                        tint = Color.Blue,
                        modifier = Modifier.size(30.dp))
                    Icon(imageVector = Icons.Default.Train,
                        contentDescription = stringResource(R.string.card_transport_icon2),
                        tint = Color.Red,
                        modifier = Modifier.size(30.dp))
                    Icon(imageVector = Icons.Default.DirectionsCar,
                        contentDescription = stringResource(R.string.card_transport_icon3),
                        tint = Color.Green,
                        modifier = Modifier.size(30.dp))
                }
            }
        }
    }
}