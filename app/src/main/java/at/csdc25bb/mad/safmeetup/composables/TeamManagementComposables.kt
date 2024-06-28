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

@Composable
fun TeamCreationBottomSheet(onSuccess: () -> Unit) {
    var message by remember { mutableStateOf("") }

    val name = bottomSheetTextField("Team Name")
    val typeOfSports = bottomSheetTextField("Type of Sports")

    BottomSheetMessage(message = message)
    AppButton(text = "Create new Team") {
        // TODO: Make api call here to request team joining
        if (false) onSuccess() // If API call succeeded
        else if (true) message = "Error message." // If API call failed
    }
}

@Composable
fun TeamJoiningBottomSheet() {
    Column(verticalArrangement = Arrangement.Center) {
        var message by remember { mutableStateOf("") }

        val inviteCode = bottomSheetTextField("Invite Code")
        BottomSheetMessage(message = message)
        AppButton(text = "Join Team") {
            // TODO: Make api call here to request team joining
            message = "Superb! Wait for an admin of the team to accept your join request."
        }
    }
}

@Composable
fun TeamSwitchBottomSheet(
    teams: List<String> = listOf(
        "Laurins Team",
        "Sorins Super Team",
        "Renés Soccer Team"
    ),
    onChoosing: (String) -> Unit
) {
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