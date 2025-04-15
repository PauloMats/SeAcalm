package com.example.seacalm

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = viewModel(),
    authRepository: AuthRepository = remember { AuthRepository() }
) {
    var email by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
        // TODO: App Logo
        Text(
            text = "Sea", // Placeholder
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
                focusedTextColor = contentColor,
                unfocusedTextColor = contentColor
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = contentColor) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = contentColor,
                unfocusedBorderColor = contentColor,
                cursorColor = contentColor,
                focusedLabelColor = contentColor,
                unfocusedLabelColor = contentColor,
                focusedTextColor = contentColor,
                unfocusedTextColor = contentColor
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password", color = contentColor) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = contentColor,
                unfocusedBorderColor = contentColor,
                cursorColor = contentColor,
                focusedLabelColor = contentColor,
                unfocusedLabelColor = contentColor,
                focusedTextColor = contentColor,
                unfocusedTextColor = contentColor
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        LaunchedEffect(Unit) {
            authRepository.authState.collectLatest { isAuthenticated ->
                if (isAuthenticated) {
                    navController.navigate("home") { popUpTo("login") { inclusive = true } }
                } else if (!isAuthenticated && authRepository.hasAttemptedAuth) {
                    println("Registration Failed!")
                    // TODO: Show error message
                }
            }
        }

        if (password == confirmPassword) {
            Button(
                onClick = {
                    authRepository.registerWithEmailAndPassword(email, password)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = contentColor, contentColor = backgroundColor)
            ) {
                Text("Register")
            }
        } else {
            Text("Passwords do not match", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Back to Login", color = contentColor)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview(){
    RegisterScreen(navController = rememberNavController())
}