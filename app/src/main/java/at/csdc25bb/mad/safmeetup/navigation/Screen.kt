package at.csdc25bb.mad.safmeetup.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen(route = "dashboard")
    data object Login : Screen(route = "login")
    data object Register : Screen(route = "register")
}