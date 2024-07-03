package at.csdc25bb.mad.safmeetup.data.datasource

import at.csdc25bb.mad.safmeetup.data.api.response.ApiResponse
import at.csdc25bb.mad.safmeetup.data.entity.activity.Activity
import retrofit2.Response

interface ActivityDataSource {
    suspend fun getActivityById(activityId: String): Response<ApiResponse<Activity>>
    suspend fun getAllActivitiesForTeam(): Response<ApiResponse<List<Activity>>>
    suspend fun getAllActivitiesForUser(): Response<ApiResponse<List<Activity>>>
    suspend fun updateAttendanceForUser(
        activityId: String,
        guestUserId: String,
        attendance: Boolean,
    ): Response<ApiResponse<Activity>>

    suspend fun createActivity(
        subject: String,
        activityType: String,
        team: String,
        opponent: String,
        location: String,
    ): Response<ApiResponse<List<Activity>>>
}