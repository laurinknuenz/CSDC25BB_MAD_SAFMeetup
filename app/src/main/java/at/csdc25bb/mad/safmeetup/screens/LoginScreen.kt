package at.csdc25bb.mad.safmeetup.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
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
import at.csdc25bb.mad.safmeetup.composables.outlinedTextField
import at.csdc25bb.mad.safmeetup.navigation.Screen

@Composable
fun LoginScreen(navController: NavController) {
    Column(verticalArrangement = Arrangement.Top) {
        RegisterLoginHeader()
        FullSizeCenteredColumn(verticalArrangement = Arrangement.SpaceAround) {
            var username = ""
            var password = ""

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(IntrinsicSize.Min)
            ) {
                val focusRequester = FocusRequester()

                Column {
                    TitleSubtitleText(title = "Login", subtitle = "Please sign in to continue.")
                    username = outlinedTextField("Username", focusRequester)
                    password = outlinedTextField(
                        label = "Password",
                        bottomPadding = 20.dp,
                        focusRequester = focusRequester,
                        lastField = true,
                        password = true
                    )
                }
                var errorMessage by remember { mutableStateOf("") }
                AppButton(
                    text = "Click to Login",
                    onClick = {
                        navController.navigate(Screen.Dashboard.route)
                        //TODO: Make login work with API
                    },
                )
                ErrorMessageText(errorMessage)
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

