package at.csdc25bb.mad.safmeetup.data.datasource

import android.util.Log
import at.csdc25bb.mad.safmeetup.data.api.ApiService
import at.csdc25bb.mad.safmeetup.data.api.response.ApiResponse
import at.csdc25bb.mad.safmeetup.data.entity.User
import retrofit2.Response
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
) : UserDataSource {

    override suspend fun getUser(username: String): Response<ApiResponse<User>> {
        val response = apiService.getUser(username)
        Log.d(TeamDataSourceImpl.TAG, response.body().toString())
        return response
    }

    companion object {
        const val TAG = "UserDataSourceImpl"
    }
}