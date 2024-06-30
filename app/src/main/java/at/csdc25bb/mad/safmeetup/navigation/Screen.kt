package at.csdc25bb.mad.safmeetup.navigation

const val DETAIL_ARGUMENT_KEY = "activityId"

sealed class Screen(val route: String) {
    data object Dashboard : Screen(route = "dashboard")
    data object Login : Screen(route = "login")
    data object Register : Screen(route = "register")
    data object Profile : Screen(route = "profile")
    data object Activity : Screen(route = "activity/{$DETAIL_ARGUMENT_KEY}") {
        fun withId(id: String): String {
            return this.route.replace(oldValue = "{$DETAIL_ARGUMENT_KEY}", newValue = id)
        }
    }
}