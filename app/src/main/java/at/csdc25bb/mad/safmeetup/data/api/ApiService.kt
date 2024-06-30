package at.csdc25bb.mad.safmeetup.data.api

import at.csdc25bb.mad.safmeetup.data.entity.Team
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/team/all")
    suspend fun getAllTeams(): Response<List<Team>>

    @GET("/team")
    suspend fun getTeam(
        @Query("team") team: String
    ): Response<Team>
}