package com.dipuguide.finslice.domain.repo

import com.dipuguide.finslice.domain.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserAuthRepository {
    suspend fun signUp(name: String, email: String, password: String): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun saveUserDetail(user: User): Result<Unit>
    fun getUserDetails(): Flow<Result<User>>
    suspend fun editUserDetail(user: User): Result<Unit>
    suspend fun forgetPassword(email: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
    fun checkAuthStatus(): Flow<Boolean>

}