package at.csdc25bb.mad.safmeetup.composables

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun profileDetailLine(name: String = "", value: String, password: Boolean = false): String {
    var editMode by remember { mutableStateOf(false) }
    var currentValue by remember { mutableStateOf(value) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 5.dp)
    ) {
        var targetColor by remember { mutableStateOf(Color.Black) }
        val passwordTextColor by animateColorAsState(
            targetValue = targetColor,
            animationSpec = tween(durationMillis = if (targetColor == Color.Black) 2000 else 150),
            label = "Animates password color change"
        )

        Text(text = "$name: ", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
        if (!editMode)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentValue,
                    style = TextStyle(
                        fontSize = if (!password) 18.sp else 12.sp,
                        color = if (!password) Color.Black else passwordTextColor
                    )
                )
                CustomIconButton(
                    onClick = { editMode = true },
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Enable edit mode"
                    )
                }
            }
        else if (password) ProfilePasswordFieldGroup(onFinishEditing = {
            editMode = false
            targetColor = Color(0xFF2DC533)
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({ targetColor = Color.Black }, 2000)
        }, onCancel = { editMode = false })
        else ProfileTextField(
            initialValue = currentValue,
            onFinishEditing = { editMode = false },
            onChangeSuccess = { newValue -> currentValue = newValue })
    }
    return currentValue
}

@Composable
fun ProfileTextField(
    initialValue: String,
    onFinishEditing: () -> Unit,
    onChangeSuccess: (String) -> Unit = {},
    password: Boolean = false,
    icon: ImageVector = Icons.Outlined.Check,
    label: String = ""
) {
    var value by remember { mutableStateOf(initialValue) }
    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        trailingIcon = {
            IconButton(onClick = {
                onFinishEditing()
                // TODO: Make the API call, if success, execute onChangeSuccess
                if (true) onChangeSuccess(value)
            }) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Disable edit mode",
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
        label = { Text(label) }
    )
}

@Composable
fun ProfilePasswordFieldGroup(onFinishEditing: () -> Unit, onCancel: () -> Unit) {
    val oldPassword by remember { mutableStateOf("") }
    val newPassword by remember { mutableStateOf("") }
    Column {
        ProfileTextField(
            initialValue = oldPassword,
            onFinishEditing = onCancel,
            password = true,
            icon = Icons.Default.Cancel,
            label = "Old Password"
        )
        ProfileTextField(
            initialValue = newPassword,
            onFinishEditing = {
                // TODO: Make the API call here, if successful: onFinishEditing()
                if (true) onFinishEditing()
            },
            password = true,
            label = "New Password"
        )
    }
}