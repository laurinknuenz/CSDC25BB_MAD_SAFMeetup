package at.csdc25bb.mad.safmeetup.data.repository

import android.util.Log
import at.csdc25bb.mad.safmeetup.data.datasource.ActivityDataSource
import at.csdc25bb.mad.safmeetup.data.entity.activity.Activity
import at.csdc25bb.mad.safmeetup.data.utils.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val activityDataSource: ActivityDataSource
) {
    suspend fun getAllActivitiesForUser(): Flow<ResourceState<List<Activity>>> {
        return flow {
            emit(ResourceState.Loading())

            val response = activityDataSource.getAllActivitiesForUser()
            Log.d(TAG, response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                val activityData = response.body()!!.data
                emit(ResourceState.Success(activityData))
            } else {
                emit(ResourceState.Error("Error fetching Activities data for user"))
            }
        }.catch { e ->
            emit(
                ResourceState.Error(
                    e.localizedMessage ?: "Error in flow while retrieving activities data for user"
                )
            )
        }
    }
    companion object {
        const val TAG = "ActivityRepository"
    }
}