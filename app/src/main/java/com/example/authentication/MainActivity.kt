package com.example.authentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authentication.data.auth.FirebaseAuthRepository
import com.example.authentication.domain.auth.AuthRepository
import com.example.authentication.domain.auth.LoginUseCase
import com.example.authentication.domain.auth.RegisterUseCase
import com.example.authentication.domain.auth.AuthViewModel
import com.example.authentication.presentation.LoginScreen
import com.example.authentication.presentation.MainPage
import com.example.authentication.presentation.RegisterScreen
import com.example.authentication.presentation.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {

            MovieApp()

        }
    }
}


@Composable
fun MovieApp(
    navController: NavHostController = rememberNavController()
) {
    val firebaseAuth = FirebaseAuth.getInstance()

    val authRepository: AuthRepository = FirebaseAuthRepository(firebaseAuth)
    val registerUseCase = RegisterUseCase(authRepository)
    val loginUseCase = LoginUseCase(authRepository)
    val authViewModel = AuthViewModel(registerUseCase, loginUseCase)
    NavHost(
        navController = navController,
        startDestination = Screen.LogIn.route,
    ) {
        composable(route = Screen.LogIn.name) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {navController.navigate(Screen.MainPage.route)} ,
                onNavigateToRegister = {navController.navigate(Screen.SignUp.route)}


            )

        }
        composable(route = Screen.SignUp.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {navController.navigate(Screen.MainPage.route)},
                onNavigateBack = {navController.navigate(Screen.LogIn.route)}
            )

        }
        composable(route = Screen.MainPage.route) {
            MainPage()

        }

    }
}