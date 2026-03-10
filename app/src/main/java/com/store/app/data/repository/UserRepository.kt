package com.store.app.data.repository

import com.store.app.data.local.dao.UserDao
import com.store.app.data.local.entity.UserEntity
import com.store.app.data.remote.api.MockApiService
import com.store.app.data.remote.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val userDao: UserDao,
    private val mockApiService: MockApiService
) {

    fun getCurrentUser(): Flow<UserEntity?> {
        return userDao.getCurrentUser()
    }

    suspend fun loginWithGoogle(idToken: String): Result<UserEntity> {
        return try {
            val result = mockApiService.loginWithGoogle(idToken)
            if (result.isSuccess) {
                val userModel = result.getOrNull()!!
                val userEntity = userModel.toEntity(isLoggedIn = true)
                userDao.insertUser(userEntity)
                Result.success(userEntity)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        val currentUser = userDao.getCurrentUserSync()
        currentUser?.let {
            userDao.updateLoginStatus(it.id, false)
        }
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    private fun UserModel.toEntity(isLoggedIn: Boolean = false): UserEntity {
        return UserEntity(
            id = id,
            email = email,
            displayName = displayName,
            photoUrl = photoUrl,
            isLoggedIn = isLoggedIn
        )
    }
}
