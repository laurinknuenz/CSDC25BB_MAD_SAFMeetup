@file:OptIn(InternalCoroutinesApi::class, ExperimentalMaterial3Api::class)

package at.csdc25bb.mad.safmeetup.composables

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import at.csdc25bb.mad.safmeetup.SFMApplication
import at.csdc25bb.mad.safmeetup.data.entity.activity.Activity
import at.csdc25bb.mad.safmeetup.ui.viewmodel.ActivityViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ActivityCard(
    activityViewModel: ActivityViewModel,
    participates: Boolean,
    activity: Activity = Activity(),
    onClick: () -> Unit
) {
    val sharedPref = SFMApplication.instance.getSharedPreferences("SFMApplication", Context.MODE_PRIVATE)
    var userId = sharedPref.getString("userId", "")

    val id by remember { mutableStateOf(activity._id) }
    val title by remember { mutableStateOf(activity.subject) }
    val type by remember { mutableStateOf(activity.type.name) }
    val team by remember { mutableStateOf(activity.hostingTeam.name) }
    val opponent by remember { mutableStateOf(activity.opponent.name) }
    val location by remember { mutableStateOf(activity.location) }
    val date by remember { mutableStateOf("Friday, 5. July 2024") }

    var participation by remember { mutableStateOf(participates) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            .clickable(onClick = { onClick() })
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            ActivityTitleText(title, type)
            LightGrayDivider(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            )
            ActivityDetailLine(detail = "Hosting Team", team)
            if (team != opponent) {
                ActivityDetailLine(detail = "Opponent", opponent)
            }
            ActivityDetailLine(detail = "Date", date)
            ActivityDetailLine(detail = "Location", location)

            LightGrayDivider(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            )
            Row(
            ) {
                ParticipationButton(true, participation) {
                    Log.d("ActivityCard", "Changing the $participation to true")
                    if (userId != null) {
                        activityViewModel.updateAttendanceForUser(id, userId, true)
                        participation = !participation
                    }
                } // TODO: Add api call here to change participation
                ParticipationButton(false, participation) {
                    Log.d("ActivityCard", "Changing the $participation to false")
                    if (userId != null) {
                        activityViewModel.updateAttendanceForUser(id, userId, false)
                        participation = !participation
                    }
                } // Here too
            }
        }
    }
}

@Composable
fun ActivityTitleText(title: String, type: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(fontSize = 18.sp),
            modifier = Modifier.fillMaxWidth(0.7f)
        )
        Text(text = type, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold))
    }
}

@Composable
fun ActivityDetailLine(detail: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$detail: ",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )
        Text(text = value)
    }
}

