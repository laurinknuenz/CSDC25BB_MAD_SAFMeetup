package at.csdc25bb.mad.safmeetup.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.csdc25bb.mad.safmeetup.navigation.Screen

@Composable
fun DashboardProfileBottomBar(
    navController: NavController,
    showDashboard: Boolean,
    manager: Boolean = true
) {
    BottomAppBar(tonalElevation = 0.dp) {
        Box {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.outline
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                NavigationBarItem(
                    selected = showDashboard,
                    onClick = { navController.navigate(Screen.Dashboard.route) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Go to dashboard"
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFFFFDEB7))
                )
                if (manager) {
                    Button(
                        onClick = {
                            // TODO: Add activity
                        },
                        shape = RectangleShape,
                        modifier = Modifier.size(50.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add new activity",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                NavigationBarItem(
                    selected = !showDashboard,
                    onClick = { navController.navigate(Screen.Profile.route) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Go to dashboard"
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFFFFDEB7))
                )
            }
        }
    }
}

@Composable
fun ProfilePageTopBar(userProfileSelected: Boolean, onProfileChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(10.dp)
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        ProfileBarItem(text = "Profile", true, userProfileSelected) {
            onProfileChange(true)
        }
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 2.dp)
                .width(1.dp),
            color = Color.White
        )
        ProfileBarItem(text = "My Team", false, !userProfileSelected) {
            onProfileChange(false)
        }
    }
}

@Composable
fun ProfileBarItem(text: String, firstButton: Boolean, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(if (firstButton) 0.5f else 1f)
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 18.sp, color = if (selected) Color.White else Color(
                    0xFFF8C382
                )
            )
        )
    }
}