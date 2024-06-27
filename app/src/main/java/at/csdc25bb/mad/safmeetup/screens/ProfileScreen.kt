package at.csdc25bb.mad.safmeetup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import at.csdc25bb.mad.safmeetup.R
import at.csdc25bb.mad.safmeetup.composables.AppButton
import at.csdc25bb.mad.safmeetup.composables.DashboardProfileBottomBar
import at.csdc25bb.mad.safmeetup.composables.ProfilePageTopBar
import at.csdc25bb.mad.safmeetup.composables.profileDetailLine
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
                .fillMaxSize()
        ) {
            var userProfileSelected by remember { mutableStateOf(true) }
            ProfilePageTopBar(userProfileSelected) { value -> userProfileSelected = value }
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = profilePadding)
                            .padding(top = 10.dp)
                            .padding(bottom = 15.dp)
                            .fillMaxWidth()
                    ) {
                        if (userProfileSelected) UserProfile() else TeamProfile()
                    }
                }
                item {
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
    }
}


@Composable
fun UserProfile(
    profilePicture: Painter = painterResource(id = R.drawable.logo_app),
    userName: String = "laurinknunz",
    firstName: String = "Laurin",
    lastName: String = "Knünz",
    email: String = "laurin.knunz@gmail.com"
) {
    var currentFirstName by remember { mutableStateOf(firstName) }
    var currentLastName by remember { mutableStateOf(lastName) }
    Column {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Image(
                painter = profilePicture,
                contentDescription = "profile picture",
                modifier = Modifier
                    .size(170.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outline)
            )
            Text(
                text = "$currentFirstName $currentLastName",
                style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        Column {
            profileDetailLine(name = "Username", value = userName)
            profileDetailLine(name = "Password", value = "●●●●●●●●", password = true)
            currentFirstName = profileDetailLine(name = "First name", value = firstName)
            currentLastName = profileDetailLine(name = "Last name", value = lastName)
            profileDetailLine(name = "E-Mail", value = email)
        }
    }
}

@Composable
fun TeamProfile() {
    Text(text = "Team profile")
}