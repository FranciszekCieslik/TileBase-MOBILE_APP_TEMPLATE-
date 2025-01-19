package com.example.tilebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
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

    fun onEmailChange(newEmail: String) {
        _state.value = _state.value.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        _state.value = _state.value.copy(password = newPassword)
    }

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
                    }
                }
        }
    }

    fun signInWithGoogle(navController: NavController) {
        // Implementacja logowania przez Google
    }
}

