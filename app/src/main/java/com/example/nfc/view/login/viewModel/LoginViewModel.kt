package com.example.nfc.view.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc.data.services.FireBaseAuth
import com.example.nfc.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val fireBaseAuth: FireBaseAuth): ViewModel(){
    data class LoginUIState(
        var isLoading: Boolean = true,
        var errorMessage: String = "",
        var isSigIn: Boolean = false
    )



    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState


    fun signIn(user: User) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                fireBaseAuth.signIn(user.email, user.password).fold (
                    error = {
                        _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = it.toString()
                        )
                    }},
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
