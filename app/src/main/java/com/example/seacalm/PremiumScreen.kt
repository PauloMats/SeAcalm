package com.example.seacalm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

enum class PlanType {
    PATIENT, PROFESSIONAL
}

@Composable
fun PremiumScreen() {
    val themeViewModel: ThemeViewModel = viewModel()
    val selectedTheme by themeViewModel.theme.collectAsState(initial = "light")

    val backgroundColor = if (selectedTheme == "dark") Color(0xFF191970) else Color.White
    val contentColor = if (selectedTheme == "dark") Color.White else Color(0xFF4169E1)
    val buttonColor = if (selectedTheme == "dark") Color.White else Color(0xFF4169E1)
    val buttonContentColor = if (selectedTheme == "dark") Color(0xFF191970) else Color.White

    var selectedPlanType by remember { mutableStateOf(PlanType.PATIENT) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { selectedPlanType = PlanType.PATIENT },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedPlanType == PlanType.PATIENT) buttonColor else Color.Gray,
                    contentColor = buttonContentColor
                )
            ) {
                Text("Patients")
            }
            Button(
                onClick = { selectedPlanType = PlanType.PROFESSIONAL },
                 colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedPlanType == PlanType.PROFESSIONAL) buttonColor else Color.Gray,
                    contentColor = buttonContentColor
                )
            ) {
                Text("Professionals")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Placeholder for plan information based on selectedPlanType
        if (selectedPlanType == PlanType.PATIENT) {
            Column {
                Text(text = "Patient Plan 1", color = contentColor, style = MaterialTheme.typography.headlineSmall)
                Text(text = "Features: Feature A, Feature B", color = contentColor)
                Text(text = "Price: $X/month", color = contentColor)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Patient Plan 2", color = contentColor, style = MaterialTheme.typography.headlineSmall)
                Text(text = "Features: Feature A, Feature B, Feature C", color = contentColor)
                Text(text = "Price: $Y/month", color = contentColor)
            }
        } else {
            Column {
                 Text(text = "Professional Plan 1", color = contentColor, style = MaterialTheme.typography.headlineSmall)
                Text(text = "Features: Access to History, Messaging", color = contentColor)
                Text(text = "Price: $Z/month", color = contentColor)
                 Spacer(modifier = Modifier.height(8.dp))
                 Text(text = "Professional Plan 2", color = contentColor, style = MaterialTheme.typography.headlineSmall)
                Text(text = "Features: All of Plan 1, Video Calls", color = contentColor)
                Text(text = "Price: $W/month", color = contentColor)
            }
        }

        // Add payment options placeholders here later
         Spacer(modifier = Modifier.height(32.dp))
         Button(onClick = { /* TODO: Implement payment logic */ }) {
            Text("Choose Plan")
        }
    }
}