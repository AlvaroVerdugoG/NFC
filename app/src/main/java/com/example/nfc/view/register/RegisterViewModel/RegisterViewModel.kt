package com.example.nfc.view.register.RegisterViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc.R
import com.example.nfc.data.services.FireBaseAuth
import com.example.nfc.data.services.FireBaseStorage
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
class RegisterViewModel @Inject constructor(
    private val fireBaseAuth: FireBaseAuth,
    private val fireBaseStorage: FireBaseStorage
) : ViewModel() {
    data class RegisterUIState(
        var isLoading: Boolean = false,
        var errorMessage: String = "",
        var isSigIn: Boolean = false,
        var confirmEmail: Boolean = false
    )

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState

    fun register(context: Context, user: User) {

        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireBaseAuth.register(user.email, user.password).fold(
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
                                confirmEmail = true
                            )
                        }
                    }
                )

            }

        }
    }

    fun signIn(context: Context, user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireBaseAuth.signIn(user.email, user.password).fold(
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
                        saveUserData(context, user = user)
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                isSigIn = true
                            )
                        }
                    }
                )
            }
        }
    }

    private fun saveUserData(context: Context, user: User) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireBaseStorage.saveUserData(user = user).fold(
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
                    }
                )
            }
        }

    }

    fun resendVerificationEmail(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireBaseAuth.reSendVerificationEmail().fold(
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
                            )
                        }
                    }
                )
            }
        }
    }

    fun checkPassword(context: Context, password: String, confirmPassword: String): String {
        return if (password.isEmpty()) {
            context.getString(R.string.password_required)
        } else if (confirmPassword.isEmpty()) {
            context.getString(R.string.validate_password)
        } else if (confirmPassword != password) {
            context.getString(R.string.passwords_not_mach)
        } else if (confirmPassword.length < 6) {
            context.getString(R.string.passwords_length_wrong)
        } else {
            ""
        }
    }

    fun resetRegisterStatus() {
        _uiState.update { currentState ->
            currentState.copy(
                isSigIn = false,
                errorMessage = ""
            )
        }
    }

    fun resetConfirmEmail() {
        _uiState.update { currentState ->
            currentState.copy(
                confirmEmail = false
            )
        }
    }
}