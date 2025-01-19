package com.example.tilebase

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()
    private lateinit var navController: NavController // Deklaracja NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
             // Inicjalizacja NavController
            MaterialTheme {
                App() // Przekazanie NavController do App
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            registerViewModel.handleGoogleSignInResult(task, navController)
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val firebaseAuth = FirebaseAuth.getInstance()
    NavGraph(navController, firebaseAuth)
}

// CompositionLocal for FirebaseAuth
val LocalFirebaseAuth = compositionLocalOf<FirebaseAuth> { error("No FirebaseAuth provided") }
