package at.csdc25bb.mad.safmeetup.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import at.csdc25bb.mad.safmeetup.data.entity.User
import androidx.lifecycle.viewModelScope
import at.csdc25bb.mad.safmeetup.data.repository.UserRepository
import at.csdc25bb.mad.safmeetup.data.utils.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _user: MutableStateFlow<ResourceState<User>> =
        MutableStateFlow(ResourceState.Loading())

    val user: StateFlow<ResourceState<User>> = _user

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUser().collectLatest { userResponse ->
                _user.value = userResponse
            }
        }
    }

    fun updateUser(
        userId: String,
        firstname: String,
        lastname: String,
        username: String,
        password: String,
        email: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Updating with email: $email")
            userRepository.updateUser(
                userId,
                firstname,
                lastname,
                username,
                password,
                email
            )
                .collectLatest { userResponse ->
                    Log.d(TAG, "Updated the user ${username}: ${userResponse}")
                    getCurrentUser()
                }
        }
    }

    init {
        Log.d(TAG, "In the init function of the ${TAG}")
        getCurrentUser()
    }

    companion object {
        const val TAG = "UserViewModel"
    }
}