@file:OptIn(ExperimentalMaterial3Api::class)

package at.csdc25bb.mad.safmeetup.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import at.csdc25bb.mad.safmeetup.SFMApplication
import at.csdc25bb.mad.safmeetup.composables.ActivityTopBar
import at.csdc25bb.mad.safmeetup.composables.AppButton
import at.csdc25bb.mad.safmeetup.composables.BottomSheetTextField
import at.csdc25bb.mad.safmeetup.composables.ExtendedDatePicker
import at.csdc25bb.mad.safmeetup.composables.GermanDateTimeFormatter
import at.csdc25bb.mad.safmeetup.composables.InformationDialog
import at.csdc25bb.mad.safmeetup.composables.LightGrayDivider
import at.csdc25bb.mad.safmeetup.composables.ParticipationButton
import at.csdc25bb.mad.safmeetup.composables.SmallTitle
import at.csdc25bb.mad.safmeetup.composables.activityTypeSelector
import at.csdc25bb.mad.safmeetup.navigation.Screen
import at.csdc25bb.mad.safmeetup.ui.viewmodel.ActivityViewModel
import java.time.LocalDate

@Composable
fun ActivityScreen(
    activityViewModel: ActivityViewModel = hiltViewModel(),
    navController: NavHostController,
    activityId: String,
) {
    val sharedPref = SFMApplication.instance.getSharedPreferences("SFMApplication", Context.MODE_PRIVATE)
    var userId = sharedPref.getString("userId", "")

    var activityTitle by remember { mutableStateOf("") }
    var activityType by remember { mutableStateOf("") }
    var activityOpponent by remember { mutableStateOf("") }
    var activityDate = LocalDate.now()
    var activityLocation by remember { mutableStateOf("") }
    var userParticipates  by remember { mutableStateOf(true) }
    var activityParticipating = listOf("Laurin Knünz", "Sorin Lazar", "Lilli Jahn")
    var activityNotParticipating =
        listOf("Mathias Kerndl", "Mathias Leitgeb", "Judy Kardouh", "Fabian Maier")
    var activityNoReply = listOf("René Goldschmid", "Leon Freudenthaler", "Burak Kongo")


    activityViewModel.getActivityById(activityId)
    val currentActivity by activityViewModel.currentActivity.collectAsState()

    Log.d("ActivityScreen", currentActivity.toString())

    activityTitle = currentActivity.subject
    activityType = currentActivity.type.name
    activityOpponent = currentActivity.opponent.name
    activityLocation = currentActivity.location

    currentActivity.listOfGuests?.let { guests ->
        for (guest in guests) {
            if (guest._id._id == userId) {
                userParticipates = guest.attendance == true
                break
            }
        }
    }

    var editMode by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(activityTitle) }
    var type by remember { mutableStateOf(activityType) }
    var opponent by remember { mutableStateOf(activityOpponent) }
    var date by remember { mutableStateOf(activityDate) }
    var location by remember { mutableStateOf(activityLocation) }

    var openInformationDialog by remember { mutableStateOf(false) }
    if (openInformationDialog) {
        InformationDialog(
            icon = Icons.Default.DeleteForever,
            warning = true,
            title = "Activity Deletion",
            dialogText = "You're about to delete this activity.",
            confirmButtonText = "Delete Activity",
            onConfirmation = {
                // TODO: Make api call here to delete activity
                openInformationDialog = false
                navController.navigate(Screen.Dashboard.route)
            },
            closeDialog = { openInformationDialog = false }
        )
    }

    Scaffold(
        topBar = {
            ActivityTopBar(
                navigateUp = { navController.navigateUp() },
                editMode,
                { editMode = true }
            ) {
                // TODO: Make API call here to update the activity
                if (true) editMode =
                    false // Execute only on success of activity updating, so when all parameters are there and it worked
            }
            LightGrayDivider(Modifier.padding(horizontal = 3.dp))
        }
    ) { innerPadding ->
        var participation by remember { mutableStateOf(userParticipates) }
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ActivityTitleLine(title, true, editMode) { newTitle -> title = newTitle }
                ActivityTitleLine(type, false, editMode) { newType -> type = newType }
            }
            LightGrayDivider()
            LazyColumn(modifier = Modifier.padding(bottom = 20.dp)) {
                item {
                    activityDetailLine(
                        name = "Date",
                        date = date,
                        editMode = editMode
                    ) { newDate -> date = newDate }
                    location =
                        activityDetailLine(name = "Location", value = location, editMode = editMode)
                    opponent = if (type == "Game") activityDetailLine(
                        name = "Opponent",
                        value = opponent,
                        editMode = editMode
                    ) else ""
                    LightGrayDivider(modifier = Modifier.padding(bottom = 10.dp))
                    Row(
                        modifier = Modifier.border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(10.dp)
                        )
                    ) {
                        Row(modifier = Modifier.padding(5.dp)) {
                            ParticipationButton(true, participation) {
                                participation = true
                            } // TODO: Add api call here to change participation
                            ParticipationButton(false, participation) {
                                participation = false
                            } // Here too
                        }
                    }
                    LightGrayDivider(modifier = Modifier.padding(vertical = 10.dp))
                    ActivityParticipationList(
                        title = "Participating", activityParticipating
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Participating icon",
                            tint = Color(0xFF2DC533)
                        )
                    }
                    ActivityParticipationList(
                        title = "Not Participating", activityNotParticipating
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbDown,
                            contentDescription = "Participating icon",
                            tint = Color(0xFFF13D2F)
                        )
                    }
                    ActivityParticipationList(
                        title = "No Answer", activityNoReply
                    ) {
                        Icon(
                            imageVector = Icons.Default.QuestionMark,
                            contentDescription = "No answer icon",
                            tint = Color.LightGray
                        )
                    }
                    AppButton(text = "Delete Activity", onClick = { openInformationDialog = true })
                }
            }
        }
    }
}

