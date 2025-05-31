package com.example.nfc.data.services

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class PhotoManager @Inject constructor(@ApplicationContext private val context: Context) :
    PhotoManagerService {
    var photoUri: Uri? = null

    override fun createImageUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFile = File(context.cacheDir, "Image${timeStamp}.jpg").apply { createNewFile() }
        photoUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )
        return photoUri!!
    }

    override fun launchCamera(photoLauncher: ManagedActivityResultLauncher<Uri, Boolean>) {
        photoUri?.let { photoLauncher.launch(it) }
    }

    override fun launchGallery(galleryLauncher: ManagedActivityResultLauncher<String, Uri?>) {
        galleryLauncher.launch("image/*")
    }


}