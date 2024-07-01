package at.csdc25bb.mad.safmeetup.screens

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
import androidx.compose.material.icons.filled.PersonOff
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
import androidx.navigation.NavController
import at.csdc25bb.mad.safmeetup.R
import at.csdc25bb.mad.safmeetup.composables.ActivityCreationBottomSheet
import at.csdc25bb.mad.safmeetup.composables.AppButton
import at.csdc25bb.mad.safmeetup.composables.BottomSheet
import at.csdc25bb.mad.safmeetup.composables.DashboardProfileBottomBar
import at.csdc25bb.mad.safmeetup.composables.InfoDialogParams
import at.csdc25bb.mad.safmeetup.composables.InformationDialog
import at.csdc25bb.mad.safmeetup.composables.ProfilePageTopBar
import at.csdc25bb.mad.safmeetup.composables.SmallTitle
import at.csdc25bb.mad.safmeetup.composables.TeamCreationBottomSheet
import at.csdc25bb.mad.safmeetup.composables.TeamJoiningBottomSheet
import at.csdc25bb.mad.safmeetup.composables.TeamMemberEntry
import at.csdc25bb.mad.safmeetup.composables.TeamSwitchBottomSheet
import at.csdc25bb.mad.safmeetup.composables.Title
import at.csdc25bb.mad.safmeetup.composables.profileDetailLine
import at.csdc25bb.mad.safmeetup.navigation.Screen
import at.csdc25bb.mad.safmeetup.ui.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel, manager: Boolean = true) {
    var errorMessage by remember { mutableStateOf("") }
    val logoutState by authViewModel.logoutState.collectAsState()

    var userProfileSelected by remember { mutableStateOf(true) }
    var chosenTeam by remember { mutableStateOf("Laurins Team") }
    val userIsPartOfTeam = true

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
                    if (userProfileSelected) UserProfile()
                    else if (userIsPartOfTeam) TeamProfile(
                        userIsAdmin = true, // TODO: Change this to check user role
                        onIconClick = { icon: ImageVector, warning: Boolean, title: String,
                                        dialogText: String, confirmButtonText: String, onClick: () -> Unit ->
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
                    else NoTeamScreen()
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
                        AppButton(
                            text = "Delete my Account"
                        ) {
                            infoDialogParams = InfoDialogParams(
                                icon = Icons.Default.PersonOff,
                                warning = true,
                                title = "Account Deletion",
                                dialogText = "You're about to DELETE YOUR ACCOUNT!",
                                confirmButtonText = "Delete Account"
                            ) {
                                // TODO: Make the API call to delete account here
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
                                    bottomSheetContent = { TeamJoiningBottomSheet() }
                                    showBottomSheet = true
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
    userName: String = "laurinknunz",
    firstName: String = "Laurin",
    lastName: String = "Knünz",
    email: String = "laurin.knunz@gmail.com"
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
            profileDetailLine(name = "Username", value = userName)
            profileDetailLine(name = "Password", value = "●●●●●●●●", password = true)
            currentFirstName = profileDetailLine(name = "First name", value = firstName)
            currentLastName = profileDetailLine(name = "Last name", value = lastName)
            profileDetailLine(name = "E-Mail", value = email)
        }
    }
}

@Composable
fun TeamProfile(
    teamName: String = "Laurins Team", // TODO: Change this to pass only a mapped team object
    typeOfSport: String = "Floorball",
    manager: String = "Laurin Knünz",
    managerContact: String = "laurin.knunz@gmail.com",
    inviteCode: String = "L4UR1N",
    userIsAdmin: Boolean,
    onIconClick: (ImageVector, Boolean, String, String, String, () -> Unit) -> Unit,
    members: MutableList<List<String>> = mutableListOf(
        mutableListOf("Admin", "Laurin Knünz", "You"),
        mutableListOf("Admin", "Sorin Lazar", ""),
        mutableListOf("User", "Lilli Jahn", ""),
        mutableListOf("User", "Mathias Leitgeb", ""),
        mutableListOf("User", "Leon Freudenthaler", ""),
        mutableListOf("User", "Arik Kofranek", ""),
        mutableListOf("User", "Fabian Maier", ""),
        mutableListOf("User", "Burak Kongo", "pending"),
        mutableListOf("User", "Mathias Kerndl", "pending"),
        mutableListOf("User", "Rene Goldschmid", "pending"),
        mutableListOf("User", "Judy Kardouh", "pending"),
    )// TODO: Replace with list of users
) {
    var currentTeamName by remember { mutableStateOf(teamName) }
    var membersList by remember { mutableStateOf(members) }
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
            profileDetailLine(name = "Manager", value = manager, editable = false)
            profileDetailLine(name = "Contact", value = managerContact, editable = false)
            profileDetailLine(name = "inviteCode", value = inviteCode, editable = false)
        }
        SmallTitle(title = "Team Members")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(3.dp))
        ) {
            membersList = membersList
                .sortedWith(compareBy(
                    { it[2] != "You" },
                    { it[2] != "" },
                    { it[2] != "pending" },
                    { it[0] != "Admin" },
                    { it[1] }
                ))
                .toMutableList()

            for (member in membersList) {
                TeamMemberEntry(member, userIsAdmin, onIconClick) { changedMember ->
                    membersList = membersList.mapNotNull {
                        if (it == member) changedMember else it
                    }.toMutableList()
                }
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

