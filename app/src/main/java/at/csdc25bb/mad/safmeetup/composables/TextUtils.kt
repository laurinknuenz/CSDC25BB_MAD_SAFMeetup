package at.csdc25bb.mad.safmeetup.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.csdc25bb.mad.safmeetup.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

@Composable
fun TextWithGoogleFont(text: String, font: String, size: TextUnit) {
    Text(
        text = text,
        fontFamily = FontFamily(
            Font(
                googleFont = GoogleFont(font),
                fontProvider = provider
            )
        ),
        fontSize = size
    )
}

@Composable
fun outlinedTextField(
    label: String = "",
    focusRequester: FocusRequester = FocusRequester(),
    lastField: Boolean = false,
    bottomPadding: Dp = 0.dp,
    password: Boolean = false,
    initialValue: String = "",
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit = {}
): String {
    val focusManager = LocalFocusManager.current
    var variable by remember { mutableStateOf(initialValue) }
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .padding(bottom = bottomPadding)
            .focusRequester(focusRequester),
        value = variable,
        onValueChange = {
            variable = it
            onValueChanged(it)
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
fun searchBar(modifier: Modifier): String {
    var searchText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        label = { Text("Enter keywords...") },
        singleLine = true,
        trailingIcon = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(end = 10.dp)
            ) {
                Text(text = "Advanced", style = TextStyle(fontSize = 14.sp))
                Text(text = "Filter", style = TextStyle(fontSize = 14.sp))
            }
        },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search bar")
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    )
    return searchText
}

@Composable
fun TitleSubtitleText(title: String, subtitle: String) {
    Text(text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        ) { append(title) }
    })
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 16.sp)) { append(subtitle) }
        },
        modifier = Modifier.padding(bottom = 20.dp)
    )
}

@Composable
fun minimalCheckbox(): Boolean {
    var checked by remember { mutableStateOf(false) }
    Checkbox(
        checked = checked,
        onCheckedChange = { checked = it }
    )
    return checked
}