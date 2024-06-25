@file:OptIn(ExperimentalMaterial3Api::class)

package at.csdc25bb.mad.safmeetup.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import at.csdc25bb.mad.safmeetup.composables.ActivityCard
import at.csdc25bb.mad.safmeetup.composables.DashboardProfileBottomBar
import at.csdc25bb.mad.safmeetup.composables.datePicker
import at.csdc25bb.mad.safmeetup.composables.searchBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DashboardScreen(navController: NavHostController) {
    val dashboardPadding = 15.dp
    Scaffold(
        bottomBar = { DashboardProfileBottomBar(navController, true) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = dashboardPadding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = dashboardPadding)
            ) {
                Text(text = "Your upcoming ", style = TextStyle(fontSize = 22.sp))
                Text(
                    text = "Activities",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp)
                )
            }
            val pickedDate: LocalDate? = datePicker()
            val searchText: String =
                searchBar(modifier = Modifier.padding(horizontal = dashboardPadding))
            Divider(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp)
                    .padding(bottom = 5.dp)
                    .padding(horizontal = 4.dp),
                color = MaterialTheme.colorScheme.outline
            )

            // BEGINNING of mocking data for testing
            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN)
            val listOfActivities = mutableListOf<List<String>>()
            listOfActivities.add(
                listOf(
                    "Weekly Training",
                    "Training",
                    LocalDate.now().format(dateTimeFormatter),
                    "FH Campus Gym"
                )
            )
            listOfActivities.add(
                listOf(
                    "Game against Eagles",
                    "Game",
                    LocalDate.now().plusDays(3).format(dateTimeFormatter),
                    "FH Technikum Gym"
                )
            )
            listOfActivities.add(
                listOf(
                    "Hike",
                    "Other Activity",
                    LocalDate.now().plusDays(5).format(dateTimeFormatter),
                    "Kahlenberg"
                )
            )
            listOfActivities.add(
                listOf(
                    "Going to a restaurant",
                    "Other Activity",
                    LocalDate.now().plusDays(5).format(dateTimeFormatter),
                    "Das Zehn"
                )
            )
            // END of mocking data for testing

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dashboardPadding)
            ) {
                val filteredActivities = listOfActivities.filter { activity ->
                    val matchesSearchText = searchText.isEmpty() ||
                            activity[0].lowercase().contains(searchText.lowercase()) ||
                            activity[1].lowercase().contains(searchText.lowercase()) ||
                            activity[3].lowercase().contains(searchText.lowercase())

                    val matchesPickedDate = pickedDate?.let {
                        activity[2] == it.format(dateTimeFormatter)
                    } ?: true

                    matchesSearchText && matchesPickedDate
                }
                items(filteredActivities.size) {
                    val activity =
                        filteredActivities[it] // TODO: Make the API call here to get the actual activities
                    ActivityCard(
                        title = activity[0],
                        type = activity[1],
                        date = activity[2],
                        location = activity[3]
                    )
                }
            }
        }
    }
}