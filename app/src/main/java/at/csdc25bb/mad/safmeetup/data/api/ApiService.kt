package at.csdc25bb.mad.safmeetup.data.api

import at.csdc25bb.mad.safmeetup.data.api.request.CreateActivityRequest
import at.csdc25bb.mad.safmeetup.data.api.request.CreateTeamRequest
import at.csdc25bb.mad.safmeetup.data.api.request.LoginRequest
import at.csdc25bb.mad.safmeetup.data.api.request.RegisterRequest
import at.csdc25bb.mad.safmeetup.data.api.request.UpdateActivityRequest
import at.csdc25bb.mad.safmeetup.data.api.request.UpdateTeamRequest
import at.csdc25bb.mad.safmeetup.data.api.request.UserTeamAddRemoveRequest
import at.csdc25bb.mad.safmeetup.data.api.request.UserTeamJoinRequest
import at.csdc25bb.mad.safmeetup.data.api.response.ApiResponse
import at.csdc25bb.mad.safmeetup.data.api.response.LogoutResponse
import at.csdc25bb.mad.safmeetup.data.api.request.UserUpdateRequest
import at.csdc25bb.mad.safmeetup.data.entity.User
import at.csdc25bb.mad.safmeetup.data.entity.activity.Activity
import at.csdc25bb.mad.safmeetup.data.entity.team.Team
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("api/team/all")
    suspend fun getAllTeams(): Response<ApiResponse<List<Team>>>

    @GET("api/team/all/{userId}")
    suspend fun getTeamsForUser(@Path("userId") userId: String): Response<ApiResponse<List<Team>>>

    @POST("api/team")
    suspend fun createTeam(@Body request: CreateTeamRequest): Response<ApiResponse<Team>>

    @GET("api/team/manager")
    suspend fun getTeamByManager(): Response<ApiResponse<Team>>

    @POST("api/team/user/join")
    suspend fun joinTeam(@Body request: UserTeamJoinRequest): Response<ApiResponse<Team>>

    @POST("api/team/user/add")
    suspend fun addUserToTeam(@Body request: UserTeamAddRemoveRequest): Response<ApiResponse<Team>>

    @POST("api/team/user/remove")
    suspend fun removeUserFromTeam(@Body request: UserTeamAddRemoveRequest): Response<ApiResponse<Team>>

    @PUT("api/team/{teamId}")
    suspend fun updateTeam(@Path("teamId") teamId: String, @Body request: UpdateTeamRequest): Response<ApiResponse<Team>>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<User>

    @POST("api/auth/logout")
    suspend fun logout(): Response<LogoutResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<User>>

    @GET("api/activity")
    suspend fun getAllActivitiesForTeam(): Response<ApiResponse<List<Activity>>>

    @POST("api/activity")
    suspend fun createActivity(@Body request: CreateActivityRequest): Response<ApiResponse<List<Activity>>>

    @POST("api/activity/updateAttendance")
    suspend fun updateAttendanceForUser(@Body request: UpdateActivityRequest): Response<ApiResponse<Activity>>

    @GET("api/activity/user")
    suspend fun getAllActivitiesForUser(): Response<ApiResponse<List<Activity>>>

    @GET("api/activity/{activityId}")
    suspend fun getActivityById(@Path("activityId") activityId: String): Response<ApiResponse<Activity>>

    @GET("api/user/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<ApiResponse<User>>

    @PUT("api/user/{userId}")
    suspend fun updateUser(@Path("userId") userId: String, @Body request: UserUpdateRequest): Response<ApiResponse<User>>
}