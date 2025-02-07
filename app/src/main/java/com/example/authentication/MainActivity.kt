package com.example.authentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.authentication.data.auth.FirebaseAuthRepository
import com.example.authentication.domain.auth.AuthRepository
import com.example.authentication.domain.auth.LoginUseCase
import com.example.authentication.domain.auth.RegisterUseCase
import com.example.authentication.domain.auth.AuthViewModel
import com.example.authentication.presentation.LoginScreen
import com.example.authentication.presentation.RegisterScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firebaseAuth = FirebaseAuth.getInstance()

        val authRepository: AuthRepository = FirebaseAuthRepository(firebaseAuth)
        val registerUseCase = RegisterUseCase(authRepository)
        val loginUseCase = LoginUseCase(authRepository)
        val authViewModel = AuthViewModel(registerUseCase, loginUseCase)

        setContent {
            var showRegisterScreen by remember { mutableStateOf(false) }

            if (showRegisterScreen) {
                RegisterScreen(
                    viewModel = authViewModel,
                    onRegisterSuccess = { showRegisterScreen = false }, // Go back to login after success
                    onNavigateBack = { showRegisterScreen = false } // Navigate back to login
                )
            } else {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = { /* Handle successful login */ },
                    onNavigateToRegister = { showRegisterScreen = true } // Show RegisterScreen
                )
            }
        }
    }
}


