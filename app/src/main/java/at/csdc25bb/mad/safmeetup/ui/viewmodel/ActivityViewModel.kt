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
    private val activityRepository: ActivityRepository,
) : ViewModel() {

    private val _currentActivity: MutableStateFlow<Activity> = MutableStateFlow(Activity())

    private val _userActivities: MutableStateFlow<ResourceState<List<Activity>>> =
        MutableStateFlow(ResourceState.Loading())

    val currentActivity: StateFlow<Activity> = _currentActivity

    val activities: MutableList<Activity> = mutableListOf()

    fun getActivityById(activityId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            activityRepository.getActivityById(activityId).collectLatest { activityResponse ->
                Log.d(TAG, activityResponse.toString())
                _currentActivity.value = activityResponse
            }
        }
    }

    fun updateAttendanceForUser(
        activityId: String,
        guestUserId: String,
        attendance: Boolean,
    ) {
        Log.d(TAG, "Update attendance for user $guestUserId called $attendance")
        viewModelScope.launch(Dispatchers.IO) {
            activityRepository.updateAttendanceForUser(activityId, guestUserId, attendance)
                .collectLatest { activityResponse ->
                    Log.d(TAG, activityResponse.toString())
                    _currentActivity.value = activityResponse
                }
        }
    }

    fun getAllActivitiesForUser() {
        viewModelScope.launch(Dispatchers.IO) {
            activityRepository.getAllActivitiesForUser().collectLatest { activityResponse ->
                Log.d(TAG, activityResponse.toString())
                _userActivities.value = activityResponse
            }
        }
    }

    fun getAllActivitiesForUserFetched(): MutableList<Activity> {
        viewModelScope.launch(Dispatchers.IO) {
            activityRepository.getAllActivitiesForUserFetched().collectLatest { activityResponse ->
                activities.clear()
                activities.addAll(activityResponse)
                Log.d(TAG, "In the fetch function")
                Log.d(TAG, activityResponse.toString())
            }
        }
        Log.d(TAG, "Returning from the fetch function")
        Log.d(TAG, activities.toString())
        return activities
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

//    init {
//        Log.d(TAG, "In the init function of the $TAG")
//        getAllActivitiesForUser()
//    }

    companion object {
        const val TAG = "ActivityViewModel"
    }
}