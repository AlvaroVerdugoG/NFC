package com.example.nfc.view.deleteAccount.viewModel

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
class DeleteAccountViewModel @Inject constructor(
    private val fireBaseAuth: FireBaseAuth,
    private val fireBaseStorage: FireBaseStorage
) : ViewModel() {
    data class DeleteAccountUIState(
        var isLoading: Boolean = true,
        var errorMessage: String = "",
        var isDelete: Boolean = false,
        var isReasonsSelected: Boolean = false
    )

    private val _uiState = MutableStateFlow(DeleteAccountUIState())
    val uiState: StateFlow<DeleteAccountUIState> = _uiState

    fun deleteAccount(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireBaseStorage.deleteDataFromEmail(fireBaseAuth.getEmail()).fold(
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
                        deleteDataAccount(context)
                    }
                )
            }
        }
    }

    private fun deleteDataAccount(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireBaseAuth.deleteAccount().fold(
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
                                isDelete = true
                            )
                        }
                    }
                )
            }
        }
    }

    fun saveDeleteAccountReason(reasons: List<String>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireBaseStorage.saveDeleteReason(reasons).fold(
                    error = {
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = "We have some issues.\nPlease try to delete the account in a few minutes"
                            )
                        }
                    },
                    success = {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isReasonsSelected = true
                            )
                        }
                    }
                )
            }
        }
    }

    fun resetVariables() {
        _uiState.update { currentState ->
            currentState.copy(
                isDelete = false,
                isReasonsSelected = false
            )
        }
    }
}