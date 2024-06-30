package at.csdc25bb.mad.safmeetup.data.datasource

import at.csdc25bb.mad.safmeetup.data.entity.Team
import retrofit2.Response

interface TeamDataSource {

    suspend fun getAllTeams(): Response<List<Team>>
    suspend fun getTeam(team: String): Response<Team>
}