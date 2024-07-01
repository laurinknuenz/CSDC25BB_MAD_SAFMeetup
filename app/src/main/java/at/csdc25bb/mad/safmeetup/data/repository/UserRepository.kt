package at.csdc25bb.mad.safmeetup.data.repository

import android.util.Log
import at.csdc25bb.mad.safmeetup.data.api.ApiService
import at.csdc25bb.mad.safmeetup.data.api.LoginRequest
import at.csdc25bb.mad.safmeetup.data.api.LogoutResponse
import at.csdc25bb.mad.safmeetup.data.api.RegisterRequest
import at.csdc25bb.mad.safmeetup.data.entity.User
import at.csdc25bb.mad.safmeetup.data.utils.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
) {

    suspend fun login(username: String, password: String): Flow<ResourceState<User>> {
        return flow {
            emit(ResourceState.Loading())
            val response = apiService.login(LoginRequest(username, password))
            Log.d("LOGIN", response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {
                Log.d("LOGIN", response.body().toString())
                emit(ResourceState.Error("Invalid username or password"))
            }
        }.catch { e ->
            emit(ResourceState.Error(e.localizedMessage ?: "Error logging in"))
        }
    }

    suspend fun logout(): Flow<ResourceState<LogoutResponse>> {
        return flow {
            emit(ResourceState.Loading())

            Log.d("UserRepository", "Logout button clicked")
            val response = apiService.logout()
            Log.d("UserRepository", response.toString())

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {
                emit(ResourceState.Error("Error during logout process"))
            }
        }.catch { e ->
            Log.d("UserRepository", e.localizedMessage)
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
            Log.d("REGISTER", response.body().toString())

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!.data))
            } else {
                emit(ResourceState.Error("Registration failed"))
            }
        }.catch { e ->
            emit(ResourceState.Error(e.localizedMessage ?: "Error during registration"))
        }
    }
}
