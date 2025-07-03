package com.example.seacalm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.seacalm.ui.theme.SeAcalmTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current // Get context for AuthRepository
            val authRepository = remember { AuthRepository(context) } // Initialize AuthRepository once
            val themeViewModel: ThemeViewModel = viewModel()
            val authState by authRepository.authState.collectAsState()
            val navController = rememberNavController()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

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

            SeAcalmTheme(darkTheme = themeViewModel.theme.collectAsState().value == "dark") { // Apply theme based on ThemeViewModel
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            Spacer(Modifier.height(12.dp))
                            Text("Navigation", modifier = Modifier.padding(16.dp))
                            Divider()
                            NavigationDrawerItem(
                                label = { Text("Login") },
                                selected = false,
                                onClick = {
                                    navController.navigate("login")
                                    scope.launch { drawerState.close() }
                                }
                            )
                             NavigationDrawerItem(
                                label = { Text("Register") },
                                selected = false,
                                onClick = {
                                    navController.navigate("register")
                                    scope.launch { drawerState.close() }
                                }
                            )
                             NavigationDrawerItem(
                                label = { Text("Forgot Password") },
                                selected = false,
                                onClick = {
                                    navController.navigate("forgotPassword")
                                    scope.launch { drawerState.close() }
                                }
                            )
                            Divider()
                            NavigationDrawerItem(
                                label = { Text("Home") },
                                selected = false,
                                onClick = {
                                    navController.navigate("home")
                                    scope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Chat") },
                                selected = false,
                                onClick = {
                                    navController.navigate("chat")
                                    scope.launch { drawerState.close() }
                                }
                            )
                             NavigationDrawerItem(
                                label = { Text("Emergency") },
                                selected = false,
                                onClick = {
                                    navController.navigate("emergency")
                                    scope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Profile") },
                                selected = false,
                                onClick = {
                                    navController.navigate("profile")
                                    scope.launch { drawerState.close() }
                                }
                            )
                             NavigationDrawerItem(
                                label = { Text("History") },
                                selected = false,
                                onClick = {
                                    navController.navigate("history")
                                    scope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Professional") },
                                selected = false,
                                onClick = {
                                    navController.navigate("professional")
                                    scope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Premium") },
                                selected = false,
                                onClick = {
                                    navController.navigate("premium")
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(navController.currentDestination?.route ?: "Sea") }, // Display current screen title
                                navigationIcon = {
                                     IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                         Icon(Icons.Filled.Menu, contentDescription = "Menu")
                                     }
                                }
                            )
                        }
                    ) { paddingValues ->
                        NavHost(navController = navController, startDestination = if (authState) "home" else "login", modifier = Modifier.padding(paddingValues))  {
                            composable("login") {
                                LoginScreen(
                                    navController = navController,
                                    authViewModel = viewModel(), // AuthViewModel provided by NavHost
                                    themeViewModel = themeViewModel
                                )
                            }
                            composable("register") {
                                RegisterScreen(
                                    navController = navController,
                                    authViewModel = viewModel(), // AuthViewModel provided by NavHost
                                    themeViewModel = themeViewModel
                                )
                            }
                             composable("forgotPassword") {
                                 ForgotPasswordScreen(
                                     navController = navController,
                                     authViewModel = viewModel() // AuthViewModel provided by NavHost
                                 )
                             }
                            composable("home") {
                                HomeScreen(
                                    navController = navController,
                                    homeViewModel = viewModel(factory = HomeViewModelFactory(authRepository, remember { FeelingsRepository(FirebaseFirestore.getInstance())})) // Provide dependencies to HomeViewModel
                                )
                            }
                            composable("chat") {
                                ChatScreen()
                            }
                            composable("emergency") {
                                EmergencyScreen()
                            }
                            composable("profile") { ProfileScreen() }
                            composable("history") { HistoryScreen() }
                            composable("professional") { ProfessionalScreen() }
                            composable("premium") { PremiumScreen() }
                        }
                    }
                }
            }
        }
    }
}

// Factory to provide dependencies to HomeViewModel
class HomeViewModelFactory(
    private val authRepository: AuthRepository,
    private val feelingsRepository: FeelingsRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(authRepository, feelingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
