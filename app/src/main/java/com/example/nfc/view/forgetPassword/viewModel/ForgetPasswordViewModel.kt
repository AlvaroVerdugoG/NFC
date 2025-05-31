package com.example.nfc.view.forgetPassword.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc.R
import com.example.nfc.data.services.FireBaseAuth
import com.example.nfc.data.services.FireBaseStorage
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
class ForgetPasswordViewModel @Inject constructor(
    private val fireBaseStorage: FireBaseStorage,
    private val fireBaseAuth: FireBaseAuth
) : ViewModel() {
    data class ForgetPasswordUIState(
        var isLoading: Boolean = true,
        var errorMessage: String = "",
        var isSendIt: Boolean = false
    )

    private val _uiState = MutableStateFlow(ForgetPasswordUIState())
    val uiState: StateFlow<ForgetPasswordUIState> = _uiState

    fun resetPassword(context: Context, email: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireBaseStorage.checkEmailRegistered(email).fold(
                error = {
                    val errorMsg = when (it) {
                        is NFCError.FireBaseError -> it.message
                        NFCError.Default ->context.getString(R.string.unexpected_error)
                    }
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = errorMsg
                        )
                    }
                },
                success = {
                    if(it){
                        sendEmail(context, email)
                    } else{
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = context.getString(R.string.has_not_registered)
                            )
                        }
                    }
                }
                )
            }
        }
    }
    private fun sendEmail(context: Context,email: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                fireBaseAuth.forgetPassword(email).fold(
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
                                isSendIt = true
                            )
                        }
                    }
                )
            }
        }
    }
    fun resetItsSendIt(){
        _uiState.update { currentState ->
            currentState.copy(
                isSendIt = false
            )
        }
    }
}