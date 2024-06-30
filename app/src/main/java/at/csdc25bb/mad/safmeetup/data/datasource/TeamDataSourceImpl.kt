package at.csdc25bb.mad.safmeetup.data.datasource

import at.csdc25bb.mad.safmeetup.data.api.ApiService
import at.csdc25bb.mad.safmeetup.data.entity.Team
import retrofit2.Response
import javax.inject.Inject

class TeamDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
) : TeamDataSource {

    override suspend fun getAllTeams(): Response<List<Team>> {
        return apiService.getAllTeams()
    }

    override suspend fun getTeam(team: String): Response<Team> {
        return apiService.getTeam(team)
    }
}