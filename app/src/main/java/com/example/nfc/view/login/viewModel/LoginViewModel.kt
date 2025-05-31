package com.example.nfc.view.login.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfc.R
import com.example.nfc.data.preference.CommonPreference
import com.example.nfc.data.services.FireBaseAuth
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
class LoginViewModel @Inject constructor(
    private val fireBaseAuth: FireBaseAuth,
    private val sharedPreferences: CommonPreference
) : ViewModel() {
    data class LoginUIState(
        var isLoading: Boolean = true,
        var errorMessage: String = "",
        var isSigIn: Boolean = false,
        var email: String = ""
    )


    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState


    fun signIn(context: Context, user: User) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
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

    fun resetLoginStatus() {
        _uiState.update { currentState ->
            currentState.copy(
                isSigIn = false,
                errorMessage = ""
            )
        }
    }
    fun getBoolean(): Boolean{
        return sharedPreferences.getBooleanUserData()
    }
    fun getUserEmail() {
        if(sharedPreferences.getBooleanUserData()) {
            _uiState.update { currentState ->
                currentState.copy(
                    email = sharedPreferences.getEmail()
                )
            }
        }
    }
    fun setUserEmailPreferences(email: String, isChecked: Boolean){
        sharedPreferences.setBooleanUserData(isChecked)
        sharedPreferences.setEmail(email)
    }
}
