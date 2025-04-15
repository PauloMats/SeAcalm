data class NavigationItem(val title: String, val route: String)

val bottomNavigationItems = listOf(
    NavigationItem(title = "Home", route = "home"),
    NavigationItem(title = "Chat", route = "chat"),
    NavigationItem(title = "Profile", route = "profile"),
    NavigationItem(title = "Emergency", route = "emergency")
)