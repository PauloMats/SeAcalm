package com.example.seacalm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = authRepository.authState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        authRepository.authState.value
    )
    val authState: StateFlow<Boolean> = _authState

    private val _currentUserProfile = authRepository.currentUserProfile.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        authRepository.currentUserProfile.value
    )
    val currentUserProfile: StateFlow<UserProfile?> = _currentUserProfile

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val success = authRepository.signInWithEmailAndPassword(email, password).first()
                if (!success) {
                    _errorMessage.value = "Login failed. Please check your credentials." // Generic error for now
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "An unexpected error occurred during login."
            } finally {
                _loading.value = false
            }
        }
    }

    fun registerWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val success = authRepository.registerWithEmailAndPassword(email, password).first()
                if (!success) {
                    _errorMessage.value = "Registration failed. Please try again." // Generic error for now
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "An unexpected error occurred during registration."
            } finally {
                _loading.value = false
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val success = authRepository.sendPasswordResetEmail(email).first()
                if (!success) {
                    _errorMessage.value = "Failed to send password reset email. Please check your email address."
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "An unexpected error occurred while sending password reset email."
            } finally {
                _loading.value = false
            }
        }
    }

    fun signInWithGoogle(launcher: (intent: android.content.Intent) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                val signInIntent = authRepository.getGoogleSignInClient().signInIntent
                launcher(signInIntent)
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "An unexpected error occurred during Google Sign-In initiation."
                _loading.value = false // Stop loading if initiation fails
            }
        }
    }

    fun handleGoogleSignInResult(result: androidx.activity.ActivityResult) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                authRepository.handleSignInResult(result)
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "An unexpected error occurred during Google Sign-In."
            } finally {
                _loading.value = false
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                authRepository.signOut()
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "An unexpected error occurred during sign out."
            } finally {
                _loading.value = false
            }
        }
    }
}