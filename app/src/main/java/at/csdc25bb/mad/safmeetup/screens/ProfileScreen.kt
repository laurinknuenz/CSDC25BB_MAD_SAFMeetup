package at.csdc25bb.mad.safmeetup.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.GroupOff
import androidx.compose.material.icons.filled.Logout
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import at.csdc25bb.mad.safmeetup.R
import at.csdc25bb.mad.safmeetup.composables.ActivityCreationBottomSheet
import at.csdc25bb.mad.safmeetup.composables.AppButton
import at.csdc25bb.mad.safmeetup.composables.BottomSheet
import at.csdc25bb.mad.safmeetup.composables.DashboardProfileBottomBar
import at.csdc25bb.mad.safmeetup.composables.InfoDialogParams
import at.csdc25bb.mad.safmeetup.composables.InformationDialog
import at.csdc25bb.mad.safmeetup.composables.Loader
import at.csdc25bb.mad.safmeetup.composables.ProfilePageTopBar
import at.csdc25bb.mad.safmeetup.composables.SmallTitle
import at.csdc25bb.mad.safmeetup.composables.TeamCreationBottomSheet
import at.csdc25bb.mad.safmeetup.composables.TeamJoiningBottomSheet
import at.csdc25bb.mad.safmeetup.composables.TeamMemberEntry
import at.csdc25bb.mad.safmeetup.composables.TeamSwitchBottomSheet
import at.csdc25bb.mad.safmeetup.composables.Title
import at.csdc25bb.mad.safmeetup.composables.profileDetailLine
import at.csdc25bb.mad.safmeetup.data.entity.team.TeamUser
import at.csdc25bb.mad.safmeetup.data.utils.ResourceState
import at.csdc25bb.mad.safmeetup.navigation.Screen
import at.csdc25bb.mad.safmeetup.ui.viewmodel.AuthViewModel
import at.csdc25bb.mad.safmeetup.ui.viewmodel.TeamViewModel
import at.csdc25bb.mad.safmeetup.ui.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    manager: Boolean = true,
    userViewModel: UserViewModel = hiltViewModel(),
    teamViewModel: TeamViewModel = hiltViewModel(),
) {
    val currentUser by userViewModel.user.collectAsState()
    val managedTeam by teamViewModel.managedTeam.collectAsState()

    var errorMessage by remember { mutableStateOf("") }
    val logoutState by authViewModel.logoutState.collectAsState()

    var userProfileSelected by remember { mutableStateOf(true) }
    var chosenTeam by remember { mutableStateOf("Laurins Team") }
    var userIsPartOfTeam by remember { mutableStateOf(false) }

    var openInformationDialog by remember { mutableStateOf(false) }
    var infoDialogParams by remember {
        mutableStateOf(InfoDialogParams())
    }
    if (openInformationDialog) {
        InformationDialog(
            icon = infoDialogParams.icon,
            warning = infoDialogParams.warning,
            title = infoDialogParams.title,
            dialogText = infoDialogParams.dialogText,
            confirmButtonText = infoDialogParams.confirmButtonText,
            onConfirmation = infoDialogParams.onConfirmation,
            closeDialog = { openInformationDialog = false }
        )
    }
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetContent by remember { mutableStateOf<@Composable () -> Unit>({}) }
    BottomSheet(showBottomSheet, { showBottomSheet = false }) { bottomSheetContent() }

    val profilePadding = 15.dp
    Scaffold(
        topBar = {
            ProfilePageTopBar(userProfileSelected, { value -> userProfileSelected = value }) {
                bottomSheetContent = {
                    TeamSwitchBottomSheet(onChoosing = { newTeam ->
                        chosenTeam = newTeam
                        showBottomSheet = false
                        navController.navigate(Screen.Profile.route)
                    })
                }
                showBottomSheet = true
            }
        },
        bottomBar = {
            DashboardProfileBottomBar(navController, false) {
                bottomSheetContent =
                    {
                        ActivityCreationBottomSheet {
                            navController.navigate(Screen.Dashboard.route)
                            showBottomSheet = false
                        }
                    }
                showBottomSheet = true
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = profilePadding)
                        .padding(top = 10.dp)
                        .padding(bottom = 15.dp)
                        .fillMaxWidth()
                ) {
                    when (currentUser) {
                        is ResourceState.Loading -> {
                            Log.d("PROFILE-SCREEN", "Still loading")
                            Loader()
                            teamViewModel.getTeamByManager()
                        }

                        is ResourceState.Success -> {
                            val userResponse = (currentUser as ResourceState.Success).data
                            if (userProfileSelected) {
                                UserProfile(
                                    username = userResponse.username,
                                    firstName = userResponse.firstname,
                                    lastName = userResponse.lastname,
                                    email = userResponse.email
                                )
                            } else {
                                when (managedTeam) {
                                    is ResourceState.Loading -> {
                                        Log.d("TEAM-PROFILE-SCREEN", "Loading team...")
                                        Loader()
                                    }

                                    is ResourceState.Success -> {
                                        userIsPartOfTeam = true
                                        val managedTeamResponse =
                                            (managedTeam as ResourceState.Success).data

                                        val safePendingMembers =
                                            managedTeamResponse.pendingMembers ?: mutableListOf()
                                        TeamProfile(
                                            teamName = managedTeamResponse.name,
                                            typeOfSport = managedTeamResponse.typeOfSport,
                                            manager = managedTeamResponse.manager,
                                            inviteCode = managedTeamResponse.inviteCode,
                                            members = managedTeamResponse.members,
                                            pendingMembers = safePendingMembers,
                                            userIsAdmin = true, // TODO: Change this to check user role
                                            teamViewModel = teamViewModel,
                                            onIconClick = {
                                                    icon: ImageVector, warning: Boolean, title: String,
                                                    dialogText: String, confirmButtonText: String, onClick: () -> Unit,
                                                ->
                                                infoDialogParams = InfoDialogParams(
                                                    icon = icon,
                                                    warning = warning,
                                                    title = title,
                                                    dialogText = dialogText,
                                                    confirmButtonText = confirmButtonText
                                                ) { onClick() }
                                                openInformationDialog = true
                                            }
                                        )
                                    }

                                    is ResourceState.Error -> {
                                        Log.d("TEAM-PROFILE-SCREEN", "Error loading team")
                                        NoTeamScreen()
                                    }

                                    is ResourceState.Idle -> TODO()
                                }
                            }
                        }

                        is ResourceState.Error -> {
                            Log.d("PROFILE-SCREEN", "Error loading user")
                        }

                        is ResourceState.Idle -> {
                            //DO NOTHING
                        }
                    }
                }
            }
            item {
                Column(modifier = Modifier.padding(horizontal = profilePadding)) {
                    if (userProfileSelected) {
                        AppButton(
                            text = "Sign out"
                        ) {
                            infoDialogParams = InfoDialogParams(
                                icon = Icons.Default.Logout,
                                title = "Signing out",
                                dialogText = "You're about to sign out.",
                                confirmButtonText = "Sign out"
                            )
                            {
                                authViewModel.logout()
                                navController.navigate(Screen.Login.route)
                            }
                            openInformationDialog = true
                        }
                    } else
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                AppButton(
                                    text = "Join new Team",
                                    modifier = Modifier.fillMaxWidth(0.49f)
                                ) {
                                    when (currentUser) {
                                        is ResourceState.Success -> {
                                            val userResponse =
                                                (currentUser as ResourceState.Success).data
                                            bottomSheetContent = {
                                                TeamJoiningBottomSheet(
                                                    userId = userResponse.id,
                                                    teamViewModel = teamViewModel
                                                )
                                            }
                                            showBottomSheet = true
                                        }

                                        is ResourceState.Error -> {
                                            //DO NOTHING
                                        }

                                        is ResourceState.Idle -> {
                                            //DO NOTHING
                                        }

                                        is ResourceState.Loading -> {
                                            //DO NOTHING
                                        }
                                    }
                                }
                                AppButton(
                                    text = "Create new Team",
                                    modifier = Modifier.fillMaxWidth(0.96f)
                                ) {
                                    bottomSheetContent = {
                                        TeamCreationBottomSheet {
                                            showBottomSheet = false
                                            navController.navigate(Screen.Dashboard.route)
                                        }
                                    }
                                    showBottomSheet = true
                                }
                            }
                            if (userIsPartOfTeam) {
                                if (manager) AppButton(
                                    text = "Delete this Team"
                                ) {
                                    infoDialogParams = InfoDialogParams(
                                        icon = Icons.Default.GroupOff,
                                        warning = true,
                                        title = "Team Deletion",
                                        dialogText = "You're about to DELETE THIS TEAM!",
                                        confirmButtonText = "Delete Team"
                                    ) {
                                        // TODO: Make the API call to delete the team here
                                    }
                                    openInformationDialog = true
                                }
                                else AppButton(
                                    text = "Leave this Team"
                                ) {
                                    infoDialogParams = InfoDialogParams(
                                        icon = Icons.Default.ExitToApp,
                                        title = "Leaving this Team",
                                        dialogText = "You're about to leave this team.",
                                        confirmButtonText = "Leave Team"
                                    ) {
                                        // TODO: Make the API call to leave the team here
                                    }
                                    openInformationDialog = true
                                }
                            }
                        }
                }
            }
        }
    }
}


