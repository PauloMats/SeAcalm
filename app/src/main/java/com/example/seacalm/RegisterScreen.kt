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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import java.util.regex.Pattern


@Composable
fun RegisterScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = viewModel(),
    authRepository: AuthRepository = remember { AuthRepository() }
) {
    var email by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var registrationError by remember { mutableStateOf<String?>(null) }
    val selectedTheme by themeViewModel.theme.collectAsState()
    val backgroundColor = if (selectedTheme == "dark") Color(0xFF191970) else Color.White // Navy blue for dark
    val contentColor = if (selectedTheme == "dark") Color.White else Color(0xFF4169E1) // Shades of blue for dark, blue for light

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
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

            val emailPattern = remember {
                Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
            }

            OutlinedTextField(
                value = email,
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) {
                        Text(emailError!!, color = Color.Red)
                    }
                },
                trailingIcon = {  },
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
                isError = passwordError != null,
                supportingText = { if (passwordError != null) Text(passwordError!!, color = Color.Red) },
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
                isError = confirmPasswordError != null,
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
                ),
                supportingText = {
                    if (confirmPasswordError != null) {
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

            LaunchedEffect(Unit) {
                authRepository.authState.collectLatest { isAuthenticated ->
                    authRepository.hasAttemptedAuth = false
                    if (isAuthenticated) {
                        navController.navigate("home") { popUpTo("login") { inclusive = true } }
                    } else if (authRepository.hasAttemptedAuth) {
                        registrationError = "Registration failed. Please try again."
                    }
                }
            }

            if (registrationError != null) {
                Text(registrationError!!, color = Color.Red)
            }

            Button(
                onClick = {
                    if (emailError == null && passwordError == null && confirmPasswordError == null && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()) {
                        authRepository.registerWithEmailAndPassword(email, password)
                        authRepository.hasAttemptedAuth = true
                    }
                },
                enabled = emailError == null && passwordError == null && confirmPasswordError == null && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = contentColor, contentColor = backgroundColor)
            ) {
                Text("Register")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview(){
    RegisterScreen(navController = rememberNavController())
}