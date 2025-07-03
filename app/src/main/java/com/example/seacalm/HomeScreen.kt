package com.example.seacalm

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController = rememberNavController(),
    themeViewModel: ThemeViewModel = viewModel() // Get ThemeViewModel
    // Add parameters for AuthRepository and FeelingsRepository to provide to ViewModel factory
) {
    val context = LocalContext.current // Get context to initialize repositories
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    NavigationItem("Home", "home"),
                    NavigationItem("Chat", "chat"), NavigationItem("Profile", "profile"), NavigationItem("Emergency", "emergency")), navController = navController
            )
        }
    ) { innerPadding ->
        val selectedTheme by themeViewModel.theme.collectAsState(initial = "light")
        val backgroundColor = if (selectedTheme == "dark") Color(0xFF191970) else Color.White
        val contentColor = if (selectedTheme == "dark") Color.White else Color(0xFF4169E1)

        var feelingText by remember { mutableStateOf("") }
 val homeViewModel: HomeViewModel = viewModel(
 factory = object : ViewModelProvider.Factory {
 override fun <T : ViewModel> create(modelClass: Class<T>): T {
 // Assuming AuthRepository and FeelingsRepository can be created with context
 // In a real app, use dependency injection for better management
 return HomeViewModel(AuthRepository(context), FeelingsRepository()) as T
 }
 }
 )

 val isLoading by homeViewModel.loading.collectAsState()
 val errorMessage by homeViewModel.errorMessage.collectAsState()
 val saveSuccess by homeViewModel.saveSuccess.collectAsState() // Assuming HomeViewModel has a saveSuccess StateFlow

 LaunchedEffect(saveSuccess) {
 if (saveSuccess) {
 // Optional: Show a success message or clear the text field
 feelingText = "" // Clear text field on successful save
 homeViewModel.clearSaveSuccess() // Clear the success state in ViewModel
 }
 }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder for app logo or greeting
            Text(
                text = "Welcome",
                style = MaterialTheme.typography.headlineMedium,
                color = contentColor
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = feelingText,
                onValueChange = { feelingText = it },
                label = { Text("How are you feeling today?", color = contentColor) },
                minLines = 5, // Allow for multi-line input
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
 homeViewModel.saveFeeling(feelingText)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = contentColor, contentColor = backgroundColor)
            ) {
                Text("Save")
            }

            // Placeholder for other content (e.g., personalized recommendations, quick access features)
            Spacer(modifier = Modifier.weight(1f)) // Pushes content upwards

            // Example Placeholder:
            /*
            Text(
                text = "Quick Access to Guided Meditations",
                style = MaterialTheme.typography.titleMedium,
                color = contentColor,
                modifier = Modifier.padding(top = 16.dp)
            )
            */

            if (isLoading) {
 CircularProgressIndicator(color = contentColor)
            }

            if (errorMessage != null) {
 Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }

        }
    }
}