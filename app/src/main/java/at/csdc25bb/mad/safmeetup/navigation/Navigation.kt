package at.csdc25bb.mad.safmeetup.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import at.csdc25bb.mad.safmeetup.screens.ActivityScreen
import at.csdc25bb.mad.safmeetup.screens.DashboardScreen
import at.csdc25bb.mad.safmeetup.screens.LoginScreen
import at.csdc25bb.mad.safmeetup.screens.ProfileScreen
import at.csdc25bb.mad.safmeetup.screens.RegisterScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(
            route = Screen.Activity.route,
            arguments = listOf(navArgument(name = "activityId") { type = NavType.StringType })
        ) { backStackEntry ->
            val activityId = backStackEntry.arguments?.getString("activityId")
            if (activityId != null) ActivityScreen(
                navController = navController,
                activityId = activityId
            )
        }
    }
}