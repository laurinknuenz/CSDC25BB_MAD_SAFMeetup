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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.csdc25bb.mad.safmeetup.composables.AppButton
import at.csdc25bb.mad.safmeetup.composables.BottomViewSwitcher
import at.csdc25bb.mad.safmeetup.composables.ErrorMessageText
import at.csdc25bb.mad.safmeetup.composables.FullSizeCenteredColumn
import at.csdc25bb.mad.safmeetup.composables.RegisterLoginHeader
import at.csdc25bb.mad.safmeetup.composables.TitleSubtitleText
import at.csdc25bb.mad.safmeetup.composables.loginRegisterTextField
import at.csdc25bb.mad.safmeetup.composables.minimalCheckbox

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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(IntrinsicSize.Min)
            ) {
                val focusRequester = FocusRequester()

                Column {
                    TitleSubtitleText(
                        title = "Register",
                        subtitle = "Enter your data to register."
                    )
                    username = loginRegisterTextField("Username", focusRequester)
                    password = loginRegisterTextField(
                        label = "Password",
                        focusRequester = focusRequester,
                        password = true
                    )
                    Divider(modifier = Modifier.padding(top = 8.dp))
                    firstName = loginRegisterTextField("First Name", focusRequester)
                    lastName = loginRegisterTextField("Last Name", focusRequester)
                    email = loginRegisterTextField("E-Mail Address", focusRequester, true)
                    Divider(modifier = Modifier.padding(top = 8.dp))
                    AnimatedVisibility(visible = checked) {
                        teamCode = loginRegisterTextField("Team Code", focusRequester)
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
                var errorMessage by remember { mutableStateOf("") }
                AppButton(
                    text = "Click to Register",
                    onClick = {
                        errorMessage = "Username is already used."
                    }, //TODO: Make registration work with API
                )
                ErrorMessageText(
                    errorMessage,
                    modifier = Modifier.padding(bottom = 13.dp)
                )
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