package com.example.tilebase

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tilebase.ui.screens.LoadingScreen
import com.example.tilebase.ui.screens.LoginScreen
import com.example.tilebase.ui.screens.MainScreen
import com.example.tilebase.ui.screens.ProfileScreen
import com.example.tilebase.ui.screens.RegisterScreen
import com.example.tilebase.ui.screens.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController, viewModel: RegisterViewModel) {
    NavHost(navController = navController, startDestination = "loading") {
        composable("loading") { LoadingScreen(navController, viewModel) }
        composable("login") { LoginScreen(navController,viewModel) }
        composable("register") { RegisterScreen(navController,viewModel) }
        composable("main") { MainScreen(navController) }
        composable("profile") { ProfileScreen(navController, viewModel) }
        composable("settings") { SettingsScreen(navController, viewModel) }
    }
}