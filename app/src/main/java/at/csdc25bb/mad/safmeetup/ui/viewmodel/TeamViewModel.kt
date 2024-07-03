package at.csdc25bb.mad.safmeetup.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.csdc25bb.mad.safmeetup.data.entity.team.Team
import at.csdc25bb.mad.safmeetup.data.repository.TeamRepository
import at.csdc25bb.mad.safmeetup.data.utils.ResourceState
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
    private val _allTeams: MutableStateFlow<ResourceState<List<Team>>> =
        MutableStateFlow(ResourceState.Loading())

    val team: StateFlow<Team> = _team
    val managedTeam: StateFlow<Team> = _managedTeam
    val allTeams: StateFlow<ResourceState<List<Team>>> = _allTeams

    fun getAllTeams() {
        viewModelScope.launch(Dispatchers.IO) {
            teamRepository.getAllTeams().collectLatest { allTeamsResponse ->
                _allTeams.value = allTeamsResponse
            }
        }
    }

    fun getTeam(team: String) {
        viewModelScope.launch(Dispatchers.IO) {
            teamRepository.getTeam(team).collectLatest { teamResponse ->
                Log.d(TAG, teamResponse.toString())
//                _team.value = teamResponse
            }
        }
    }

    fun getTeamByManager() {
        viewModelScope.launch(Dispatchers.IO) {
            teamRepository.getTeamByManager().collectLatest { managedTeamResponse ->
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