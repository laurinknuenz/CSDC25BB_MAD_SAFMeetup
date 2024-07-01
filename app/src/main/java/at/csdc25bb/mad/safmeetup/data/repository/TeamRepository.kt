package at.csdc25bb.mad.safmeetup.data.repository

import android.util.Log
import at.csdc25bb.mad.safmeetup.data.datasource.TeamDataSource
import at.csdc25bb.mad.safmeetup.data.entity.Team
import at.csdc25bb.mad.safmeetup.data.utils.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TeamRepository @Inject constructor(
    private val teamDataSource: TeamDataSource,
) {


    suspend fun getTeam(team: String): Flow<ResourceState<Team>> {
        return flow {
            emit(ResourceState.Loading())

            val response = teamDataSource.getTeam(team)
            Log.d("GET-TEAM", response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                val teamData = response.body()!!.data
                emit(ResourceState.Success(teamData))
            } else {
                emit(ResourceState.Error("Error fetching Team data"))
            }
        }.catch { e ->
            emit(
                ResourceState.Error(
                    e.localizedMessage ?: "Error in flow while retrieving Team data"
                )
            )
        }
    }

    suspend fun getAllTeams(): Flow<ResourceState<List<Team>>> {
        return flow {
            emit(ResourceState.Loading())

            val response = teamDataSource.getAllTeams()

            if (response.isSuccessful && response.body() != null) {
                val teamsData = response.body()!!.data
                emit(ResourceState.Success(teamsData))
            } else {
                emit(ResourceState.Error("Error fetching Team data"))
            }
        }.catch { e ->
            emit(
                ResourceState.Error(
                    e.localizedMessage ?: "Error in flow while retrieving Team data"
                )
            )
        }
    }
}