@file:OptIn(InternalCoroutinesApi::class)

package at.csdc25bb.mad.safmeetup.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Divider
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
import kotlinx.coroutines.InternalCoroutinesApi

@Composable
fun ActivityCard(
    title: String = "Your Activity",
    type: String = "Training",
    date: String = "Friday, 28. June 2024", // TODO: Change this to date or make it somehow to convert format to german format
    location: String = "FH Campus Wien Gym",
    participates: Boolean = true
) {
    var participation by remember { mutableStateOf(participates) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Row(modifier = Modifier.padding(vertical = 5.dp)) {
                Text(text = title, style = TextStyle(fontSize = 19.sp))
                Text(text = " - $type", style = TextStyle(fontSize = 19.sp))
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                color = MaterialTheme.colorScheme.outline
            )
            ActivityDetailLine(detail = "Date", date)
            ActivityDetailLine(detail = "Location", location)

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                color = MaterialTheme.colorScheme.outline
            )
            Row(
                modifier = Modifier
            ) {
                ParticipationButton(true, participation) { participation = true }
                ParticipationButton(false, participation) { participation = false }
            }
        }
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
    Box(
        modifier = Modifier
            .fillMaxWidth(fraction = if (firstButton) 0.5f else 1f)
            .padding(5.dp)
            .height(60.dp)
            .background(
                if (firstButton && participation) Color(0xFF2DC533)
                else if (!firstButton && !participation) Color(0xFFF13D2F)
                else MaterialTheme.colorScheme.outline,
                RoundedCornerShape(15.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (firstButton) Icons.Outlined.ThumbUp else Icons.Outlined.ThumbDown,
            contentDescription = (if (firstButton) "" else "not") + "participating in activity",
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}