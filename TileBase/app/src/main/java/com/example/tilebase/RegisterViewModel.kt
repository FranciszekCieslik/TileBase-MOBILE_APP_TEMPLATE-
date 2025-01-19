package com.example.tilebase

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class RegisterViewModel : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    private val firebaseAuth = FirebaseAuth.getInstance()

    // Aktualizowanie emaila
    fun onEmailChange(newEmail: String) {
        _state.value = _state.value.copy(email = newEmail)
    }

    // Aktualizowanie hasła
    fun onPasswordChange(newPassword: String) {
        _state.value = _state.value.copy(password = newPassword)
    }

    // Funkcja do rejestracji użytkownika za pomocą emaila i hasła
    fun register(navController: NavController) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            firebaseAuth.createUserWithEmailAndPassword(_state.value.email, _state.value.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navController.navigate("login")
                    } else {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = task.exception?.message
                        )
                        navController.navigate("register")
                    }
                }
        }
    }

    // Funkcja do logowania użytkownika za pomocą Google
    fun signInWithGoogle(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_WEB_CLIENT_ID") // Zamień na swój Web Client ID
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        val signInIntent = googleSignInClient.signInIntent
        (context as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Obsługa wyniku logowania przez Google
    fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>, navController: NavController) {
        viewModelScope.launch {
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            navController.navigate("main")
                        } else {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = authTask.exception?.message
                            )
                            navController.navigate("register")
                        }
                    }
            } catch (e: ApiException) {
                _state.value = _state.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}



