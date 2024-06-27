package at.csdc25bb.mad.safmeetup.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.input.ImeAction
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
fun Title(text: String, topPadding: Dp = 10.dp){
    Text(
        text = text,
        style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(top = topPadding)
    )
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