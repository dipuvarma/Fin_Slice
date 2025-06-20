package com.dipuguide.finslice.data.repo

import android.util.Log
import com.dipuguide.finslice.data.model.AuthUser
import com.dipuguide.finslice.ui.theme.backgroundDarkMediumContrast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    companion object {
        private const val TAG = "FirebaseAuthRepository"
        private const val USERS_COLLECTION = "users"
    }

    suspend fun signUp(name: String, email: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid
                    ?: throw FirebaseAuthException("USER_NULL", "User ID is null")

                val user = AuthUser(
                    id = userId,
                    name = name,
                    email = email,
                    password = ""
                )

                val userMap = mapOf(
                    "id" to user.id,
                    "name" to user.name,
                    "email" to user.email,
                    "createdAt" to FieldValue.serverTimestamp()
                )

                firestore.collection(USERS_COLLECTION).document(userId).set(userMap).await()

                Log.d(TAG, "User sign-up successful: $userId")
                Result.success(Unit)
            } catch (e: FirebaseAuthException) {
                Log.e(TAG, "FirebaseAuthException during sign-up: ${e.message}", e)
                Result.failure(e)
            } catch (e: Exception) {
                Log.e(TAG, "Unknown exception during sign-up: ${e.message}", e)
                Result.failure(e)
            }
        }


    suspend fun signIn(email: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                Log.d(TAG, "User sign-in successful: ${auth.currentUser?.uid}")
                Result.success(Unit)
            } catch (e: FirebaseAuthException) {
                Log.e(TAG, "FirebaseAuthException during sign-in: ${e.message}", e)
                Result.failure(e)
            } catch (e: Exception) {
                Log.e(TAG, "Unknown exception during sign-in: ${e.message}", e)
                Result.failure(e)
            }
        }

    suspend fun forgetPassword(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: FirebaseAuthException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserWithEmail(email: String): AuthUser? {
        return Firebase.firestore.collection(USERS_COLLECTION)
            .whereEqualTo("email", email)
            .get()
            .await()
            .toObjects(AuthUser::class.java)
            .firstOrNull()
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser


    suspend fun signOut() {
        auth.signOut()
        Log.d(TAG, "User signed out")
    }

    suspend fun deleteAccount() {
        val user = auth.currentUser
        if (user != null) {
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User account deleted.")
                    } else {
                        Log.e(TAG, "Account deletion failed", task.exception)
                    }
                }
        } else {
            Log.w(TAG, "No user is currently signed in.")
        }
    }


}