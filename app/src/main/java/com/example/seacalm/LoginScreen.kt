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

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedTheme by remember { mutableStateOf("light") } // "light" or "dark"

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

        // Theme Selection
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            //TODO: implement radio buttons or similar
            Button(onClick = { selectedTheme = "light" }) {
                Text("Light Theme", color = if (selectedTheme == "light") Color.White else contentColor)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { selectedTheme = "dark" }) {
                Text("Dark Theme", color = if (selectedTheme == "dark") Color.White else contentColor)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = contentColor) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = contentColor, unfocusedBorderColor = contentColor, cursorColor = contentColor, focusedLabelColor = contentColor, unfocusedLabelColor = contentColor, textColor = contentColor)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = contentColor) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = contentColor, unfocusedBorderColor = contentColor, cursorColor = contentColor, focusedLabelColor = contentColor, unfocusedLabelColor = contentColor, textColor = contentColor)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { /* TODO: Implement Forgot Password */ }) {
            Text("Forgot Password?", color = contentColor)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* TODO: Implement Login */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = contentColor, contentColor = backgroundColor)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* TODO: Implement Register */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = contentColor)
        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: Implement Google Sign-In
        Button(
            onClick = { /* TODO: Implement Google Sign-In */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = contentColor, contentColor = backgroundColor)
        ) {
            Text("Sign in with Google")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}