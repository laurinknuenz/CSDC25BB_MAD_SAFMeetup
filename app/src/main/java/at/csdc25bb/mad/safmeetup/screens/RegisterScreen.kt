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
import at.csdc25bb.mad.safmeetup.composables.minimalCheckbox
import at.csdc25bb.mad.safmeetup.data.utils.ResourceState
import at.csdc25bb.mad.safmeetup.navigation.Screen
import at.csdc25bb.mad.safmeetup.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var inviteCode by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    val registerState by authViewModel.registerState.collectAsState()

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
                    firstname = loginRegisterTextField("First Name", focusRequester)
                    lastname = loginRegisterTextField("Last Name", focusRequester)
                    email = loginRegisterTextField("E-Mail Address", focusRequester, true)
                    Divider(modifier = Modifier.padding(top = 8.dp))
                    AnimatedVisibility(visible = checked) {
                        inviteCode = loginRegisterTextField("Team Code", focusRequester)
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
                        authViewModel.register(
                            firstname,
                            lastname,
                            username,
                            email,
                            password,
                            inviteCode
                        )
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
            when (registerState) {
                is ResourceState.Loading -> Loader()
                is ResourceState.Success -> {
                    // Navigate to another screen or show success message
                    Text(text = "Registration Successful!")
                    authViewModel.clearRegisterState()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
                is ResourceState.Error -> {
                    val error = (registerState as ResourceState.Error).error
                    Text(text = "Error: $error")
                }
                is ResourceState.Idle -> {
                    // Do nothing
                }
            }
        }
    }
}