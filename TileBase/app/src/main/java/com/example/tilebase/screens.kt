package com.example.tilebase

import android.annotation.SuppressLint

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewmodel.compose.viewModel

//-----------------------------------------------------------------------

@Composable
fun LoginScreen(navController: NavController, firebaseAuth: FirebaseAuth, viewModel: RegisterViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign In", style = MaterialTheme.typography.displayMedium)

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        error?.let {
            Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    isLoading = true
                    error = null
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                navController.navigate("main")
                            } else {
                                error = task.exception?.localizedMessage ?: "Sign in failed"
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign in")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.signInWithGoogle(navController) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4285F4)
                )

            ) {
                Text("Sign in with Google", color = Color.White)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate("register") },
                modifier = Modifier.fillMaxWidth()

            ) {
                Text("Create an account")
            }

        }
    }
}



//-----------------------------------------------------------------------

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel()
) {
    val state = viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Create an Account", style = MaterialTheme.typography.displayMedium)

        Spacer(modifier = Modifier.height(16.dp))

        EmailField(
            value = state.value.email,
            onValueChange = { viewModel.onEmailChange(it) }
        )
        PasswordField(
            value = state.value.password,
            onValueChange = { viewModel.onPasswordChange(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        state.value.error?.let {
            Text(text = it, color = Color.Red)
        }

        Button(
            onClick = { viewModel.register(navController) },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.value.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text("Sign up")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { navController.navigate("login") }
        ) {
            Text("Already have an account? Sign in")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.signInWithGoogle(navController) },
            colors = ButtonDefaults.buttonColors(Color(0xFF4285F4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in with Google", color = Color.White)
        }
    }
}

@Composable
fun EmailField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PasswordField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}


//-----------------------------------------------------------------------

@Composable
fun BottomNavBar(navController: NavController, selectedRoute: String) {
    BottomNavigation {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedRoute == "main",
            onClick = { navController.navigate("main") }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedRoute == "profile",
            onClick = { navController.navigate("profile") }
        )
    }
}

//-----------------------------------------------------------------------

@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController, selectedRoute = "main")
        }
    ) {
        Text(
            text = "Main Screen Content",
            modifier = Modifier.fillMaxSize().padding(it),
            textAlign = TextAlign.Center
        )
    }
}

//-----------------------------------------------------------------------


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController, selectedRoute = "profile")
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("User Profile")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("settings") }) {
                Text("Go to Settings")
            }
        }
    }
}

//-----------------------------------------------------------------------

@Composable
fun SettingsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("profile") }) {
            Text("Back to Profile")
        }
    }
}

//-----------------------------------------------------------------------
