package at.csdc25bb.mad.safmeetup.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.csdc25bb.mad.safmeetup.components.BottomViewSwitcher
import at.csdc25bb.mad.safmeetup.components.FullSizeCenteredColumn
import at.csdc25bb.mad.safmeetup.components.RegisterLoginHeader
import at.csdc25bb.mad.safmeetup.components.TitleSubtitleText
import at.csdc25bb.mad.safmeetup.components.minimalCheckbox
import at.csdc25bb.mad.safmeetup.components.outlinedTextField

@Composable
fun RegisterScreen(navController: NavController) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {
        RegisterLoginHeader()
        FullSizeCenteredColumn(
            modifier = Modifier
                .padding(vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            var username = ""
            var password = ""
            var firstName = ""
            var lastName = ""
            var email = ""
            var teamCode = ""
            var checked by remember { mutableStateOf(false) }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Column(modifier = Modifier.width(IntrinsicSize.Max)) {
                    TitleSubtitleText(
                        title = "Register",
                        subtitle = "Enter your data to register."
                    )
                    username = outlinedTextField("Username")
                    password = outlinedTextField("Password")
                    Divider(modifier = Modifier.padding(top = 8.dp))
                    firstName = outlinedTextField("First Name")
                    lastName = outlinedTextField("Last Name")
                    email = outlinedTextField("E-Mail Address")
                    Divider(modifier = Modifier.padding(top = 8.dp))
                    AnimatedVisibility(visible = !checked) {
                        teamCode = outlinedTextField("Team Code")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        checked = minimalCheckbox()
                        Text(text = "Create New Team")
                    }
                }
                Button(
                    modifier = Modifier.padding(bottom = 20.dp),
                    onClick = { }
                ) {
                    Text(text = "Click to Register")
                }
            }
            BottomViewSwitcher(
                question = "Already have an account? ",
                highlightedText = "Login"
            ) {
                navController.navigate("login")
            }
        }
    }
}