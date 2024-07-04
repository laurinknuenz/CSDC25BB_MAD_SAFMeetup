package at.csdc25bb.mad.safmeetup.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import at.csdc25bb.mad.safmeetup.data.entity.team.TeamUser
import at.csdc25bb.mad.safmeetup.ui.viewmodel.TeamViewModel

@Composable
fun profileDetailLine(
    name: String,
    value: String,
    password: Boolean = false,
    editable: Boolean = true,
    editMode: Boolean = false
): String {
    var currentEditMode by remember { mutableStateOf(editMode) }
    var currentValue by remember { mutableStateOf(value) }

    LaunchedEffect(editMode) {
        currentEditMode = editMode
    }

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
        if (!currentEditMode)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if(!password) currentValue else "●●●●●●●●",
                    style = TextStyle(
                        fontSize = if (!password) 18.sp else 12.sp,
                        color = if (!password) Color.Black else passwordTextColor
                    )
                )
            }
        else if (password) ProfileTextField(password = true) { newPassword ->
            currentValue = newPassword
        }
        else if (editable) ProfileTextField(
            initialValue = currentValue,
            onChange = { newValue -> currentValue = newValue })
    }
    return currentValue
}

@Composable
fun ProfileTextField(
    initialValue: String = "",
    password: Boolean = false,
    onChange: (String) -> Unit = {}
) {
    var value by remember { mutableStateOf(initialValue) }

    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it
            onChange(it)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if(password) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun TeamMemberEntry(
    member: TeamUser,
    teamName: String,
    userIsAdmin: Boolean = false,
    userIsPending: Boolean = false,
    teamViewModel: TeamViewModel,
    onIconClick: (ImageVector, Boolean, String, String, String, () -> Unit) -> Unit,
    onChangeSuccess: (List<String>?) -> Unit = {}
) {
    Row(
        modifier = Modifier

            .border(0.1.dp, MaterialTheme.colorScheme.outline)
            .padding(15.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Icon(
                imageVector = if (userIsAdmin) Icons.Default.VerifiedUser else Icons.Default.Person,
                contentDescription = "Icon of user",
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(text = member.firstname)
        }
        Row {
            if (!userIsAdmin) {
                if (!userIsPending) {
                    val removeIcon = Icons.Default.DeleteOutline
                    val removeAction = "Remove user"
                    CustomIconButton(
                        onClick = {
                            onIconClick(
                                removeIcon,
                                true,
                                "Remove User from Team",
                                "You're about to remove ${member.firstname} from the team.",
                                removeAction
                            ) {
                                teamViewModel.removeUserFromTeam(member._id, teamName)
                            }
                        },
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Icon(
                            imageVector = removeIcon,
                            contentDescription = removeAction
                        )
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
                                "You're about to accept ${member.firstname} to the team.",
                                acceptAction
                            ) {
                                teamViewModel.addUserToTeam(member._id, teamName)
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
                                "You're about to decline ${member.firstname} from the team.",
                                declineAction
                            ) {
                                teamViewModel.removeUserFromTeam(member._id, teamName)
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
