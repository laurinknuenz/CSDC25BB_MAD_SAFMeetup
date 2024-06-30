package at.csdc25bb.mad.safmeetup.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign

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
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        shape = RectangleShape,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(text = text, textAlign = TextAlign.Center)
    }
}

@Composable
fun CustomIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center,
    ) { content() }
}

data class InfoDialogParams(
    val icon: ImageVector = Icons.Default.Info,
    val warning: Boolean = false,
    val title: String = "",
    val dialogText: String = "",
    val confirmButtonText: String = "",
    val onConfirmation: () -> Unit = {}
)

@Composable
fun InformationDialog(
    icon: ImageVector,
    warning: Boolean,
    title: String,
    dialogText: String,
    confirmButtonText: String,
    onConfirmation: () -> Unit,
    closeDialog: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = "Icon of the dialog",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (warning) Text(text = "WARNING!")
                Text(text = title, textAlign = TextAlign.Center)
            }
        },
        text = { Text(text = "$dialogText Do you want to continue?") },
        onDismissRequest = { closeDialog() },
        confirmButton = {
            TextButton(onClick = {
                onConfirmation()
                closeDialog()
            }) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                closeDialog()
            }) {
                Text(text = "Cancel")
            }
        }
    )
}