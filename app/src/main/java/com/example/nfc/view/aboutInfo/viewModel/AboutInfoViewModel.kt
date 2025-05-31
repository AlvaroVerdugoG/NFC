package com.example.nfc.view.aboutInfo.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.nfc.R
import com.example.nfc.data.services.FireBaseAuth
import com.example.nfc.data.services.FireBaseStorage
import com.example.nfc.model.AboutInfo
import com.example.nfc.view.deleteAccount.viewModel.DeleteAccountViewModel.DeleteAccountUIState
import com.google.firebase.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AboutInfoViewModel @Inject constructor() : ViewModel() {
    data class AboutInfoUIState(
        var aboutInfo: AboutInfo? = null
    )
    private val _uiState = MutableStateFlow(AboutInfoUIState())
    val uiState: StateFlow<AboutInfoUIState> = _uiState

    fun getAboutInfo(context: Context){
        _uiState.update { currentState ->
            currentState.copy(
                aboutInfo = AboutInfo(
                    appName = context.getString(R.string.app_name),
                    version = BuildConfig.VERSION_NAME,
                    develop = context.getString(R.string.develop_name)
                )
            )
        }
    }
}