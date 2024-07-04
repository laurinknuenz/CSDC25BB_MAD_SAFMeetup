package at.csdc25bb.mad.safmeetup.data.datasource

import at.csdc25bb.mad.safmeetup.data.api.response.ApiResponse
import at.csdc25bb.mad.safmeetup.data.entity.User
import retrofit2.Response

interface UserDataSource {

    suspend fun getUser(userId: String): Response<ApiResponse<User>>

    suspend fun updateUser(
        userId: String,
        firstname: String,
        lastname: String,
        username: String,
        password: String,
        email: String,
    ): Response<ApiResponse<User>>

}