@Composable
fun UserProfile(
    profilePicture: Painter = painterResource(id = R.drawable.logo_app), // TODO: Change this to pass only a mapped user object
    username: String = "laurinknunz",
    firstName: String = "Laurin",
    lastName: String = "Knünz",
    email: String = "laurin.knunz@gmail.com",
) {
    var currentFirstName by remember { mutableStateOf(firstName) }
    var currentLastName by remember { mutableStateOf(lastName) }
    Column {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Image(
                painter = profilePicture,
                contentDescription = "profile picture",
                modifier = Modifier
                    .size(170.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outline)
            )
            Title(text = "$currentFirstName $currentLastName")

        }
        Column {
            profileDetailLine(name = "Username", value = username)
            profileDetailLine(name = "Password", value = "●●●●●●●●", password = true)
            currentFirstName = profileDetailLine(name = "First name", value = firstName)
            currentLastName = profileDetailLine(name = "Last name", value = lastName)
            profileDetailLine(name = "E-Mail", value = email)
        }
    }
}

@Composable
fun TeamProfile(
    teamName: String, // TODO: Change this to pass only a mapped team object
    typeOfSport: String,
    manager: TeamUser,
    inviteCode: String,
    userIsAdmin: Boolean,
    onIconClick: (ImageVector, Boolean, String, String, String, () -> Unit) -> Unit,
    members: List<TeamUser>,
    pendingMembers: List<TeamUser>?,
    teamViewModel: TeamViewModel,
) {
    var currentTeamName by remember { mutableStateOf(teamName) }
    var membersList by remember { mutableStateOf(members) }
    val safePendingMembers = pendingMembers ?: mutableListOf()

    Column {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ) {
            Title(currentTeamName, 0.dp)
        }
        Column(modifier = Modifier.padding(bottom = 10.dp)) {
            currentTeamName = profileDetailLine(
                name = "Team name",
                value = currentTeamName,
                editable = userIsAdmin
            )
            profileDetailLine(
                name = "Type of Sport",
                value = typeOfSport,
                editable = userIsAdmin
            )
            profileDetailLine(
                name = "Manager",
                value = manager.firstname + " " + manager.lastname,
                editable = false
            )
            profileDetailLine(name = "Contact", value = manager.email, editable = false)
            profileDetailLine(name = "inviteCode", value = inviteCode, editable = false)
        }
        SmallTitle(title = "Team Members")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(3.dp))
        ) {
            TeamMemberEntry(
                member = manager,
                teamName = teamName,
                userIsAdmin = true,
                teamViewModel = teamViewModel,
                onIconClick = onIconClick
            )
            for (member in membersList.filter { member -> member != manager }) {
                TeamMemberEntry(
                    member = member,
                    teamName = teamName,
                    teamViewModel = teamViewModel,
                    onIconClick = onIconClick
                )
            }
            for (pendingMember in safePendingMembers) {
                TeamMemberEntry(
                    member = pendingMember,
                    teamName = teamName,
                    userIsPending = true,
                    teamViewModel = teamViewModel,
                    onIconClick = onIconClick
                )
            }
        }
    }
}

@Composable
fun NoTeamScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "You are still part \n of no team!",
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 15.dp)
        )
        Text(
            text = "Join or create a new team with the buttons you see on the bottom.",
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 20.sp)
        )
    }
}

