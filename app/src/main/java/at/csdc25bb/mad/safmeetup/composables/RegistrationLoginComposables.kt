package at.csdc25bb.mad.safmeetup.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.csdc25bb.mad.safmeetup.R

@Composable
fun RegisterLoginHeader() {
    FullSizeCenteredColumn(
        modifier = Modifier
            .requiredHeight(LocalConfiguration.current.screenHeightDp.dp / 3)
            .background(MaterialTheme.colorScheme.secondary),
    ) {
        Image(
            modifier = Modifier.padding(bottom = 30.dp),
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "Logo"
        )
        TextWithGoogleFont(
            text = "Sports and Friends Meetup",
            font = "Graduate",
            size = 20.sp
        )
        TextWithGoogleFont(
            text = "Connect, Exercise and Have Fun!",
            font = "Lobster Two",
            size = 25.sp
        )
    }
}

@Composable
fun loginRegisterTextField(
    label: String,
    focusRequester: FocusRequester,
    lastField: Boolean = false,
    bottomPadding: Dp = 0.dp,
    password: Boolean = false
): String {
    val focusManager = LocalFocusManager.current
    var variable by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .padding(bottom = bottomPadding)
            .focusRequester(focusRequester),
        value = variable,
        onValueChange = {
            variable = it
        },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (password) {
            if (visible) VisualTransformation.None else PasswordVisualTransformation()
        } else VisualTransformation.None,
        trailingIcon = {
            if (password) {
                IconButton(
                    content = {
                        Icon(
                            imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (visible) "Hide password" else "Show password",
                        )
                    },
                    onClick = { visible = !visible },
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = if (lastField) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = { focusManager.clearFocus() }
        ),
    )
    return variable
}

@Composable
fun BottomViewSwitcher(question: String, highlightedText: String, navigate: () -> Unit) {
    Row(verticalAlignment = Alignment.Bottom) {
        Text(text = question)
        ClickableText(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                ) { append(highlightedText) }
            },
            onClick = { navigate() }
        )
    }
}

@Composable
fun ErrorMessageText(errorMessage: String, modifier: Modifier = Modifier) {
    Text(
        text = errorMessage,
        style = TextStyle(fontSize = 12.sp, color = Color.Red),
        modifier = modifier.padding(top = 4.dp)
    )
}