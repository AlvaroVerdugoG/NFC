package com.example.nfc.view.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nfc.view.aboutInfo.viewModel.AboutInfoViewModel
import com.example.nfc.view.deleteAccount.viewModel.DeleteAccountViewModel
import com.example.nfc.view.faqScreen.viewModel.FAQViewModel
import com.example.nfc.view.forgetPassword.viewModel.ForgetPasswordViewModel
import com.example.nfc.view.home.viewModel.HomeViewModel
import com.example.nfc.view.login.viewModel.LoginViewModel
import com.example.nfc.view.profile.viewModel.ProfileViewModel
import com.example.nfc.view.register.RegisterViewModel.RegisterViewModel
import com.example.nfc.view.route.RouteAbout
import com.example.nfc.view.route.RouteDeleteAccount
import com.example.nfc.view.route.RouteFAQ
import com.example.nfc.view.route.RouteForgetPassword
import com.example.nfc.view.route.RouteHome
import com.example.nfc.view.route.RouteLogIn
import com.example.nfc.view.route.RouteProfile
import com.example.nfc.view.route.RouteSignUp
import com.example.nfc.view.screen.Screen

@Composable
fun NavigationGraph(
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    homeViewModel: HomeViewModel,
    accountViewModel: ProfileViewModel,
    forgetPasswordViewModel: ForgetPasswordViewModel,
    deleteAccountViewModel: DeleteAccountViewModel,
    faqViewModel: FAQViewModel,
    aboutInfoViewModel: AboutInfoViewModel
) {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = Screen.Login.route,
    ) {
        composable(route = Screen.Login.route) {
            RouteLogIn(
                onLoginClick = {
                    navHostController.navigate(
                        Screen.Home.route
                    )
                },
                registerClick = {
                    navHostController.navigate(
                        Screen.Register.route
                    )
                },
                loginViewModel = loginViewModel,
                onForgetPasswordClick = {
                    navHostController.navigate(
                        Screen.ForgetPassword.route
                    )
                }

            )
        }
        composable(route = Screen.Register.route) {
            RouteSignUp(
                registerClick = {
                    navHostController.navigate(
                        Screen.Home.route
                    )
                },
                onSignInClick = {
                    navHostController.navigate(
                        Screen.Login.route
                    )
                },
                registerViewModel = registerViewModel,
            )
        }
        composable(route = Screen.Home.route) {
            RouteHome(
                homeViewModel = homeViewModel,
                onAccountClicked = {
                    navHostController.navigate(
                        Screen.Profile.route
                    )
                },
                onFAQClick =  {
                    navHostController.navigate(
                        Screen.FAQ.route
                    )
                },
                onAboutClick =  {
                    navHostController.navigate(
                        Screen.About.route
                    )
                },
                onDeleteAccountClick = {
                    navHostController.navigate(
                        Screen.Delete.route
                    )
                },

            )
        }
        composable(route = Screen.Profile.route) {
            RouteProfile(
                accountViewModel = accountViewModel,
                onSignOutClicked = {
                    navHostController.navigate(
                        Screen.Login.route
                    )
                },
                onBackClick = {
                    navHostController.navigate(
                        Screen.Home.route
                    )
                }
            )
        }
        composable(route = Screen.ForgetPassword.route) {
            RouteForgetPassword(
                forgetPasswordViewModel = forgetPasswordViewModel,
                onItsDoneClick = {
                    navHostController.navigate(
                        Screen.Login.route
                    )
                },
                onBackClick = {
                    navHostController.navigate(
                        Screen.Login.route
                    )
                }
            )
        }
        composable(route = Screen.FAQ.route) {
            RouteFAQ(
                faqViewModel = faqViewModel,
                onBackClick = {
                    navHostController.navigate(
                        Screen.Home.route
                    )
                }
            )
        }
        composable(route = Screen.About.route) {
            RouteAbout(
                aboutInfoViewModel = aboutInfoViewModel,
                onBackClick = {
                    navHostController.navigate(
                        Screen.Home.route
                    )
                }
            )
        }
        composable(route = Screen.Delete.route) {
            RouteDeleteAccount(
                deleteAccountViewModel = deleteAccountViewModel,
                onDeleteClick = {
                    navHostController.navigate(
                        Screen.Login.route
                    )
                },
                onBackClick = {
                    navHostController.navigate(
                        Screen.Home.route
                    )
                }
            )
        }
    }
}