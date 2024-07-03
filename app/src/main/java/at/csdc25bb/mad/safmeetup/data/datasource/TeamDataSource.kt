package at.csdc25bb.mad.safmeetup.data.datasource

import at.csdc25bb.mad.safmeetup.data.api.response.ApiResponse
import at.csdc25bb.mad.safmeetup.data.entity.team.Team
import retrofit2.Response

interface TeamDataSource {

    suspend fun getAllTeams(): Response<ApiResponse<List<Team>>>
    suspend fun getTeamsForUser(userId: String): Response<ApiResponse<List<Team>>>
    suspend fun getTeamByManager(): Response<ApiResponse<Team>>
    suspend fun joinTeam(userId: String, inviteCode: String): Response<ApiResponse<Team>>
    suspend fun addUserToTeam(userId: String, teamName: String): Response<ApiResponse<Team>>
    suspend fun removeUserFromTeam(userId: String, teamName: String): Response<ApiResponse<Team>>
    suspend fun createTeam(name: String, typeOfSport: String): Response<ApiResponse<Team>>

}