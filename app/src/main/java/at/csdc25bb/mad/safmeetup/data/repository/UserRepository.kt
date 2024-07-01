package at.csdc25bb.mad.safmeetup.data.repository

import android.content.Context
import android.util.Log
import at.csdc25bb.mad.safmeetup.SFMApplication
import at.csdc25bb.mad.safmeetup.data.api.ApiService
import at.csdc25bb.mad.safmeetup.data.api.request.LoginRequest
import at.csdc25bb.mad.safmeetup.data.api.response.LogoutResponse
import at.csdc25bb.mad.safmeetup.data.api.request.RegisterRequest
import at.csdc25bb.mad.safmeetup.data.datasource.UserDataSource
import at.csdc25bb.mad.safmeetup.data.entity.User
import at.csdc25bb.mad.safmeetup.data.utils.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDataSource: UserDataSource
) {

    val sharedPref = SFMApplication.instance.getSharedPreferences("SFMApplication", Context.MODE_PRIVATE)

    suspend fun login(username: String, password: String): Flow<ResourceState<User>> {
        return flow {
            emit(ResourceState.Loading())
            val response = apiService.login(LoginRequest(username, password))
            Log.d(TAG, response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
                with(sharedPref.edit()) {
                    putString("username", response.body()!!.username)
                    apply()
                }
            } else {
                Log.d(TAG, response.body().toString())
                emit(ResourceState.Error("Invalid username or password"))
            }
        }.catch { e ->
            emit(ResourceState.Error(e.localizedMessage ?: "Error logging in"))
        }
    }

    suspend fun logout(): Flow<ResourceState<LogoutResponse>> {
        return flow {
            emit(ResourceState.Loading())

            val response = apiService.logout()

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
                with(sharedPref.edit()) {
                    remove("username")
                    apply()
                }
            } else {
                emit(ResourceState.Error("Error during logout process"))
            }
        }.catch { e ->
            Log.d(TAG, e.localizedMessage)
            emit(ResourceState.Error(e.localizedMessage ?: "Error logging out"))
        }
    }

    suspend fun register(
        firstname: String,
        lastname: String,
        username: String,
        email: String,
        password: String,
        inviteCode: String,
    ): Flow<ResourceState<User>> {
        return flow {
            emit(ResourceState.Loading())

            val response =
                apiService.register(
                    RegisterRequest(
                        firstname,
                        lastname,
                        username,
                        email,
                        password,
                        inviteCode
                    )
                )
            Log.d(TAG, response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!.data))
            } else {
                emit(ResourceState.Error("Registration failed"))
            }
        }.catch { e ->
            emit(ResourceState.Error(e.localizedMessage ?: "Error during registration"))
        }
    }

    suspend fun getUser(): Flow<ResourceState<User>> {
        return flow {
            emit(ResourceState.Loading())
            val username = sharedPref.getString("username", "")
            val response = userDataSource.getUser(username!!)
            Log.d(TAG, response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                val userData = response.body()!!.data
                emit(ResourceState.Success(userData))
            }
            else {
                emit(ResourceState.Error("Invalid username or password"))
            }
        }.catch { e ->
            emit(ResourceState.Error(e.localizedMessage ?: "Error logging in"))
        }
    }

    companion object {
        const val TAG = "ActivityRepository"
    }
}