@Composable
fun ActivityTitleLine(
    value: String,
    mainTitle: Boolean,
    editMode: Boolean,
    onChange: (String) -> Unit = {},
) {

    if (editMode && mainTitle) BottomSheetTextField(
        label = "Title",
        onChange = onChange,
        initValue = value
    )
    else if (editMode) activityTypeSelector(initialValue = value, onChange = onChange)
    else Text(
        text = value,
        style = TextStyle(fontSize = if (mainTitle) 35.sp else 20.sp),
        fontWeight = if (mainTitle) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier.padding(bottom = if (!mainTitle) 20.dp else 5.dp)
    )
}

@Composable
fun activityDetailLine(
    name: String,
    value: String = "",
    date: LocalDate? = null,
    editMode: Boolean,
    onDateChange: (LocalDate?) -> Unit = {},
): String {
    var currentValue by remember { mutableStateOf(value) }
    var showExtendedDatePicker by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$name: ", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
        if (editMode && date == null) BottomSheetTextField(
            label = "",
            onChange = { newValue -> currentValue = newValue },
            initValue = value
        )
        else if (editMode) {
            AppButton(
                text = if (date != null) date.format(GermanDateTimeFormatter) else "",
                onClick = { showExtendedDatePicker = true })
            if (showExtendedDatePicker) ExtendedDatePicker(onClose = {
                showExtendedDatePicker = false
            }, onChangeDate = onDateChange)
        } else Text(
            text = if (date != null) date.format(GermanDateTimeFormatter) else value,
            style = TextStyle(fontSize = 18.sp)
        )
    }
    return currentValue
}

@Composable
fun ActivityParticipationList(
    title: String,
    userList: List<String>,
    icon: @Composable () -> Unit,
) {
    Row {
        icon()
        SmallTitle(title = title, modifier = Modifier.padding(start = 7.dp))
    }
    Column(
        modifier = Modifier
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .height((userList.size * 40).dp)
                .fillMaxWidth()
                .padding(horizontal = 3.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            userList.forEach { user ->
                Text(text = user, modifier = Modifier.padding(horizontal = 7.dp))
                if (userList.last() != user) LightGrayDivider()
            }
        }
    }
    LightGrayDivider(modifier = Modifier.padding(vertical = 10.dp))
}