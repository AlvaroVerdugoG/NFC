package com.example.nfc.view.route

import com.example.nfc.view.faqScreen.view.FAQScreen
import androidx.compose.runtime.Composable
import com.example.nfc.view.faqScreen.viewModel.FAQViewModel

@Composable
fun RouteFAQ(
    faqViewModel: FAQViewModel,
    onBackClick: () -> Unit
){
    FAQScreen(
        faqViewModel = faqViewModel,
        onBackClick = onBackClick
    )
}