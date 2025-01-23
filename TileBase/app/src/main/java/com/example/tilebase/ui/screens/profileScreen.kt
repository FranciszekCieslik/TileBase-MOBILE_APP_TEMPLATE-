package com.example.tilebase.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.tilebase.R
import com.example.tilebase.RegisterViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavController,
                  viewModel: RegisterViewModel
) {
    val state = viewModel.state.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController, selectedRoute = "profile")
        },
        topBar = {
            TopAppBar(
                title = { Text("Your Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate("settings")}) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Go to Settings"
                        )
                    }
                },
                backgroundColor = Color.White,
                contentColor = Color.Black,
                elevation = 4.dp
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(state.value.photoUrl.toString() != "null") {
                AsyncImage(
                    model = state.value.photoUrl,
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                        .border(2.dp, Color.Black, CircleShape) // Zaokrąglona granica
                        .clip(CircleShape),
                    placeholder = painterResource(R.drawable.account_icon), // Ikona zastępcza

                )
            }else{
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Default Profile Icon",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))



            // Nazwa użytkownika
            if(state.value.name.isNotEmpty()) {
                Text(
                    text = state.value.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Adres e-mail użytkownika
            Text(
                text = state.value.email,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}