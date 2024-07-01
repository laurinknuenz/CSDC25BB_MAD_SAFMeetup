package at.csdc25bb.mad.safmeetup.data.datasource

import at.csdc25bb.mad.safmeetup.data.api.ApiResponse
import at.csdc25bb.mad.safmeetup.data.entity.Team
import retrofit2.Response

interface TeamDataSource {

    suspend fun getAllTeams(): Response<ApiResponse<List<Team>>>
    suspend fun getTeam(team: String): Response<ApiResponse<Team>>
}