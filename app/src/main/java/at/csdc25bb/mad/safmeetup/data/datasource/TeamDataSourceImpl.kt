package at.csdc25bb.mad.safmeetup.data.datasource

import android.util.Log
import at.csdc25bb.mad.safmeetup.data.api.ApiResponse
import at.csdc25bb.mad.safmeetup.data.api.ApiService
import at.csdc25bb.mad.safmeetup.data.entity.Team
import retrofit2.Response
import javax.inject.Inject

class TeamDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
) : TeamDataSource {

    override suspend fun getAllTeams(): Response<ApiResponse<List<Team>>> {
        return apiService.getAllTeams()
    }

    override suspend fun getTeam(team: String): Response<ApiResponse<Team>> {
        val response = apiService.getTeam(team)
        Log.d(TAG, response.body().toString())
        return response
    }

    companion object {
        const val TAG = "TeamDataSourceImpl"
    }
}