package com.example.nfc.view.faqScreen.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.nfc.R
import com.example.nfc.util.components.FAQItem
import com.example.nfc.view.faqScreen.viewModel.FAQViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(
    faqViewModel: FAQViewModel,
    onBackClick: () -> Unit
) {
    val context =
        LocalContext.current
    val uiState by faqViewModel.uiState.collectAsState()

    faqViewModel.getFAQ(context)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.faq),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.forget_password)
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
        ) {
            items(uiState.faqs){ faq ->
                FAQItem(faq)
            }
        }
    }
}