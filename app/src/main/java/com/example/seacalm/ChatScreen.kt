package com.example.seacalm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun ChatScreen(themeViewModel: ThemeViewModel = viewModel()) {
 val selectedTheme by themeViewModel.theme.collectAsState(initial = "light")

    val backgroundColor = if (selectedTheme == "dark") Color(0xFF191970) else Color.White
    val contentColor = if (selectedTheme == "dark") Color.White else Color(0xFF4169E1)

    var messageInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Placeholder for chat messages
        Column(modifier = Modifier.weight(1f)) {
            Text("Chat Messages Placeholder", color = contentColor)
            // TODO: Display chat messages here
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(value = messageInput, onValueChange = { messageInput = it }, modifier = Modifier.weight(1f), label = { Text("Enter message") })
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /* TODO: Send message */ }) {
                Text("Send")
            }
        }
    }
}