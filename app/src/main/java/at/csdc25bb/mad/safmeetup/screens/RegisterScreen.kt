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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
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
                val focusRequester = FocusRequester()
                val focusManager = LocalFocusManager.current

                Column(modifier = Modifier.width(IntrinsicSize.Max)) {
                    TitleSubtitleText(
                        title = "Register",
                        subtitle = "Enter your data to register."
                    )
                    username = outlinedTextField("Username", focusRequester, focusManager)
                    password = outlinedTextField(
                        label = "Password",
                        focusRequester = focusRequester,
                        focusManager = focusManager,
                        password = true
                    )
                    Divider(modifier = Modifier.padding(top = 8.dp))
                    firstName = outlinedTextField("First Name", focusRequester, focusManager)
                    lastName = outlinedTextField("Last Name", focusRequester, focusManager)
                    email = outlinedTextField("E-Mail Address", focusRequester, focusManager, true)
                    Divider(modifier = Modifier.padding(top = 8.dp))
                    AnimatedVisibility(visible = checked) {
                        teamCode = outlinedTextField("Team Code", focusRequester, focusManager)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        checked = minimalCheckbox()
                        Text(text = "Join Existing Team")
                    }
                }
                Button(
                    modifier = Modifier.padding(bottom = 20.dp),
                            onClick = { } // TODO: Make registration work with API
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