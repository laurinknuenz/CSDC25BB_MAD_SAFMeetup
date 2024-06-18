package at.csdc25bb.mad.safmeetup.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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