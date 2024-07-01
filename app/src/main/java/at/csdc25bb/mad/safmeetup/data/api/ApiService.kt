package at.csdc25bb.mad.safmeetup.data.api

import at.csdc25bb.mad.safmeetup.data.api.request.LoginRequest
import at.csdc25bb.mad.safmeetup.data.api.request.RegisterRequest
import at.csdc25bb.mad.safmeetup.data.api.response.ApiResponse
import at.csdc25bb.mad.safmeetup.data.api.response.LogoutResponse
import at.csdc25bb.mad.safmeetup.data.entity.activity.Activity
import at.csdc25bb.mad.safmeetup.data.entity.Team
import at.csdc25bb.mad.safmeetup.data.entity.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/team/all")
    suspend fun getAllTeams(): Response<ApiResponse<List<Team>>>

    @GET("api/team")
    suspend fun getTeam(
        @Query("team") team: String
    ): Response<ApiResponse<Team>>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<User>

    @POST("api/auth/logout")
    suspend fun logout(): Response<LogoutResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<User>>

    @GET("api/activity")
    suspend fun getAllActivitiesForTeam(): Response<ApiResponse<List<Activity>>>

    @GET("api/activity/user")
    suspend fun getAllActivitiesForUser(): Response<ApiResponse<List<Activity>>>

    @GET("api/user/{username}")
    suspend fun getUser(@Path("username") username: String): Response<ApiResponse<User>>
}