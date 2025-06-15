package com.example.nfc.view.faqScreen.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.nfc.R
import com.example.nfc.model.FAQ
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FAQViewModel @Inject constructor() : ViewModel() {
    data class FAQUIState(var faqs: List<FAQ> = listOf())

    private val _uiState = MutableStateFlow(FAQUIState())
    val uiState: StateFlow<FAQUIState> = _uiState
    fun getFAQ(context: Context) {
        _uiState.update { currentState ->
            currentState.copy(faqs = listOf(
                FAQ(question = context.getString(R.string.login_question),
                    answer = context.getString(R.string.login_answer)),
                FAQ(question = context.getString(R.string.register_question),
                    answer = context.getString(R.string.register_answer)),
                FAQ(question = context.getString(R.string.forget_password_question),
                    answer = context.getString(R.string.forget_password_answer)),
                FAQ(question = context.getString(R.string.change_password_question),
                    answer = context.getString(R.string.change_password_answer)),
                FAQ(question = context.getString(R.string.add_photo_question),
                    answer = context.getString(R.string.add_photo_answer)),
                FAQ(question = context.getString(R.string.how_NFC_question),
                    answer = context.getString(R.string.how_NFC_answer)),
            ))
        }
    }
}