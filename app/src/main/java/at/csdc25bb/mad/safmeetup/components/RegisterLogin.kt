package at.csdc25bb.mad.safmeetup.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
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
fun outlinedTextField(label: String, bottomPadding: Dp = 0.dp): String {
    var variable by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier.padding(bottom = bottomPadding),
        value = variable,
        onValueChange = { variable = it },
        label = { Text(label) }
    )
    return variable
}

@Composable
fun FullSizeCenteredColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
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
fun minimalCheckbox(): Boolean {
    var checked by remember { mutableStateOf(false) }
    Checkbox(
        checked = checked,
        onCheckedChange = { checked = it }
    )
    return checked
}