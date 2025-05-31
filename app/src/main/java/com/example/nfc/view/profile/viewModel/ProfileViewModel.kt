package com.example.nfc.view.profile.viewModel

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc.R
import com.example.nfc.data.services.FireBaseAuth
import com.example.nfc.data.services.FireBaseStorage
import com.example.nfc.data.services.PhotoManager
import com.example.nfc.model.User
import com.example.nfc.model.error.NFCError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fireBaseAuth: FireBaseAuth,
    private val photoManager: PhotoManager,
    private val fireBaseStorage: FireBaseStorage
) : ViewModel() {
    data class ProfileUIState(
        var isLoading: Boolean = false,
        var errorMessage: String = "",
        var isSignOut: Boolean = false,
        var isUpdatedPassword: Boolean = false,
        var user: User? = null
    )

    private val _uiState = MutableStateFlow((ProfileUIState()))
    val uiState: StateFlow<ProfileUIState> = _uiState

    fun fetchUserData(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireBaseStorage.fetchUserData(fireBaseAuth.getEmail()).fold(
                    error = {
                        val errorMsg = when (it) {
                            is NFCError.FireBaseError -> it.message
                            NFCError.Default -> context.getString(R.string.unexpected_error)
                        }
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = errorMsg
                            )
                        }
                    },
                    success = {
                        _uiState.update { currentState ->
                            currentState.copy(
                                user = it
                            )
                        }
                    }
                )
            }
        }
    }
    fun signOut(context: Context) {

        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireBaseAuth.signOut().fold(
                    error = {
                        val errorMsg = when (it) {
                            is NFCError.FireBaseError -> it.message
                            NFCError.Default -> context.getString(R.string.unexpected_error)
                        }
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = errorMsg
                            )
                        }
                    },
                    success = {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                isSignOut = true
                            )
                        }
                    }
                )
            }
        }
    }

    fun updatePassword(context: Context,password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireBaseAuth.changePassword(password).fold(
                    error = {
                        val errorMsg = when (it) {
                            is NFCError.FireBaseError -> it.message
                            NFCError.Default -> context.getString(R.string.unexpected_error)
                        }
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = errorMsg
                            )
                        }
                    },
                    success = {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                isUpdatedPassword = true,
                            )
                        }
                    }
                )
            }
        }
    }

    fun setProfilePhotoUri(uri: Uri) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireBaseStorage.updateProfilePhoto(fireBaseAuth.getEmail(), uri.toString())
                _uiState.update { currentState ->
                    currentState.copy(
                        user = currentState.user?.copy(
                            profilePhotoUrl = uri.toString()
                        )
                    )
                }
            }
        }
    }

    fun createImageFile(): Uri {
        return photoManager.createImageUri()
    }

    fun launchCamera(cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>) {
        photoManager.launchCamera(cameraLauncher)
    }

    fun launchGallery(galleryLauncher: ManagedActivityResultLauncher<String, Uri?>){
        photoManager.launchGallery(galleryLauncher)
    }

    fun resetSignOutStatus() {
        _uiState.update { currentState ->
            currentState.copy(
                isSignOut = false
            )
        }
    }

    fun resetUpdatedPasswordStatus() {
        _uiState.update { currentState ->
            currentState.copy(
                isUpdatedPassword = false
            )
        }
    }
}
