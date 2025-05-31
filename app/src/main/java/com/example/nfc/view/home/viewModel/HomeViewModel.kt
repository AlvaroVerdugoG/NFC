package com.example.nfc.view.home.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc.R
import com.example.nfc.data.preference.CommonPreference
import com.example.nfc.data.services.FireBaseAuth
import com.example.nfc.data.services.FireBaseStorage
import com.example.nfc.data.services.MyHostApduService
import com.example.nfc.model.User
import com.example.nfc.model.error.NFCError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fireBaseAuth: FireBaseAuth,
    private val fireBaseStorage: FireBaseStorage
) : ViewModel() {
    data class HomeUIState(
        var isLoading: Boolean = true,
        var errorMessage: String = "",
        var user: User? = null
    )

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState

    fun enableNFC(){
        MyHostApduService.enable = true
    }
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
                                isLoading = false,
                                user = it
                            )
                        }
                    }
                )
            }
        }
    }
}