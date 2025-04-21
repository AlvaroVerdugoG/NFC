package com.example.nfc.view.register.RegisterViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc.data.services.FireBaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val fireBaseAuth: FireBaseAuth) :
    ViewModel() {
    data class RegisterUIState(
        var isLoading: Boolean = true,
        var errorMessage: String = "",
        var isSigIn: Boolean = false
    )

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState

    fun register(email: String, password: String) {

        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireBaseAuth.register(email, password).fold(
                    error = {
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = it.toString()
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
                fireBaseAuth.signIn(email, password).fold(
                    error = {
                        _uiState.update { currentState ->
                            currentState.copy(
                                errorMessage = it.toString()
                            )
                        }
                    },
                    success = {
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
}