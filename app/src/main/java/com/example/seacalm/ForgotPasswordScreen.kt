package com.example.seacalm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ForgotPasswordScreen(navController: NavController, themeViewModel: ThemeViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    val selectedTheme by themeViewModel.theme.collectAsState()

    val backgroundColor = if (selectedTheme == "dark") Color(0xFF191970) else Color.White // Navy blue for dark
    val contentColor = if (selectedTheme == "dark") Color.White else Color(0xFF4169E1) // Shades of blue for dark, blue for light

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Forgot Password",
            style = MaterialTheme.typography.headlineLarge,
            color = contentColor
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = contentColor) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = contentColor,
                unfocusedBorderColor = contentColor,
                cursorColor = contentColor,
                focusedLabelColor = contentColor,
                unfocusedLabelColor = contentColor,
                textColor = contentColor
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isNotBlank()) {
                    val authRepository = AuthRepository()
                    authRepository.sendPasswordResetEmail(email).let { result ->
                        if (result.value) {
                            message = "If an account with that email exists, a reset email has been sent."
                        } else {
                            message = "Failed to send reset email. Please check the email address."
                        }
                    }
                } else {
                    message = "Please enter your email address."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = contentColor, contentColor = backgroundColor)
        ) {
            Text("Send Reset Email")
        }

        Spacer(modifier = Modifier.height(16.dp))

        message?.let {
            Text(
                text = it,
                color = contentColor
            )
        }

        Spacer(modifier = Modifier.height(32.dp))


        OutlinedButton(
            onClick = { navController.popBackStack() }, // Navigate back to LoginScreen
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = contentColor)
        ) {
            Text("Back to Login")
        }
    }