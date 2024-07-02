package at.csdc25bb.mad.safmeetup.data.datasource

import at.csdc25bb.mad.safmeetup.data.api.response.ApiResponse
import at.csdc25bb.mad.safmeetup.data.entity.activity.Activity
import retrofit2.Response

interface ActivityDataSource {
    suspend fun getAllActivitiesForTeam(): Response<ApiResponse<List<Activity>>>
    suspend fun getAllActivitiesForUser(): Response<ApiResponse<List<Activity>>>
    suspend fun createActivity(
        subject: String,
        activityType: String,
        team: String,
        opponent: String,
        location: String,
    ): Response<ApiResponse<List<Activity>>>
}