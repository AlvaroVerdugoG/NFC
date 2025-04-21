package com.example.nfc.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nfc.view.login.viewModel.LoginViewModel
import com.example.nfc.view.register.SingUpViewModel.SignUpViewModel
import com.example.nfc.view.route.RouteError
import com.example.nfc.view.route.RouteHome
import com.example.nfc.view.route.RouteLogIn
import com.example.nfc.view.route.RouteSignUp
import com.example.nfc.view.screen.Screen

@Composable
fun NavigationGraph(
    loginViewModel: LoginViewModel,
    signUpViewModel: SignUpViewModel
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
                    ) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                            saveState = false
                        }
                    }
                },
                onSignUpClick = {
                    navHostController.navigate(
                        Screen.SignIn.route
                    ) {
                        popUpTo(Screen.SignIn.route) {
                            inclusive = true
                            saveState = false
                        }
                    }
                },
                loginViewModel = loginViewModel,
                onMessageError = {
                    navHostController.navigate(
                        Screen.Error.route
                    ) {
                        popUpTo(Screen.Error.route) {
                            inclusive = true
                            saveState = false
                        }
                    }
                }

            )
        }
        composable(route = Screen.SignIn.route) {
            RouteSignUp(
                onSignUpClick = {
                    navHostController.navigate(
                        Screen.Home.route
                    ) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                            saveState = false
                        }
                    }
                },
                onSignInClick = {
                    navHostController.navigate(
                        Screen.Login.route
                    ) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                            saveState = false
                        }
                    }
                },
                signUpViewModel = signUpViewModel,
                onMessageError = {
                    navHostController.navigate(
                        Screen.Error.route
                    ) {
                        popUpTo(Screen.Error.route) {
                            inclusive = true
                            saveState = false
                        }
                    }
                }
                )
        }
        composable(route = Screen.Home.route) {
            RouteHome()
        }
        composable(route = Screen.Error.route) {
            RouteError("")
        }

    }
}