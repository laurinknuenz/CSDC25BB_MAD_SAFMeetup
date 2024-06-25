package at.csdc25bb.mad.safmeetup.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import at.csdc25bb.mad.safmeetup.composables.BottomViewSwitcher
import at.csdc25bb.mad.safmeetup.composables.FullSizeCenteredColumn
import at.csdc25bb.mad.safmeetup.composables.RegisterLoginHeader
import at.csdc25bb.mad.safmeetup.composables.TitleSubtitleText
import at.csdc25bb.mad.safmeetup.composables.outlinedTextField

@Composable
fun LoginScreen(navController: NavController) {
    Column(verticalArrangement = Arrangement.Top) {
        RegisterLoginHeader()
        FullSizeCenteredColumn(verticalArrangement = Arrangement.SpaceAround) {
            var username = ""
            var password = ""

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(IntrinsicSize.Min)) {
                val focusRequester = FocusRequester()
                val focusManager = LocalFocusManager.current

                Column {
                    TitleSubtitleText(title = "Login", subtitle = "Please sign in to continue.")
                    username = outlinedTextField("Username", focusRequester, focusManager)
                    password = outlinedTextField(
                        label = "Password",
                        bottomPadding = 20.dp,
                        focusRequester = focusRequester,
                        focusManager = focusManager,
                        lastField = true,
                        password = true
                    )
                }
                Button(
                    modifier = Modifier.focusRequester(focusRequester)
                        .fillMaxWidth(),
                    shape = RectangleShape,
                    onClick = { }, //TODO: Make login work with API
                ) {
                    Text(text = "Click to Login")
                }
            }
            BottomViewSwitcher(
                question = "Don't have an account yet? ",
                highlightedText = "Sign up"
            ) {
                navController.navigate("register")
            }
        }
    }
}

