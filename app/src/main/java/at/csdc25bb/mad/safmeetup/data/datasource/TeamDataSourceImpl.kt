package at.csdc25bb.mad.safmeetup.data.datasource

import android.util.Log
import at.csdc25bb.mad.safmeetup.data.api.ApiService
import at.csdc25bb.mad.safmeetup.data.api.request.CreateTeamRequest
import at.csdc25bb.mad.safmeetup.data.api.request.UpdateTeamRequest
import at.csdc25bb.mad.safmeetup.data.api.request.UserTeamAddRemoveRequest
import at.csdc25bb.mad.safmeetup.data.api.request.UserTeamJoinRequest
import at.csdc25bb.mad.safmeetup.data.api.response.ApiResponse
import at.csdc25bb.mad.safmeetup.data.entity.team.Team
import retrofit2.Response
import javax.inject.Inject

class TeamDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
) : TeamDataSource {

    override suspend fun getAllTeams(): Response<ApiResponse<List<Team>>> {
        return apiService.getAllTeams()
    }

    override suspend fun getTeamsForUser(userId: String): Response<ApiResponse<List<Team>>> {
        val response = apiService.getTeamsForUser(userId)
        Log.d(TAG, "Getting the teams for user")
        Log.d(TAG, response.body().toString())
        return response
    }

    override suspend fun getTeamByManager(): Response<ApiResponse<Team>> {
        val response = apiService.getTeamByManager()
        Log.d(TAG, response.body().toString())
        return response
    }

    override suspend fun joinTeam(userId: String, inviteCode: String): Response<ApiResponse<Team>> {
        Log.d(TAG, "Sending request to join the team")
        val response = apiService.joinTeam(
            UserTeamJoinRequest(
                userId, inviteCode
            )
        )
        Log.d(TAG, response.body().toString())
        return response
    }

    override suspend fun addUserToTeam(
        userId: String,
        teamName: String,
    ): Response<ApiResponse<Team>> {
        Log.d(TAG, "Sending request to join the team")
        val response = apiService.addUserToTeam(
            UserTeamAddRemoveRequest(
                userId, teamName
            )
        )
        Log.d(TAG, response.body().toString())
        return response
    }

    override suspend fun removeUserFromTeam(
        userId: String,
        teamName: String,
    ): Response<ApiResponse<Team>> {
        Log.d(TAG, "Sending request to remove the user from the team")
        val response = apiService.removeUserFromTeam(
            UserTeamAddRemoveRequest(
                userId, teamName
            )
        )
        Log.d(TAG, response.body().toString())
        return response
    }


    override suspend fun createTeam(
        name: String,
        typeOfSport: String,
    ): Response<ApiResponse<Team>> {
        val response = apiService.createTeam(
            CreateTeamRequest(
                name, typeOfSport
            )
        )
        Log.d(TAG, response.body().toString())
        return response
    }

    override suspend fun updateTeam(
        teamId: String,
        name: String,
        typeOfSport: String
    ): Response<ApiResponse<Team>> {
        val response = apiService.updateTeam(
            teamId, UpdateTeamRequest(name, typeOfSport)

        )
        Log.d(TAG, response.body().toString())
        return response
    }

    companion object {
        const val TAG = "TeamDataSourceImpl"
    }
}