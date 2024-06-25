package at.csdc25bb.mad.safmeetup.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun datePicker(): LocalDate? { // TODO MAYBE: Lazy generation and garbage collection to make infinite scroll possible
    var selectedDate: LocalDate? by remember { mutableStateOf(null) }

    LazyRow(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        items(30) {
            DatePickerItem(
                itemDate = LocalDate.now().plusDays(it.toLong()),
                selectedDate = selectedDate
            ) { newSelectedDate ->
                selectedDate = if (selectedDate != newSelectedDate) newSelectedDate else null
            }
        }
    }

    return selectedDate
}

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("E", Locale.ENGLISH)

@Composable
fun DatePickerItem(
    itemDate: LocalDate,
    selectedDate: LocalDate?,
    onClick: (LocalDate) -> Unit
) {
    val isDateSelected = itemDate == selectedDate
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (itemDate == LocalDate.now() || itemDate.dayOfMonth == 1) {
            Text(
                text = itemDate
                    .format(DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH))
                    .uppercase(),
                modifier = Modifier.rotate(-90f),
                style = TextStyle(fontSize = 15.sp),
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 3.dp)
        ) {
            Text(
                text = itemDate.format(formatter).uppercase(),
                style = TextStyle(color = Color.Gray, fontSize = 12.sp),
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Box(
                modifier = Modifier
                    .background(
                        color = if (isDateSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    )
                    .clickable(
                        onClick = { onClick(itemDate) },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .width(50.dp)
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = itemDate.dayOfMonth.toString(),
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = if (isDateSelected) Color.White else Color.LightGray
                    )
                )
            }
        }
    }
}