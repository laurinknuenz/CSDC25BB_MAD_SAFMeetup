package at.csdc25bb.mad.safmeetup.composables

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun profileDetailLine(
    name: String,
    value: String,
    password: Boolean = false,
    editable: Boolean = true
): String {
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
                if (editable) {
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
    onChangeSuccess: (String) -> Unit = {}
) {
    var value by remember { mutableStateOf(initialValue) }
    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        trailingIcon = {
            IconButton(onClick = {
                // TODO: Make the change API call here
                if (true) { // On success of API call
                    onChangeSuccess(value)
                    onFinishEditing()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Disable edit mode",
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun ProfilePasswordFieldGroup(onFinishEditing: () -> Unit, onCancel: () -> Unit) {
    var errorMessage by remember { mutableStateOf("") }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val oldPassword = profilePasswordTextField(
            onFinishEditing = onCancel,
            icon = Icons.Default.Cancel,
            label = "Old Password"
        )
        profilePasswordTextField(
            onFinishEditing = {
                onFinishEditing()
            },
            onError = { error -> errorMessage = error },
            label = "New Password",
            oldPassword = oldPassword,
            newPassword = true
        )
        ErrorMessageText(errorMessage)
    }
}

@Composable
fun profilePasswordTextField(
    onFinishEditing: () -> Unit,
    onError: (String) -> Unit = {},
    icon: ImageVector = Icons.Outlined.Check,
    label: String = "",
    oldPassword: String = "",
    newPassword: Boolean = false
): String {
    var password by remember { mutableStateOf("") }
    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        trailingIcon = {
            IconButton(onClick = {
                if (!newPassword) onFinishEditing()
                else {
                    // TODO: Make password change API call here
                    if (true) { // On success of API call
                        onFinishEditing()
                    }
                    if (false) // On error of API call
                        onError("Error message.")
                }
            }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Disable edit mode",
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        label = { Text(label) }
    )
    return password
}

@Composable
fun TeamMemberEntry(
    member: List<String>,
    userIsAdmin: Boolean,
    onIconClick: (ImageVector, Boolean, String, String, String, () -> Unit) -> Unit,
    onChangeSuccess: (List<String>?) -> Unit
) {
    val currentMember = member.toMutableList()
    Row(
        modifier = Modifier

            .border(0.1.dp, MaterialTheme.colorScheme.outline)
            .padding(15.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Icon(
                imageVector = if (currentMember[0] == "Admin") Icons.Default.VerifiedUser else Icons.Default.Person,
                contentDescription = "Icon of user",
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(text = currentMember[1])
        }
        Row {
            if (userIsAdmin) {
                if (currentMember[2] != "pending") {
                    if (currentMember[2] != "You") {
                        val memberIsAdmin = currentMember[0] == "Admin"
                        val adminIcon =
                            if (!memberIsAdmin) Icons.Default.VerifiedUser else Icons.Default.PersonOutline
                        val adminAction = "${if (memberIsAdmin) "Revoke" else "Grant"} rights"
                        CustomIconButton(
                            onClick = {
                                onIconClick(
                                    adminIcon,
                                    false,
                                    if (memberIsAdmin) "Admin Removal" else "Admin Addition",
                                    "You're about to ${if (memberIsAdmin) "revoke the admin rights of" else "grant admin rights to"} ${currentMember[1]}.",
                                    adminAction
                                ) {
                                    if (!memberIsAdmin) {
                                        // TODO: API call to make user admin
                                        currentMember[0] = "Admin"
                                    } else {

                                        // TODO: API call to remove users admin rights
                                        currentMember[0] = "User"
                                    }
                                    onChangeSuccess(currentMember)
                                }
                            },
                        ) {
                            Icon(
                                imageVector = adminIcon,
                                contentDescription = adminAction
                            )
                        }
                        val removeIcon = Icons.Default.DeleteOutline
                        val removeAction = "Remove user"
                        CustomIconButton(
                            onClick = {
                                onIconClick(
                                    removeIcon,
                                    true,
                                    "Remove User from Team",
                                    "You're about to remove ${currentMember[1]} from the team.",
                                    removeAction
                                ) {
                                    // TODO: API call to remove user from team
                                    onChangeSuccess(null)
                                }
                            },
                            modifier = Modifier.padding(start = 10.dp)
                        ) {
                            Icon(
                                imageVector = removeIcon,
                                contentDescription = removeAction
                            )
                        }
                    }
                } else {
                    val acceptIcon = Icons.Default.CheckCircleOutline
                    val acceptAction = "Accept user"
                    CustomIconButton(
                        onClick = {
                            onIconClick(
                                acceptIcon,
                                false,
                                "Accept User to Team",
                                "You're about to accept ${currentMember[1]} to the team.",
                                acceptAction
                            ) {
                                // TODO: API call to accept user to team
                                currentMember[2] = ""
                                onChangeSuccess(currentMember)
                            }
                        },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Icon(
                            imageVector = acceptIcon,
                            contentDescription = acceptAction
                        )
                    }
                    val declineIcon = Icons.Default.RemoveCircleOutline
                    val declineAction = "Decline user"
                    CustomIconButton(
                        onClick = {
                            onIconClick(
                                declineIcon,
                                true,
                                "Decline User from Team",
                                "You're about to decline ${currentMember[1]} from the team.",
                                declineAction
                            ) {
                                // TODO: API call to decline user from team
                                onChangeSuccess(null)
                            }
                        },
                    ) {
                        Icon(
                            imageVector = declineIcon,
                            contentDescription = declineAction
                        )
                    }
                }
            }
        }
    }
}
