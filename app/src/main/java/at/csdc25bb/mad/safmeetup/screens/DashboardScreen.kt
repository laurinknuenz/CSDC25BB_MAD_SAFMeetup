@file:OptIn(ExperimentalMaterial3Api::class)

package at.csdc25bb.mad.safmeetup.screens

import android.content.Context
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
import at.csdc25bb.mad.safmeetup.SFMApplication
import at.csdc25bb.mad.safmeetup.composables.ActivityCard
import at.csdc25bb.mad.safmeetup.composables.ActivityCreationBottomSheet
import at.csdc25bb.mad.safmeetup.composables.AdvancedFilterBottomSheet
import at.csdc25bb.mad.safmeetup.composables.BottomSheet
import at.csdc25bb.mad.safmeetup.composables.DashboardProfileBottomBar
import at.csdc25bb.mad.safmeetup.composables.HorizontalDatePicker
import at.csdc25bb.mad.safmeetup.composables.LightGrayDivider
import at.csdc25bb.mad.safmeetup.composables.SearchBar
import at.csdc25bb.mad.safmeetup.data.entity.activity.Activity
import at.csdc25bb.mad.safmeetup.navigation.Screen
import at.csdc25bb.mad.safmeetup.ui.viewmodel.ActivityViewModel
import at.csdc25bb.mad.safmeetup.ui.viewmodel.TeamViewModel
import java.time.LocalDate

@Composable
fun DashboardScreen(
    navController: NavHostController,
    activityViewModel: ActivityViewModel = hiltViewModel(),
    teamViewModel: TeamViewModel = hiltViewModel(),
) {
    val sharedPref =
        SFMApplication.instance.getSharedPreferences("SFMApplication", Context.MODE_PRIVATE)
    var userId = sharedPref.getString("userId", "")
    var userActivitiesFetched by remember { mutableStateOf(listOf<Activity>()) }

    val managedTeam by teamViewModel.managedTeam.collectAsState()

    var currentTeamName by remember { mutableStateOf("") }

    val dashboardPadding = 15.dp
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetContent by remember { mutableStateOf<@Composable () -> Unit>({}) }

    userActivitiesFetched = activityViewModel.getAllActivitiesForUserFetched()

    BottomSheet(showBottomSheet, { showBottomSheet = false }) { bottomSheetContent() }

    Scaffold(
        bottomBar = {
            DashboardProfileBottomBar(navController, true) {
                bottomSheetContent = {

                    teamViewModel.getTeamByManager()

                    currentTeamName = managedTeam.name

                    ActivityCreationBottomSheet(
                        activityViewModel = activityViewModel,
                        currentTeamName = currentTeamName
                    ) {
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

            LightGrayDivider(
                Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp)
                    .padding(bottom = 5.dp)
                    .padding(horizontal = 4.dp)
            )
            val response = userActivitiesFetched
            Log.d("DASHBOARD-SCREEN", "Activities: ${response}")
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dashboardPadding)
            ) {
                val filteredActivities = response.filter { activity ->
                    val matchesSearchText = keywords.isEmpty() ||
                            activity.subject.lowercase().contains(keywords.lowercase()) ||
                            activity.hostingTeam.name.lowercase().contains(keywords.lowercase()) ||
                            activity.opponent.name.lowercase().contains(keywords.lowercase())

                    val matchesSubject =
                        subject.isEmpty() || activity.subject.lowercase()
                            .contains(keywords.lowercase())

                    val matchesLocation =
                        location.isEmpty() || activity.location.lowercase()
                            .contains(location.lowercase())

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
                    var participates by remember { mutableStateOf(false) }
                    activity.listOfGuests?.let { guests ->
                        for (guest in guests) {
                            if (guest._id._id == userId) {
                                participates = guest.attendance == true
                                break
                            }
                        }
                    }
                    ActivityCard(
                        activityViewModel = activityViewModel,
                        activity = activity,
                        participates = participates
                    ) {
                        navController.navigate(Screen.Activity.withId(activity._id))
                    }
                }
            }
        }
    }
}