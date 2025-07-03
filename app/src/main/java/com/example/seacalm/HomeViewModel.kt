package com.example.seacalm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val feelingsRepository: FeelingsRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun saveFeeling(feelingText: String) {
        _loading.value = true
        _errorMessage.value = null

        val userId = authRepository.getCurrentUser()?.uid

        if (userId == null) {
            _errorMessage.value = "User not logged in."
            _loading.value = false
            return
        }

        if (feelingText.isBlank()) {
            _errorMessage.value = "Feeling text cannot be empty."
            _loading.value = false
            return
        }

        val feelingEntry = FeelingEntry(
            userId = userId,
            feelingText = feelingText,
            timestamp = Timestamp.now()
        )

        viewModelScope.launch {
            feelingsRepository.saveFeelingEntry(feelingEntry).collect { success ->
                _loading.value = false
                if (!success) {
                    _errorMessage.value = "Failed to save feeling. Please try again."
                } else {
                    // Optionally, clear the text field or show a success message
                }
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}