package com.example.seacalm

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EmergencyScreen(
    themeViewModel: ThemeViewModel = viewModel()
) {
    val selectedTheme by themeViewModel.theme.collectAsState(initial = "light")

    val backgroundColor = if (selectedTheme == "dark") Color(0xFF191970) else Color.White
    val contentColor = if (selectedTheme == "dark") Color.White else Color(0xFF4169E1)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Emergency") })
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "In case of emergency:",
                style = MaterialTheme.typography.headlineSmall,
                color = contentColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Placeholder for calling emergency services
            Button(
                onClick = { /* TODO: Implement call to emergency services */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
            ) {
                Text("Call Emergency Services")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Placeholder for contacting a family member
            Button(
                onClick = { /* TODO: Implement contact family member */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = contentColor, contentColor = backgroundColor)
            ) {
                Text("Contact a Family Member")
            }

            // Add more emergency contact options or information here
        }
    }
}