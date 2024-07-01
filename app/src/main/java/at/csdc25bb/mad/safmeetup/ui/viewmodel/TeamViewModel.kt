package at.csdc25bb.mad.safmeetup.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.csdc25bb.mad.safmeetup.data.entity.Team
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

    private val _team: MutableStateFlow<ResourceState<Team>> =
        MutableStateFlow(ResourceState.Loading())
    private val _allTeams: MutableStateFlow<ResourceState<List<Team>>> =
        MutableStateFlow(ResourceState.Loading())

    val team: StateFlow<ResourceState<Team>> = _team
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
                _team.value = teamResponse
            }
        }
    }

    companion object {
        const val TAG = "TeamViewModel"
    }
}