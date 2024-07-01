package at.csdc25bb.mad.safmeetup.data.datasource

import at.csdc25bb.mad.safmeetup.data.api.response.ApiResponse
import at.csdc25bb.mad.safmeetup.data.entity.User
import retrofit2.Response

interface UserDataSource {

    suspend fun getUser(username: String):  Response<ApiResponse<User>>
}