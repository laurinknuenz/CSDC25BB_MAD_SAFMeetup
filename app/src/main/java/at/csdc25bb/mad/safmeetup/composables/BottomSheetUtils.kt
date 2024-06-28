@file:OptIn(ExperimentalMaterial3Api::class)

package at.csdc25bb.mad.safmeetup.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BottomSheet(showBottomSheet: Boolean, onHide: () -> Unit, content: @Composable () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val updatedContent by rememberUpdatedState(content)

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { onHide() },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.9f))
                {
                    updatedContent()
                    AppButton(
                        "Go back", onClick = {
                            onHide()
                        }, modifier = Modifier
                            .padding(top = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun bottomSheetTextField(label: String): String {
    var value by remember { mutableStateOf("") }
    OutlinedTextField(
        label = { Text(text = label) },
        value = value,
        onValueChange = { newValue -> value = newValue },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    return value
}

@Composable
fun BottomSheetMessage(message: String){
    Text(
        text = message,
        style = TextStyle(fontSize = 14.sp),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
    )
}