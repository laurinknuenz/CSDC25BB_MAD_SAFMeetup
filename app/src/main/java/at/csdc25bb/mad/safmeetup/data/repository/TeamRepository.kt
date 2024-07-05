package at.csdc25bb.mad.safmeetup.data.repository

import android.util.Log
import at.csdc25bb.mad.safmeetup.data.datasource.TeamDataSource
import at.csdc25bb.mad.safmeetup.data.entity.team.Team
import at.csdc25bb.mad.safmeetup.data.utils.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TeamRepository @Inject constructor(
    private val teamDataSource: TeamDataSource,
) {

    suspend fun getTeamsForUser(userId: String): Flow<List<Team>> {
        return flow {
            val response = teamDataSource.getTeamsForUser(userId)
            Log.d("GET-TEAMS-FOR-USER", response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                val teamData = response.body()!!.data
                emit(teamData)
            } else {
                emit(emptyList())
            }
        }.catch { e ->
            emit(emptyList())
        }
    }

    suspend fun getTeamByManager(): Flow<Team> {
        return flow {

            val response = teamDataSource.getTeamByManager()
            Log.d("GET-TEAM-BY-MANAGER", response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                val teamData = response.body()!!.data
                emit(teamData)
            } else {
                emit(Team())
            }
        }.catch { e ->
            emit(Team())
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

    suspend fun createTeam(name: String, typeOfSport: String): Flow<Team> {
        return flow {
            Log.d(TAG, "[TEAM-REPO] Sending request to create a team")
            val response = teamDataSource.createTeam(name, typeOfSport)
            Log.d(TAG, response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                val teamData = response.body()!!.data
                Log.d(TAG, teamData.toString())
                emit(teamData)
            }
        }.catch { e ->
            emit(Team())
            Log.d(TAG, "Error while sending create team request")
        }
    }

    suspend fun updateTeam(
        teamId: String,
        name: String,
        typeOfSport: String,
    ): Flow<Team> {
        return flow {
            Log.d(TAG, "[TEAM-REPO] Sending request to update a team")
            val response = teamDataSource.updateTeam(
                teamId,
                name,
                typeOfSport
            )
            Log.d(TAG, response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                val teamData = response.body()!!.data
                Log.d(TAG, teamData.toString())
                emit(teamData)
            }
        }.catch { e ->
            emit(Team())
            Log.d(TAG, "Error while sending update team request")
        }
    }

    suspend fun joinTeam(userId: String, inviteCode: String): Flow<Team> {
        return flow {
            Log.d(TAG, "[TEAM-REPO] Sending request to join team")
            val response = teamDataSource.joinTeam(userId, inviteCode)
            Log.d(TAG, response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                val teamData = response.body()!!.data
                Log.d(TAG, teamData.pendingMembers.toString())
                emit(teamData)
            }
        }.catch { e ->
            emit(Team())
        }
    }


    suspend fun addUserToTeam(userId: String, teamName: String): Unit {
        try {
            Log.d(TAG, "[TEAM-REPO] Sending request to add user $userId to the team $teamName")
            val response = teamDataSource.addUserToTeam(userId, teamName)
            Log.d(TAG, response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                val teamData = response.body()!!.data
                Log.d(TAG, teamData.pendingMembers.toString())
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error while sending add request")
        }
    }

    suspend fun removeUserFromTeam(userId: String, teamName: String): Unit {
        try {
            Log.d(TAG, "[TEAM-REPO] Sending request to remove user $userId from the team $teamName")
            val response = teamDataSource.removeUserFromTeam(userId, teamName)
            Log.d(TAG, response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                val teamData = response.body()!!.data
                Log.d(TAG, teamData.pendingMembers.toString())
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error while sending remove request")
        }
    }

    companion object {
        const val TAG = "TeamRepository"
    }
}