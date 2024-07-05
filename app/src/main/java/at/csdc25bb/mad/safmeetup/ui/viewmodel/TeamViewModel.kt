package at.csdc25bb.mad.safmeetup.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.csdc25bb.mad.safmeetup.data.entity.team.Team
import at.csdc25bb.mad.safmeetup.data.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
) : ViewModel() {

    private val _team: MutableStateFlow<Team> =
        MutableStateFlow(Team())
    private val _managedTeam: MutableStateFlow<Team> =
        MutableStateFlow(Team())
    private val _allTeams: MutableStateFlow<List<Team>> =
        MutableStateFlow(emptyList())

    val team: StateFlow<Team> = _team
    val managedTeam: StateFlow<Team> = _managedTeam
    val allTeams: StateFlow<List<Team>> = _allTeams

    fun clearTeamsOnLogout() {
        _team.value = Team()
        _managedTeam.value = Team()
    }

    fun getTeamsForUser(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            teamRepository.getTeamsForUser(userId).collectLatest { teamsResponse ->
                Log.d(TAG, "getTeamsForUser")
                Log.d(TAG, teamsResponse.toString())
                _allTeams.value = teamsResponse
            }
        }
    }

    fun getTeamByManager() {
        viewModelScope.launch(Dispatchers.IO) {
            teamRepository.getTeamByManager().collectLatest { managedTeamResponse ->
                Log.d(TAG, "getTeamByManager")
                Log.d(TAG, managedTeamResponse.toString())
                _managedTeam.value = managedTeamResponse
            }
        }
    }

    fun createTeam(name: String, typeOfSport: String) {
        viewModelScope.launch(Dispatchers.IO) {
            teamRepository.createTeam(name, typeOfSport).collectLatest { createdTeam ->
                Log.d(TAG, createdTeam.toString())
                _team.value = createdTeam
            }
        }
    }

    fun updateTeam(
        teamId: String,
        name: String,
        typeOfSport: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            teamRepository.updateTeam(
                teamId,
                name,
                typeOfSport
            ).collectLatest { createdTeam ->
                Log.d(TAG, createdTeam.toString())
                _team.value = createdTeam
            }
        }
    }

    fun joinTeam(userId: String, inviteCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Sending request to join team: ${userId}, $inviteCode")
            teamRepository.joinTeam(userId, inviteCode).collectLatest { joinedTeam ->
                _team.value = joinedTeam
            }
        }
    }

    fun addUserToTeam(userId: String, teamName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Sending request to add user to the team: ${userId}, $teamName")
            teamRepository.addUserToTeam(userId, teamName)
        }
    }

    fun removeUserFromTeam(userId: String, teamName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Sending request to remove user from the team: ${userId}, $teamName")
            teamRepository.removeUserFromTeam(userId, teamName)
        }
    }


    companion object {
        const val TAG = "TeamViewModel"
    }
}