@Composable
fun ParticipationButton(firstButton: Boolean, participation: Boolean, onClick: () -> Unit) {
    CustomIconButton(
        modifier = Modifier
            .fillMaxWidth(fraction = if (firstButton) 0.5f else 1f)
            .padding(5.dp)
            .height(60.dp)
            .background(
                if (firstButton && participation) Color(0xFF2DC533)
                else if (!firstButton && !participation) Color(0xFFF13D2F)
                else MaterialTheme.colorScheme.outline,
                RoundedCornerShape(15.dp)
            ),
        onClick = onClick
    ) {
        Icon(
            imageVector = if (firstButton) Icons.Outlined.ThumbUp else Icons.Outlined.ThumbDown,
            contentDescription = (if (firstButton) "" else "not") + "participating in activity",
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun ActivityCreationBottomSheet(
    activityViewModel: ActivityViewModel,
    currentTeamName: String,
    onCreation: () -> Unit,
) {
    var message by remember { mutableStateOf("") }

    var subject by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var hostingTeam by remember { mutableStateOf("") }
    var opponent by remember { mutableStateOf("") }
    var selectedDate: LocalDate? by remember { mutableStateOf(null) }
    var showExtendedDatePicker by remember { mutableStateOf(false) }

    if (showExtendedDatePicker) {
        ExtendedDatePicker(
            onClose = { showExtendedDatePicker = false },
            onChangeDate = { newDate -> selectedDate = newDate })
    }

    SmallTitle(title = "Create new Activity")
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier.fillMaxWidth(0.85f)) {
            HorizontalDatePicker(selectedDate) { newSelectedDate ->
                selectedDate = newSelectedDate
            }
        }
        CustomIconButton(
            onClick = { showExtendedDatePicker = true },
            modifier = Modifier.fillMaxWidth(),
            indication = true
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Open extended date picker",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            )
        }
    }
    BottomSheetMessage(
        message = if (selectedDate != null) {
            "Selected Date: " + (selectedDate!!.format(GermanDateTimeFormatter))
        } else ""
    )

    BottomSheetTextField(label = "Subject") { newSubject -> subject = newSubject }
    BottomSheetTextField(label = "Location") { newLocation -> location = newLocation }
    BottomSheetTextField(label = "Hosting Team") { newHostingTeam -> hostingTeam = newHostingTeam }
    val selectedType = activityTypeSelector("Other Activity")
    if (selectedType == "Game")
        BottomSheetTextField(label = "Opponent") { newOpponent -> opponent = newOpponent }
    BottomSheetMessage(message = message)
    AppButton(text = "Create Activity") {
        activityViewModel.createActivity(
            subject,
            selectedType,
            hostingTeam,
            opponent,
            location,
//            selectedDate.toString()
        )
        if (true) onCreation() // on Success of API call, on error display message by changing message value
    }
}

@Composable
fun activityTypeSelector(
    initialValue: String,
    types: List<String> = listOf(
        "Training",
        "Game",
        "Other Activity"
    ),
    onChange: (String) -> Unit = {}
): String {
    var selected by remember { mutableStateOf(initialValue) }
    Row {
        var iterator = 0
        types.forEach { type ->
            iterator++
            AppButton(
                text = type,
                modifier = Modifier
                    .height(70.dp)
                    .padding(top = 10.dp)
                    .fillMaxWidth(if (iterator == 1) (0.33f) else if (iterator == 2) 0.5f else 1f),
                color = (if (type == selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
            ) {
                selected = if (selected != type) type else ""
                onChange(selected)
            }
        }
    }
    return selected
}

@Composable
fun AdvancedFilterBottomSheet(
    initSubject: String, initLocation: String, initSelectedDate: LocalDate?, initType: String,
    onApplyFilter: (String, String, LocalDate?, String) -> Unit
) {
    var subject by remember { mutableStateOf(initSubject) }
    var location by remember { mutableStateOf(initLocation) }
    var selectedDate: LocalDate? by remember { mutableStateOf(initSelectedDate) }
    var type by remember { mutableStateOf(initType) }
    var showExtendedDatePicker by remember { mutableStateOf(false) }

    SmallTitle(title = "Advanced Filter")
    AdvancedFilterOption("Subject") {
        BottomSheetTextField(label = "", subject) { newSubject -> subject = newSubject }
    }
    AdvancedFilterOption("Location") {
        BottomSheetTextField(label = "", location) { newLocation -> location = newLocation }
    }
    val pickedDate = if (selectedDate != null) selectedDate!!.format(
        GermanDateTimeFormatter
    ) else "Activity Date"
    AdvancedFilterOption(pickedDate) {
        Row {
            AppButton(
                text = "Clear",
                onClick = { selectedDate = null },
                modifier = Modifier
                    .width(100.dp)
                    .padding(end = 10.dp)
            )
            AppButton(
                text = "Open...",
                onClick = { showExtendedDatePicker = true },
                modifier = Modifier.width(100.dp)
            )
        }
        if (showExtendedDatePicker) ExtendedDatePicker(
            onClose = { showExtendedDatePicker = false },
            onChangeDate = { newDate ->
                selectedDate = newDate
            })
    }
    AdvancedFilterOption(title = "Activity Type Selector", true) {
        type = activityTypeSelector(type)
    }
    LightGrayDivider()
    AppButton(
        text = "Apply Filter",
        onClick = { onApplyFilter(subject, location, selectedDate, type) })
}

@Composable
fun AdvancedFilterOption(title: String, column: Boolean = false, content: @Composable () -> Unit) {
    Column {
        LightGrayDivider()
        if (!column)
            Row(
                modifier = Modifier
                    .padding(vertical = 5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FilterOptionText(text = title)
                content()
            }
        else Column(modifier = Modifier.padding(vertical = 5.dp)) {
            FilterOptionText(text = title)
            content()
        }
    }
}

@Composable
fun FilterOptionText(text: String) {
    Text(text = text, style = TextStyle(fontSize = 18.sp), modifier = Modifier.padding(end = 10.dp))
}

val GermanDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
    "dd.MM.yyyy",
    Locale.GERMAN
)