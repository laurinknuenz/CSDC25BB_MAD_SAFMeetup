package at.csdc25bb.mad.safmeetup.data.datasource

import android.util.Log
import at.csdc25bb.mad.safmeetup.data.api.ApiService
import at.csdc25bb.mad.safmeetup.data.api.response.ApiResponse
import at.csdc25bb.mad.safmeetup.data.api.request.UserUpdateRequest
import at.csdc25bb.mad.safmeetup.data.entity.User
import retrofit2.Response
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
) : UserDataSource {

    override suspend fun getUser(userId: String): Response<ApiResponse<User>> {
        val response = apiService.getUser(userId)
        Log.d(TeamDataSourceImpl.TAG, response.body().toString())
        return response
    }

    override suspend fun updateUser(
        userId: String,
        firstname: String,
        lastname: String,
        username: String,
        password: String,
        email: String,
    ): Response<ApiResponse<User>> {
        val response = apiService.updateUser(
            userId,
            UserUpdateRequest(firstname, lastname, username, password, email)
        )
        Log.d(TeamDataSourceImpl.TAG, response.body().toString())
        return response
    }

    companion object {
        const val TAG = "UserDataSourceImpl"
    }
}