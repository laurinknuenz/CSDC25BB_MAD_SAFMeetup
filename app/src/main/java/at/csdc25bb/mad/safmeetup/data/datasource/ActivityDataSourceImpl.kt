package at.csdc25bb.mad.safmeetup.data.datasource

import android.util.Log
import at.csdc25bb.mad.safmeetup.data.api.response.ApiResponse
import at.csdc25bb.mad.safmeetup.data.api.ApiService
import at.csdc25bb.mad.safmeetup.data.api.request.CreateActivityRequest
import at.csdc25bb.mad.safmeetup.data.entity.activity.Activity
import retrofit2.Response
import javax.inject.Inject

class ActivityDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
) : ActivityDataSource {
    override suspend fun getAllActivitiesForTeam(): Response<ApiResponse<List<Activity>>> {
        return apiService.getAllActivitiesForTeam()
    }

    override suspend fun getAllActivitiesForUser(): Response<ApiResponse<List<Activity>>> {
        val response = apiService.getAllActivitiesForUser()
        Log.d(TAG, response.toString())
        return response
    }

    override suspend fun createActivity(
        subject: String,
        activityType: String,
        team: String,
        opponent: String,
        location: String,
    ): Response<ApiResponse<List<Activity>>> {
        val response = apiService.createActivity(
            CreateActivityRequest(
                subject,
                activityType,
                team,
                opponent,
                location
            )
        )
        Log.d(TAG, response.toString())
        return response
    }

    companion object {
        const val TAG = "ActivityDataSourceImpl"
    }
}