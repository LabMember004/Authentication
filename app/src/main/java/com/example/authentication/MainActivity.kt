package com.example.authentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.authentication.ui.theme.AuthenticationTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthenticationTheme {
                // Check if the user is logged in
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    // Show home screen if logged in
                    HomeScreen()
                } else {
                    // Show login screen if not logged in
                    AuthenticationScreen(auth = auth)
                }
            }
        }
    }
}

@Composable
fun AuthenticationScreen(auth: FirebaseAuth) {
    var showLogin by remember { mutableStateOf(true) }

    if (showLogin) {
        // Login screen
        LoginScreen(auth = auth, onNavigateToRegister = { showLogin = false })
    } else {
        // Registration screen
        RegisterScreen(auth = auth, onNavigateToLogin = { showLogin = true })
    }
}

@Composable
fun LoginScreen(auth: FirebaseAuth, onNavigateToRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = { Text(text = "Login", style = MaterialTheme.typography.headlineSmall) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                Spacer(modifier = Modifier.height(8.dp))

                TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
                Spacer(modifier = Modifier.height(16.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        login(auth, email, password) { success, message ->
                            if (success) {
                                // Navigate to HomeScreen on successful login
                            } else {
                                errorMessage = message
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Login")
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onNavigateToRegister) {
                    Text(text = "Don't have an account? Register here.")
                }
            }
        }
    )
}

@Composable
fun RegisterScreen(auth: FirebaseAuth, onNavigateToLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = { Text(text = "Register", style = MaterialTheme.typography.headlineSmall) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                Spacer(modifier = Modifier.height(8.dp))

                TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
                Spacer(modifier = Modifier.height(8.dp))

                TextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") })
                Spacer(modifier = Modifier.height(16.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        if (password == confirmPassword) {
                            register(auth, email, password) { success, message ->
                                if (success) {
                                    // Navigate to login screen on successful registration
                                    onNavigateToLogin()
                                } else {
                                    errorMessage = message
                                }
                            }
                        } else {
                            errorMessage = "Passwords do not match"
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Register")
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onNavigateToLogin) {
                    Text(text = "Already have an account? Login here.")
                }
            }
        }
    )
}

fun login(auth: FirebaseAuth, email: String, password: String, onResult: (Boolean, String) -> Unit) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, "")
            } else {
                onResult(false, task.exception?.message ?: "Login failed")
            }
        }
}

fun register(auth: FirebaseAuth, email: String, password: String, onResult: (Boolean, String) -> Unit) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, "")
            } else {
                onResult(false, task.exception?.message ?: "Registration failed")
            }
        }
}

@Composable
fun HomeScreen() {
    Scaffold(
        topBar = { Text(text = "Home", style = MaterialTheme.typography.headlineSmall) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Welcome to the home screen!")
            }
        }
    )
}
