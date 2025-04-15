import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController = rememberNavController()) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    NavigationItem("Home", "home"),
                    NavigationItem("Chat", "chat"), NavigationItem("Profile", "profile"), NavigationItem("Emergency", "emergency")), navController = navController
            )
        }
    ) { innerPadding ->
        Text(text = "Home Screen", modifier = Modifier.padding(innerPadding))
    }
}