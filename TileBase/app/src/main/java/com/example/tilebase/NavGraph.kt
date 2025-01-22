package com.example.tilebase

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "register") {
        composable("login") { LoginScreen(navController,viewModel()) }
        composable("register") {RegisterScreen(navController,viewModel()) }
        composable("main") { MainScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("settings") { SettingsScreen(navController, viewModel()) }
    }
}