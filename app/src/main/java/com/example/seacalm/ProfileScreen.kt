package com.example.seacalm

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(), // Use AuthViewModel
    themeViewModel: ThemeViewModel = viewModel() // Use ThemeViewModel
) {
    val currentUserProfile by authViewModel.currentUserProfile.collectAsState()
    val selectedTheme by themeViewModel.theme.collectAsState(initial = "light")

    var name by remember { mutableStateOf(currentUserProfile?.name ?: "") }
    var age by remember { mutableStateOf(currentUserProfile?.age?.toString() ?: "") }

    // Conditional fields for professional users (placeholders)
    var specialties by remember { mutableStateOf(currentUserProfile?.specialties ?: "") }
    var consultationPrice by remember { mutableStateOf(currentUserProfile?.consultationPrice?.toString() ?: "") }

    val isProfessional = currentUserProfile?.role == UserRole.Professional // Assuming UserProfile has a role field and UserRole enum

    val backgroundColor = if (selectedTheme == "dark") Color(0xFF191970) else Color.White
    val contentColor = if (selectedTheme == "dark") Color.White else Color(0xFF4169E1)

    Scaffold(
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineLarge,
                color = contentColor
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Placeholder for profile image
            Text("Profile Image Placeholder", color = contentColor)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                label = { Text("Name", color = contentColor) },
                // Apply colors based on theme
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = age,
                onValueChange = { age = it },
                label = { Text("Age", color = contentColor) },
                // Apply colors based on theme
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.foundation.text.KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isProfessional) {
                Text("Professional Information", style = MaterialTheme.typography.headlineSmall, color = contentColor)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = specialties, onValueChange = { specialties = it }, label = { Text("Specialties", color = contentColor) })
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = consultationPrice, onValueChange = { consultationPrice = it }, label = { Text("Consultation Price", color = contentColor) }, keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.foundation.text.KeyboardType.Number))
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Placeholder for other profile fields
            Text("Other profile fields placeholder", color = contentColor)
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {
                // TODO: Implement save/update profile logic in AuthViewModel
                // val updatedProfile = UserProfile(currentUserProfile?.userId ?: "", name, age.toIntOrNull(), etc...)
                // authViewModel.updateUserProfile(updatedProfile)
            }) {
                Text("Save Profile")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                authViewModel.signOut()
                navController.navigate("login") { popUpTo("home") { inclusive = true } }
            }) {
                Text("Sign Out")
            }
        }
    }
}