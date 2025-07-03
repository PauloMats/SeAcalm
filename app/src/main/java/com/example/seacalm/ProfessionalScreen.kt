package com.example.seacalm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfessionalScreen() {
 val themeViewModel: ThemeViewModel = viewModel()
 val selectedTheme by themeViewModel.theme.collectAsState(initial = "light")

 val backgroundColor = if (selectedTheme == "dark") Color(0xFF191970) else Color.White
 val contentColor = if (selectedTheme == "dark") Color.White else Color(0xFF4169E1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
 Text(text = "List of Professionals Placeholder", color = contentColor)
 }
}