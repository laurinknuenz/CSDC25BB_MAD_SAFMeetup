package at.csdc25bb.mad.safmeetup.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import at.csdc25bb.mad.safmeetup.composables.AppButton
import at.csdc25bb.mad.safmeetup.composables.DashboardProfileBottomBar
import at.csdc25bb.mad.safmeetup.composables.ProfilePageTopBar
import at.csdc25bb.mad.safmeetup.composables.ProfileTextField
import at.csdc25bb.mad.safmeetup.navigation.Screen

@Composable
fun ProfileScreen(navController: NavHostController, manager: Boolean = true) {
    val profilePadding = 15.dp
    Scaffold(
        bottomBar = { DashboardProfileBottomBar(navController, false) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            var userProfileSelected by remember { mutableStateOf(true) }

            Column {
                ProfilePageTopBar(userProfileSelected) { value -> userProfileSelected = value }
                Box(
                    modifier = Modifier
                        .padding(horizontal = profilePadding)
                        .fillMaxWidth()
                ) {
                    if (userProfileSelected) UserProfile() else TeamProfile()
                }
            }

            Column(modifier = Modifier.padding(horizontal = profilePadding)) {
                if (userProfileSelected) {
                    AppButton(
                        text = "Sign out",
                        onClick = { navController.navigate(Screen.Login.route) }) // TODO: make it work
                    AppButton(text = "Delete my Account") // TODO: make it work
                } else
                    Column {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AppButton(
                                text = "Join new Team",
                                modifier = Modifier.fillMaxWidth(0.49f),
                                onClick = { } // TODO: make it work
                            )
                            AppButton(
                                text = "Create new Team",
                                modifier = Modifier.fillMaxWidth(0.96f),
                                onClick = { } // TODO: make it work
                            )
                        }
                        if (manager) AppButton(
                            text = "Delete this Team",
                            onClick = {} // TODO: make it work
                        )
                        else AppButton(
                            text = "Leave this Team",
                            onClick = {} // TODO: make it work
                        )
                    }
            }
        }
    }
}

@Composable
fun UserProfile() {
    Column() {
        ProfileDetailLine(name = "First name", value = "Laurin")
        ProfileDetailLine(name = "Last name", value = "Knünz")
        ProfileDetailLine(name = "E-Mail", value = "laurin.knunz@gmail.com")
    }
}

@Composable
fun TeamProfile() {
    Text(text = "Team profile")
}

@Composable
fun ProfileDetailLine(name: String = "", value: String) {
    var editMode by remember { mutableStateOf(false) }
    var currentValue by remember { mutableStateOf(value) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$name: ")
        if (!editMode)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = currentValue)
                IconButton(onClick = { editMode = true }) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Enable edit mode",
                    )
                }
            }
        else ProfileTextField(
            initialValue = currentValue,
            onFinishEditing = { editMode = false },
            onChangeSuccess = { newValue -> currentValue = newValue })
    }
}