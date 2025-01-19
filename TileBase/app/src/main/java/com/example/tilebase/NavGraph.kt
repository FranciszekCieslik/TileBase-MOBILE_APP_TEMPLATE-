package com.example.tilebase

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavHostController,firebaseAuth: FirebaseAuth) {
    NavHost(navController = navController, startDestination = "login") {
        composable("welcome") { WelcomeScreen(navController) }
        composable("login") { LoginScreen(navController, firebaseAuth) }
        composable("register") { RegisterScreen(navController, firebaseAuth) }
        composable("main") { MainScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}