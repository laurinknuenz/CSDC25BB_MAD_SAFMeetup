@file:OptIn(ExperimentalMaterial3Api::class)

package at.csdc25bb.mad.safmeetup.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


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
                    .padding(vertical = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                updatedContent()
                Button(onClick = {
                    onHide()
                }) {
                    Text("Go back")
                }
            }
        }
    }
}