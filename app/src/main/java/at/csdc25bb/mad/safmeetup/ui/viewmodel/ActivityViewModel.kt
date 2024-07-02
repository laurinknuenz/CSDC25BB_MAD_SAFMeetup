package at.csdc25bb.mad.safmeetup.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.csdc25bb.mad.safmeetup.data.entity.activity.Activity
import at.csdc25bb.mad.safmeetup.data.repository.ActivityRepository
import at.csdc25bb.mad.safmeetup.data.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
): ViewModel() {

    private val _userActivities: MutableStateFlow<ResourceState<List<Activity>>> =
        MutableStateFlow(ResourceState.Loading())

    val userActivities: StateFlow<ResourceState<List<Activity>>> = _userActivities

    fun getAllActivitiesForUser() {
        viewModelScope.launch ( Dispatchers.IO ) {
            activityRepository.getAllActivitiesForUser().collectLatest { activityResponse ->
                Log.d(TAG, activityResponse.toString())
                _userActivities.value = activityResponse
            }
        }
    }

    fun createActivity(
        subject: String,
        activityType: String,
        team: String,
        opponent: String,
        location: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            activityRepository.createActivity(subject, activityType, team, opponent, location)
                .collectLatest { activityResponse ->
                    Log.d(TAG, activityResponse.toString())
                    _userActivities.value = activityResponse
                }
        }
    }

    init {
        Log.d(TAG, "In the init function of the $TAG")
        getAllActivitiesForUser()
    }

    companion object {
        const val TAG = "ActivityViewModel"
    }
}