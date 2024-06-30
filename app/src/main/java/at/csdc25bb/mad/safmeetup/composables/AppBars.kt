@file:OptIn(ExperimentalMaterial3Api::class)

package at.csdc25bb.mad.safmeetup.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SwapHorizontalCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import at.csdc25bb.mad.safmeetup.navigation.Screen

@Composable
fun DashboardProfileBottomBar(
    navController: NavController,
    showDashboard: Boolean,
    manager: Boolean = true,
    onActivityCreation: () -> Unit
) {
    BottomAppBar(tonalElevation = 0.dp) {
        Box {
            LightGrayDivider(Modifier.fillMaxWidth())
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
                            onActivityCreation()
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
fun ProfilePageTopBar(
    userProfileSelected: Boolean,
    showDashboard: (Boolean) -> Unit,
    onTeamSwitch: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp)
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        ProfileBarItem(text = "Profile", true, userProfileSelected, null) {
            showDashboard(true)
        }
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 2.dp)
                .width(1.dp),
            color = Color.White
        )
        ProfileBarItem(text = "My Team", false, !userProfileSelected, onTeamSwitch) {
            showDashboard(false)
        }
    }
}

@Composable
fun ProfileBarItem(
    text: String,
    firstButton: Boolean,
    selected: Boolean,
    onTeamSwitch: (() -> Unit)?,
    onClick: () -> Unit
) {
    CustomIconButton(
        modifier = Modifier
            .fillMaxWidth(if (firstButton) 0.5f else 1f)
            .fillMaxHeight(),
        onClick = onClick
    ) {
        val textStyle =
            TextStyle(fontSize = 18.sp, color = if (selected) Color.White else Color(0xFFF8C382))
        if (onTeamSwitch == null) {
            Text(text = text, style = textStyle)
        } else {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = text, style = textStyle, modifier = Modifier.padding(end = 10.dp))
                CustomIconButton(onClick = { if (selected) onTeamSwitch() else onClick() }) {
                    Icon(
                        imageVector = Icons.Default.SwapHorizontalCircle,
                        contentDescription = "Switch teams",
                        tint = if (selected) Color.White else Color(0xFFF8C382)
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityTopBar(
    navigateUp: () -> Unit,
    editMode: Boolean,
    onBeginEditing: () -> Unit,
    onFinishEditing: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(
                "Activity Details",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navigateUp() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(
                onClick = if (editMode) onFinishEditing else onBeginEditing
            ) {
                Icon(
                    imageVector = if (editMode) Icons.Filled.Check else Icons.Filled.Edit,
                    contentDescription = "${if (editMode) "Disable" else "Enable"} edit mode"
                )
            }
        }
    )
}