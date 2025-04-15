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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun LoginScreen(
    navController: NavController,
    authRepository: AuthRepository,
    themeViewModel: ThemeViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val selectedTheme by themeViewModel.theme.collectAsState(initial = "light")

    val backgroundColor = if (selectedTheme == "dark") Color(0xFF191970) else Color.White
    val contentColor = if (selectedTheme == "dark") Color.White else Color(0xFF4169E1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sea",
            style = MaterialTheme.typography.headlineLarge,
            color = contentColor
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { themeViewModel.updateTheme("light") }) {
                Text(
                    "Light Theme",
                    color = if (selectedTheme == "light") Color.White else contentColor
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { themeViewModel.updateTheme("dark") }) {
                Text(
                    "Dark Theme",
                    color = if (selectedTheme == "dark") Color.White else contentColor
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = contentColor) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = contentColor, unfocusedBorderColor = contentColor, cursorColor = contentColor, focusedLabelColor = contentColor, unfocusedLabelColor = contentColor, textColor = contentColor)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = contentColor) },
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = contentColor, unfocusedBorderColor = contentColor, cursorColor = contentColor, focusedLabelColor = contentColor, unfocusedLabelColor = contentColor, textColor = contentColor)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { /* TODO: Implement Forgot Password */ }) {
            Text("Forgot Password?", color = contentColor)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                authRepository.signInWithEmailAndPassword(email, password)
            },
            modifier = Modifier.fillMaxWidth(),
            colors =
            ButtonDefaults.buttonColors(containerColor = contentColor, contentColor = backgroundColor)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }


        val authState by authRepository.authState.collectAsState(initial = AuthState.SignedOut)
        LaunchedEffect(authState) {
            when (authState) {
                AuthState.SignedIn -> {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                is AuthState.Failed -> errorMessage = (authState as AuthState.Failed).message
                AuthState.SignedOut -> {}
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: Implement Google Sign-In
       Button(
           onClick = { /* TODO: Implement Google Sign-In */ },
           modifier = Modifier.fillMaxWidth(),
           colors = ButtonDefaults.buttonColors(containerColor = contentColor, contentColor = backgroundColor)
       ) {
           Text("Sign in with Google (Not implemented yet)")
       }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { navController.navigate("register") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = contentColor)
        ) {
            Text("Register")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController(), AuthRepository(), ThemeViewModel())
}