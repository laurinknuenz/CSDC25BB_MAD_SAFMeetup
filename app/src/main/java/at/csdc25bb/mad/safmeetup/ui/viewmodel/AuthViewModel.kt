package at.csdc25bb.mad.safmeetup.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.csdc25bb.mad.safmeetup.data.api.response.LogoutResponse
import at.csdc25bb.mad.safmeetup.data.entity.User
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
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _loginState: MutableStateFlow<ResourceState<User>> =
        MutableStateFlow(ResourceState.Idle())
    private val _logoutState: MutableStateFlow<ResourceState<LogoutResponse>> =
        MutableStateFlow(ResourceState.Idle())
    private val _registerState: MutableStateFlow<ResourceState<User>> =
        MutableStateFlow(ResourceState.Idle())

    val loginState: StateFlow<ResourceState<User>> = _loginState
    val logoutState: StateFlow<ResourceState<LogoutResponse>> = _logoutState
    val registerState: StateFlow<ResourceState<User>> = _registerState


    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loginState.value = ResourceState.Loading() // Set loading state when login starts
            userRepository.login(username, password).collectLatest { loginResponse ->
                Log.d(TAG, loginResponse.toString())
                _logoutState.value = ResourceState.Idle()
                _loginState.value = loginResponse
            }
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            _logoutState.value = ResourceState.Loading()
            userRepository.logout().collectLatest { logoutResponse ->
                _loginState.value = ResourceState.Idle()
                Log.d(TAG, logoutResponse.toString())
                _logoutState.value = logoutResponse
            }
        }
    }

    fun register(
        firstname: String,
        lastname: String,
        username: String,
        email: String,
        password: String,
        inviteCode: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _registerState.value = ResourceState.Loading()
            userRepository.register(firstname, lastname, username, email, password, inviteCode)
                .collectLatest { registerResponse ->
                    Log.d(TAG, registerResponse.toString())
                    _registerState.value = registerResponse
                }
        }
    }

    fun clearRegisterState(){
        _registerState.value = ResourceState.Idle()
    }

    companion object {
        const val TAG = "LoginViewModel"
    }
}
