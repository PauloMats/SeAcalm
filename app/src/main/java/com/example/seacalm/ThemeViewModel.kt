package com.example.seacalm

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeViewModel : ViewModel() {
    private val _selectedTheme = MutableStateFlow("light") // Initial theme is light
    val selectedTheme: StateFlow<String> = _selectedTheme

    fun updateTheme(theme: String) {
        _selectedTheme.value = theme
    }
}