package com.store.app.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.store.app.StoreApplication
import com.store.app.data.local.entity.UserEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {

    private val userRepository = StoreApplication.getInstance().userRepository

    private val _user = MutableLiveData<UserEntity?>()
    val user: LiveData<UserEntity?> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser().collectLatest { userEntity ->
                _user.value = userEntity
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            userRepository.logout()
            _isLoading.value = false
        }
    }
}
