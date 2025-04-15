package com.example.seacalm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
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
            val themeViewModel: ThemeViewModel = viewModel()
            val selectedTheme by themeViewModel.selectedTheme.collectAsState()
            SeAcalmTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "login"
                 ) {
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
                            themeViewModel = themeViewModel,
                           
                        )
                    }
                    composable("home") { HomeScreen() }
                    composable("profile") { ProfileScreen() }
                    composable("history") { HistoryScreen() }
                    composable("chat") { ChatScreen() }
                    composable("emergency") { EmergencyScreen() }
                    composable("professional") { ProfessionalScreen() }
                    composable("premium") { PremiumScreen() }
                }
            }
        }
    }
}