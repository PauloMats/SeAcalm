package com.example.seacalm


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }

    val selectedTheme by themeViewModel.theme.collectAsState(initial = "light")

    val backgroundColor = if (selectedTheme == "dark") Color(0xFF191970) else Color.White
    val contentColor = if (selectedTheme == "dark") Color.White else Color(0xFF4169E1)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                onValueChange = {
                    email = it
                    emailError = null // Clear error when typing
                },
                label = { Text("Email", color = contentColor) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = contentColor,
                    unfocusedBorderColor = contentColor,
                    cursorColor = contentColor,
                    focusedLabelColor = contentColor,
                    unfocusedLabelColor = contentColor,
                    textColor = contentColor
                ),
                isError = !emailError.isNullOrEmpty(),
                supportingText = {
                    if (!emailError.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = emailError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null // Clear error when typing
                },
                label = { Text("Password", color = contentColor) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = contentColor,
                    unfocusedBorderColor = contentColor,
                    cursorColor = contentColor,
                    focusedLabelColor = contentColor,
                    unfocusedLabelColor = contentColor,
                    textColor = contentColor
                ),
                isError = !passwordError.isNullOrEmpty(),
                supportingText = {
                    if (!passwordError.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = passwordError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("forgotPassword") }) {
                Text("Forgot Password?", color = contentColor)
            }

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
                    if (isValidInput(email, password)) {
                        authRepository.signInWithEmailAndPassword(email, password)
                            .observeForever { success ->
                                if (success) {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    errorMessage = when (val exception = authRepository.lastError) {
                                        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> {
                                            if (exception.errorCode == "ERROR_INVALID_EMAIL") {
                                                "Invalid email address"
                                            } else {
                                                "Incorrect email or password"
                                            }
                                        }
                                        else -> "Login failed. Please try again."
                                    }
                                }
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = contentColor, contentColor = backgroundColor)
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))

            val launcher = rememberLauncherForActivityResult(
                contract = AuthRepository.signInIntent(),
                onResult = { task ->
                    authRepository.handleSignInResult(task)
                }
            )

            val authState by authRepository.authState.collectAsState()

            LaunchedEffect(authState) {
                if (authState) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }

            Button(
                onClick = {
                    val googleSignInClient = authRepository.getGoogleSignInClient()
                    authRepository.signInWithGoogle { intent ->
                        launcher.launch(intent)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = contentColor)
            ) {
                Text("Sign in with Google")
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
}

private fun isValidInput(email: String, password: String): Boolean {
    var isValid = true
    if (!isValidEmail(email)) {
        isValid = false
    }
    if (password.length < 6) {
        isValid = false
    }
    return isValid
}

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}