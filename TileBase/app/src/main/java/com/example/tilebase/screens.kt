package com.example.tilebase

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

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
fun SettingsScreen(navController: NavController,
                   viewModel: RegisterViewModel = viewModel()
) {
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
        Button(onClick = { viewModel.signOut(navController) }) {
            Text("Logout")
        }
    }
}

//-----------------------------------------------------------------------
