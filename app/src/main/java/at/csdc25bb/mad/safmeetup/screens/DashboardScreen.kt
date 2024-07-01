@file:OptIn(ExperimentalMaterial3Api::class)

package at.csdc25bb.mad.safmeetup.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import at.csdc25bb.mad.safmeetup.composables.ActivityCard
import at.csdc25bb.mad.safmeetup.composables.ActivityCreationBottomSheet
import at.csdc25bb.mad.safmeetup.composables.AdvancedFilterBottomSheet
import at.csdc25bb.mad.safmeetup.composables.BottomSheet
import at.csdc25bb.mad.safmeetup.composables.DashboardProfileBottomBar
import at.csdc25bb.mad.safmeetup.composables.GermanDateTimeFormatter
import at.csdc25bb.mad.safmeetup.composables.HorizontalDatePicker
import at.csdc25bb.mad.safmeetup.composables.LightGrayDivider
import at.csdc25bb.mad.safmeetup.composables.SearchBar
import at.csdc25bb.mad.safmeetup.navigation.Screen
import at.csdc25bb.mad.safmeetup.ui.viewmodel.TeamViewModel
import java.time.LocalDate

@Composable
fun DashboardScreen(
    navController: NavHostController,
    teamViewModel: TeamViewModel = hiltViewModel(),
) {
    val dashboardPadding = 15.dp
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetContent by remember { mutableStateOf<@Composable () -> Unit>({}) }
    BottomSheet(showBottomSheet, { showBottomSheet = false }) { bottomSheetContent() }

    Scaffold(
        bottomBar = {
            DashboardProfileBottomBar(navController, true) {
                bottomSheetContent = {
                    ActivityCreationBottomSheet {
                        showBottomSheet = false
                        navController.navigate(Screen.Dashboard.route)
                    }
                }
                showBottomSheet = true
            }
        },
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
            var keywords by remember { mutableStateOf("") }
            var subject by remember { mutableStateOf("") }
            var location by remember { mutableStateOf("") }
            var type by remember { mutableStateOf("") }
            var pickedDate: LocalDate? by remember { mutableStateOf(null) }

            HorizontalDatePicker(pickedDate) { newDate -> pickedDate = newDate }
            SearchBar(
                modifier = Modifier.padding(horizontal = dashboardPadding),
                onChange = { newKeywords ->
                    keywords = newKeywords
                }, onClickResetFilter = {
                    keywords = ""
                    subject = ""
                    location = ""
                    pickedDate = null
                    type = ""
                }) {
                bottomSheetContent =
                    {
                        AdvancedFilterBottomSheet(subject, location, pickedDate, type,
                            onApplyFilter = { newSubject, newLocation, newDate, newType ->
                                subject = newSubject
                                location = newLocation
                                pickedDate = newDate
                                type = newType
                                showBottomSheet = false
                            })
                    }
                showBottomSheet = true
            }
//            teamViewModel.getTeam("laurins Team")
//            val team by teamViewModel.team.collectAsState()
//            when(team) {
//                is ResourceState.Loading -> {
//                    Log.d("DASHBOARD-SCREEN", "Still loading")
//                }
//
//                is ResourceState.Success -> {
//                    val response = (team as ResourceState.Success).data
//                    Log.d("DASHBOARD-SCREEN", "TEAM: ${response}")
//                    Text(text = response.name)
//                }
//                is ResourceState.Error -> {
//                    Log.d("DASHBOARD-SCREEN", "Error loading team")
//                }
//
//                is ResourceState.Idle -> {
//                    //DO NOTHING
//                }
//            }

            LightGrayDivider(
                Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp)
                    .padding(bottom = 5.dp)
                    .padding(horizontal = 4.dp)
            )

            // BEGINNING of mocking data for testing
            val listOfActivities = mutableListOf<List<String>>()
            listOfActivities.add(
                listOf(
                    "Weekly Training",
                    "Training",
                    LocalDate.now().format(GermanDateTimeFormatter),
                    "FH Campus Gym"
                )
            )
            listOfActivities.add(
                listOf(
                    "Game against Eagles",
                    "Game",
                    LocalDate.now().plusDays(3).format(GermanDateTimeFormatter),
                    "FH Technikum Gym"
                )
            )
            listOfActivities.add(
                listOf(
                    "Hike",
                    "Other Activity",
                    LocalDate.now().plusDays(5).format(GermanDateTimeFormatter),
                    "Kahlenberg"
                )
            )
            listOfActivities.add(
                listOf(
                    "Going to a restaurant",
                    "Other Activity",
                    LocalDate.now().plusDays(5).format(GermanDateTimeFormatter),
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
                    val matchesSearchText = keywords.isEmpty() ||
                            activity[0].lowercase().contains(keywords.lowercase()) ||
                            activity[1].lowercase().contains(keywords.lowercase()) ||
                            activity[3].lowercase().contains(keywords.lowercase())

                    val matchesSubject =
                        subject.isEmpty() || activity[0].lowercase().contains(subject.lowercase())

                    val matchesLocation =
                        location.isEmpty() || activity[3].lowercase().contains(location.lowercase())

                    val matchesPickedDate = pickedDate?.let {
                        activity[2] == it.format(GermanDateTimeFormatter)
                    } ?: true

                    val matchesType =
                        type.isEmpty() || activity[1].lowercase().contains(type.lowercase())

                    matchesSearchText && matchesSubject && matchesLocation && matchesPickedDate && matchesType
                }
                items(filteredActivities.size) {
                    val activity =
                        filteredActivities[it] // TODO: Make the API call here to get the actual activities
                    ActivityCard(
                        title = activity[0],
                        type = activity[1],
                        date = activity[2],
                        location = activity[3]
                    ) {
                        navController.navigate(Screen.Activity.withId("ActivityId")) // TODO: Pass the actual ID here
                    }
                }
            }
        }
    }
}