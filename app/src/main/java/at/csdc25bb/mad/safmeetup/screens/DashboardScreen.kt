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
import at.csdc25bb.mad.safmeetup.composables.searchBar

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
            var searchText: String = searchBar(modifier = Modifier.padding(horizontal = dashboardPadding)) // TODO: Make search filter the lazy list
            Divider(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp)
                    .padding(bottom = 5.dp)
                    .padding(horizontal = 4.dp),
                color = MaterialTheme.colorScheme.outline
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dashboardPadding)
            ) {
                items(6) {
                    ActivityCard()
                }
            }
        }
    }
}
