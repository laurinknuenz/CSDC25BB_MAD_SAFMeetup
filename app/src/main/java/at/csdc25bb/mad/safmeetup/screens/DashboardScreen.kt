@file:OptIn(ExperimentalMaterial3Api::class)

package at.csdc25bb.mad.safmeetup.screens

import android.util.Log
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
import androidx.compose.runtime.collectAsState
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
import at.csdc25bb.mad.safmeetup.data.utils.ResourceState
import at.csdc25bb.mad.safmeetup.navigation.Screen
import at.csdc25bb.mad.safmeetup.ui.viewmodel.ActivityViewModel
import at.csdc25bb.mad.safmeetup.ui.viewmodel.TeamViewModel
import java.time.LocalDate

@Composable
fun DashboardScreen(
    navController: NavHostController,
    activityViewModel: ActivityViewModel = hiltViewModel(),
    teamViewModel: TeamViewModel = hiltViewModel()
) {
    val userActivities by activityViewModel.userActivities.collectAsState()
    val managedTeam by teamViewModel.managedTeam.collectAsState()

    var currentTeamName by remember { mutableStateOf("") }

    val dashboardPadding = 15.dp
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetContent by remember { mutableStateOf<@Composable () -> Unit>({}) }
    BottomSheet(showBottomSheet, { showBottomSheet = false }) { bottomSheetContent() }

    Scaffold(
        bottomBar = {
            DashboardProfileBottomBar(navController, true) {
                bottomSheetContent = {
                    when (managedTeam) {
                        is ResourceState.Loading -> {
                            Log.d("DASHBOARD-SCREEN", "Loading team...")
                            teamViewModel.getTeamByManager()
                        }

                        is ResourceState.Success -> {
                            val managedTeamResponse =
                                (managedTeam as ResourceState.Success).data

                            currentTeamName = managedTeamResponse.name
                        }

                        is ResourceState.Error -> {
                            Log.d("DASHBOARD-SCREEN", "Error loading team")
                        }

                        is ResourceState.Idle -> TODO()
                    }
                    ActivityCreationBottomSheet (
                        activityViewModel = activityViewModel,
                        currentTeam = currentTeamName
                    ){
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

            when(userActivities) {
                is ResourceState.Loading -> {
                    Log.d("DASHBOARD-SCREEN", "Still loading")
                }

                is ResourceState.Success -> {
                    val response = (userActivities as ResourceState.Success).data
                    Log.d("DASHBOARD-SCREEN", "Activities: ${response}")
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = dashboardPadding)
                    ) {
                        val filteredActivities = response.filter { activity ->
                            val matchesSearchText = keywords.isEmpty() ||
                                    activity.subject.lowercase().contains(keywords.lowercase()) ||
                                    activity.hostingTeam.name.lowercase().contains(keywords.lowercase())  ||
                                    activity.opponent.name.lowercase().contains(keywords.lowercase())

                            val matchesSubject =
                                subject.isEmpty() || activity.subject.lowercase().contains(keywords.lowercase())

                            val matchesLocation =
                                location.isEmpty() || activity.location.lowercase().contains(location.lowercase())

//                            val matchesPickedDate = pickedDate?.let {
//                                val date = Date.from(it.atStartOfDay(ZoneId.systemDefault()).toInstant())
//                                activity.date == date
//                            } ?: true

                            val matchesType =
                                type.isEmpty() || activity.type.name.lowercase().contains(type.lowercase())

                            matchesSearchText
                                    && matchesSubject
                                    && matchesLocation
//                                    && matchesPickedDate
                                    && matchesType
                        }
                        items(filteredActivities.size) {
                            val activity =
                                filteredActivities[it] // TODO: Make the API call here to get the actual activities
                            ActivityCard(
                                title = activity.subject,
                                type = activity.type.name,
                                team = activity.hostingTeam.name,
//                                date = activity.date.toString(),
                                location = activity.location
                            ) {
                                navController.navigate(Screen.Activity.withId(activity._id)) // TODO: Pass the actual ID here
                            }
                        }
                    }
                }
                is ResourceState.Error -> {
                    Log.d("DASHBOARD-SCREEN", "Error loading activity")
                }

                is ResourceState.Idle -> {
                    //DO NOTHING
                }
            }
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


        }
    }
}