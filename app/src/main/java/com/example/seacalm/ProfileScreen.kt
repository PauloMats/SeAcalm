package com.example.seacalm

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen(authRepository: AuthRepository = viewModel()) {
    val currentUserProfile by authRepository.currentUserProfile.collectAsState()

    Column {
        if (currentUserProfile != null) {
            Text(text = "Email: ${currentUserProfile!!.email}", style = MaterialTheme.typography.bodyLarge)
        } else {
            Text(text = "No profile information available", style = MaterialTheme.typography.bodyLarge)
        }
    }
}