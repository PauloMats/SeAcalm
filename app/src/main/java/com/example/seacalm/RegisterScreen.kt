package com.example.seacalm

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.util.regex.Pattern


@Composable
fun RegisterScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel() // Get AuthViewModel using viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val selectedTheme by themeViewModel.theme.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val isLoading by authViewModel.loading.collectAsState()

    val backgroundColor = if (selectedTheme == "dark") Color(0xFF191970) else Color.White // Navy blue for dark
    val contentColor = if (selectedTheme == "dark") Color.White else Color(0xFF4169E1) // Shades of blue for dark, blue for light

    Scaffold(
        topBar = {
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = contentColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = contentColor,
                ),
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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

    LaunchedEffect(authState) {
        if (authState) {
            navController.navigate("home") { popUpTo("login") { inclusive = true } }
        }
    }

            val emailPattern = remember {
                Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
            }

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    authViewModel.clearErrorMessage() // Clear error when typing
                },
                value = email,
                onValueChange = {
                    email = it
                    authViewModel.clearErrorMessage()
                },
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) {
                        Text(emailError!!, color = Color.Red)
                    }
                },
                trailingIcon = {  },
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
                ),
                onValueChange = {
                    email = it
                    emailError = if (it.isNotEmpty() && !emailPattern.matcher(it).matches()) {
                        "Invalid email format"
                    } else {
                        null
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    authViewModel.clearErrorMessage() },
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
                },
                 isError = !authViewModel.passwordError.collectAsState().value.isNullOrEmpty(),
                supportingText = {
                    if (!authViewModel.passwordError.collectAsState().value.isNullOrEmpty()) {
                        Text(
                            authViewModel.passwordError.collectAsState().value!!,
                            color = MaterialTheme.colorScheme.error)
                    }
                    unfocusedTextColor = contentColor
                ),
                onValueChange = {
                    password = it
                    passwordError = if (it.isNotEmpty() && it.length < 6) {
                        "Password must be at least 6 characters"
                    } else {
                        null
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it
                    authViewModel.clearErrorMessage()
                },
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
                ),
                isError = !authViewModel.confirmPasswordError.collectAsState().value.isNullOrEmpty(),
                supportingText = {
                    if (!authViewModel.confirmPasswordError.collectAsState().value.isNullOrEmpty()) {
                        Text(
                            authViewModel.confirmPasswordError.collectAsState().value!!,
                            color = MaterialTheme.colorScheme.error)
                    }
                    unfocusedTextColor = contentColor
                ),
                supportingText = {
                    if (!authViewModel.confirmPasswordError.collectAsState().value.isNullOrEmpty()) {
                        Text(confirmPasswordError!!, color = Color.Red)
                    }
                },
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = if (it.isNotEmpty() && it != password) {
                        "Passwords do not match"
                    } else {
                        null
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                   authViewModel.registerWithEmailAndPassword(email, password, confirmPassword)
                },
                 enabled = !isLoading, // Disable button when loading
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = contentColor, contentColor = backgroundColor)
            ) {
                 if (isLoading) {
                    CircularProgressIndicator(color = contentColor) // Show loading indicator
                } else {
                    Text("Register")
                    if (emailError == null && passwordError == null && confirmPasswordError == null && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()) {
                        authRepository.registerWithEmailAndPassword(email, password)
                        authRepository.hasAttemptedAuth = true
                    }
                },
                enabled = emailError == null && passwordError == null && confirmPasswordError == null && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = contentColor, contentColor = backgroundColor),
                enabled = !isLoading
            ) {

                Text("Register")
            }
        }
    }
}
@Preview(showBackground = true)