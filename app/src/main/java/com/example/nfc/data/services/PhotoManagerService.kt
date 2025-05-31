package com.example.nfc.data.services

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher

interface PhotoManagerService {
    fun createImageUri(): Uri
    fun launchCamera(photoLauncher: ManagedActivityResultLauncher<Uri, Boolean>)
    fun launchGallery(galleryLauncher: ManagedActivityResultLauncher<String, Uri?>)
}