package at.csdc25bb.mad.safmeetup.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import at.csdc25bb.mad.safmeetup.composables.Loader
import at.csdc25bb.mad.safmeetup.composables.RegisterLoginHeader
import at.csdc25bb.mad.safmeetup.composables.TitleSubtitleText
import at.csdc25bb.mad.safmeetup.composables.loginRegisterTextField
import at.csdc25bb.mad.safmeetup.data.utils.ResourceState
import at.csdc25bb.mad.safmeetup.navigation.Screen
import at.csdc25bb.mad.safmeetup.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val loginState by authViewModel.loginState.collectAsState()

    Column(verticalArrangement = Arrangement.Top) {
        RegisterLoginHeader()
        FullSizeCenteredColumn(verticalArrangement = Arrangement.SpaceAround) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(IntrinsicSize.Min)
            ) {
                val focusRequester = FocusRequester()

                Column {
                    TitleSubtitleText(title = "Login", subtitle = "Please sign in to continue.")
                    username = loginRegisterTextField("Username", focusRequester)
                    password = loginRegisterTextField(
                        label = "Password",
                        bottomPadding = 20.dp,
                        focusRequester = focusRequester,
                        lastField = true,
                        password = true
                    )
                }
                AppButton(
                    text = "Click to Login",
                    onClick = {
                        authViewModel.login(username, password)
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
    when (loginState) {
        is ResourceState.Loading -> {
            Loader()
        }
        is ResourceState.Success -> {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
        is ResourceState.Error -> {
            errorMessage = (loginState as ResourceState.Error).error
        }

        is ResourceState.Idle -> {
            //DO NOTHING
        }
    }
}

