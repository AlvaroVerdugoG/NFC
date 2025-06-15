package com.example.nfc

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.nfc.view.aboutInfo.viewModel.AboutInfoViewModel
import com.example.nfc.view.deleteAccount.viewModel.DeleteAccountViewModel
import com.example.nfc.view.faqScreen.viewModel.FAQViewModel
import com.example.nfc.view.forgetPassword.viewModel.ForgetPasswordViewModel
import com.example.nfc.view.home.viewModel.HomeViewModel
import com.example.nfc.view.login.viewModel.LoginViewModel
import com.example.nfc.view.navigation.NavigationGraph
import com.example.nfc.view.profile.viewModel.ProfileViewModel
import com.example.nfc.view.register.viewModel.RegisterViewModel
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : AppCompatActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()
    private val signInViewModel: LoginViewModel by viewModels()
    private val accountViewModel: ProfileViewModel by viewModels()
    private val forgetPasswordViewModel: ForgetPasswordViewModel by viewModels()
    private val deleteAccountViewModel: DeleteAccountViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val faqViewModel: FAQViewModel by viewModels()
    private val aboutInfoViewModel: AboutInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)
        setContent {
            NavigationGraph(signInViewModel,
                registerViewModel,
                homeViewModel,
                accountViewModel,
                forgetPasswordViewModel,
                deleteAccountViewModel,
                faqViewModel,
                aboutInfoViewModel)

        }
    }
}
