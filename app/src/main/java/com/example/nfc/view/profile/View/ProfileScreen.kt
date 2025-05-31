package com.example.nfc.view.profile.View

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.nfc.R

import com.example.nfc.util.components.LoginTextField
import com.example.nfc.view.profile.viewModel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    accountViewModel: ProfileViewModel,
    onSignOutClicked: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by accountViewModel.uiState.collectAsState()

    var showPhotoDialog by remember { mutableStateOf(false) }
    var isEditable by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            accountViewModel.setProfilePhotoUri(it)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            accountViewModel.createImageFile().let { accountViewModel.setProfilePhotoUri(it) }
        }

    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            accountViewModel.createImageFile()
            accountViewModel.launchCamera(cameraLauncher)
        } else {
            Toast.makeText(
                context,
                R.string.permission_needed,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    accountViewModel.fetchUserData(context)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.account),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.account_icon)
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        LaunchedEffect(key1 = uiState.isSignOut) {
            if (uiState.isSignOut) {
                onSignOutClicked()
                accountViewModel.resetSignOutStatus()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = uiState.isUpdatedPassword,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = stringResource(R.string.password_changed),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Green
                )
            }
            AnimatedVisibility(
                visible = uiState.errorMessage.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = uiState.errorMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFFD32F2F)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.user != null && uiState.user?.profilePhotoUrl?.isNotEmpty() == true) {
                AsyncImage(
                    model = uiState.user!!.profilePhotoUrl,
                    contentDescription = stringResource(R.string.profile_photo),
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.profile_photo),
                    modifier = Modifier
                        .size(120.dp)
                        .clickable { showPhotoDialog = true },
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            if (showPhotoDialog) {
                AlertDialog(
                    onDismissRequest = { showPhotoDialog = false },
                    title = { Text(text = stringResource(R.string.select_photo)) },
                    text = {
                        Text(
                            text = stringResource(R.string.gallery_or_photo)
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showPhotoDialog = false
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        ) {
                            Text(text = stringResource(R.string.take_photo))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showPhotoDialog = false
                                accountViewModel.launchGallery(galleryLauncher)
                            }
                        ) {
                            Text(text = stringResource(R.string.gallery))
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(
                    text = stringResource(R.string.user_name),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = uiState.user?.email ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text(
                    text = stringResource(R.string.password),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = if (!isEditable) "********" else password,
                    onValueChange = { password = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            accountViewModel.updatePassword(context, password = password)
                            isEditable = false
                            password = ""
                            accountViewModel.resetUpdatedPasswordStatus()
                        }
                    ),
                    enabled = isEditable,
                    modifier = Modifier.fillMaxWidth()
                )

            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    isEditable = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.change_password))
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { accountViewModel.signOut(context) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.sign_out))
            }
        }
    }
}
