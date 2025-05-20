package com.example.seacalm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seacalm.ui.theme.SeAcalmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authRepository = AuthRepository(applicationContext)
            val themeViewModel: ThemeViewModel = viewModel()
            val authState by authRepository.authState.collectAsState()
            val navController = rememberNavController()

            LaunchedEffect(authState) {
                if (authState) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
            SeAcalmTheme {
                NavHost(navController = navController, startDestination = if (authState) "home" else "login")  {
                    composable("login") { 
                        LoginScreen(
                            navController = navController,
                            themeViewModel = themeViewModel,
                            onRegisterClick = { navController.navigate("register") }
                        ) 
                    }
                    composable("register") {
                        RegisterScreen(
                            navController = navController,
                            themeViewModel = themeViewModel

                        )
                    }                                        
                    composable("home") {
                        HomeScreen(navController = navController)
                    }
                    composable("chat") {
                        ChatScreen()
                    }
                    composable("emergency") {
                        EmergencyScreen()
                    }
                    composable("profile") {
                        ProfileScreen(navController = navController)
                    }
                    composable("history") {
                        HistoryScreen()
                    }
                    composable("professional") {
                        ProfessionalScreen()
                    }
                    composable("premium") {
                        PremiumScreen()
                    }
                }
            }
        }
    }
}

// Placeholder composables for new routes
@Composable
fun HistoryScreen() { Text(text = "History Screen") }
@Composable
fun ProfessionalScreen() { Text(text = "Professional Screen") }
@Composable
fun PremiumScreen() { Text(text = "Premium Screen") }