package at.csdc25bb.mad.safmeetup.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.csdc25bb.mad.safmeetup.ui.viewmodel.TeamViewModel

@Composable
fun TeamCreationBottomSheet(teamViewModel: TeamViewModel, onSuccess: () -> Unit) {
    var message by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var typeOfSports by remember { mutableStateOf("") }

    SmallTitle(title = "Create new Team")
    BottomSheetTextField("Team Name") { newName -> name = newName }
    BottomSheetTextField("Type of Sports") { newType -> typeOfSports = newType }

    BottomSheetMessage(message = message)
    AppButton(text = "Create new Team") {
        teamViewModel.createTeam(name, typeOfSports)
        message = "Nice! Check out your team now."
    }
}

@Composable
fun TeamJoiningBottomSheet(
    userId: String = "",
    teamViewModel: TeamViewModel,
) {
    SmallTitle(title = "Join new Team")
    Column(verticalArrangement = Arrangement.Center) {
        var message by remember { mutableStateOf("") }
        var inviteCode = ""

        BottomSheetTextField("Invite Code") { newInviteCode -> inviteCode = newInviteCode }
        BottomSheetMessage(message = message)
        AppButton(
            text = "Join Team",
            onClick = {
                teamViewModel.joinTeam(userId, inviteCode)
                message = "Superb! Wait for an admin of the team to accept your join request."
            }
        )
    }
}

@Composable
fun TeamSwitchBottomSheet(
    teams: List<String> = listOf(
        "Laurins Team",
        "Sorins Super Team",
        "Renés Soccer Team"
    ),
    onChoosing: (String) -> Unit,
) {
    SmallTitle(title = "Switch Teams")
    teams.forEach { team ->
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(3.dp))
                    .clickable(onClick = {
                        // TODO: Call the data of the coosen team to display
                        if (true) onChoosing(team) // OnSuccess of API call
                    }),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = team, style = TextStyle(fontSize = 18.sp))
            }
        }
    }
}