package com.store.app.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.app.StoreApplication
import com.store.app.data.local.entity.UserEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val userRepository = StoreApplication.getInstance().userRepository

    private val _userState = MutableLiveData<UserState>()
    val userState: LiveData<UserState> = _userState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser().collectLatest { user ->
                _userState.value = if (user != null && user.isLoggedIn) {
                    UserState.LoggedIn(user)
                } else {
                    UserState.NotLoggedIn
                }
            }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = userRepository.loginWithGoogle(idToken)
            _isLoading.value = false

            result.fold(
                onSuccess = { user ->
                    _userState.value = UserState.LoggedIn(user)
                },
                onFailure = { error ->
                    _userState.value = UserState.Error(error.message ?: "Login failed")
                }
            )
        }
    }

    fun loginAsGuest() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = userRepository.loginAsGuest()
            _isLoading.value = false

            result.fold(
                onSuccess = { user ->
                    _userState.value = UserState.LoggedIn(user)
                },
                onFailure = { error ->
                    _userState.value = UserState.Error(error.message ?: "Guest login failed")
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _userState.value = UserState.NotLoggedIn
        }
    }

    sealed class UserState {
        object NotLoggedIn : UserState()
        data class LoggedIn(val user: UserEntity) : UserState()
        data class Error(val message: String) : UserState()
    }
}
