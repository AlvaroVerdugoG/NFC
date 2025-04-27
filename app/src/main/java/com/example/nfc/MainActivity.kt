package com.example.nfc

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nfc.view.login.viewModel.LoginViewModel
import com.example.nfc.view.navigation.NavigationGraph
import com.example.nfc.view.register.RegisterViewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : AppCompatActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()
    private val signInViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        setContent {
            NavigationGraph(signInViewModel, registerViewModel)
        }
    }
}
