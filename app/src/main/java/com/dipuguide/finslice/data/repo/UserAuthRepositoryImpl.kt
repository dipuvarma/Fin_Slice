package com.dipuguide.finslice.data.repo

import com.dipuguide.finslice.domain.model.User
import com.dipuguide.finslice.domain.repo.UserAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UserAuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : UserAuthRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
        private val ioDispatchers: CoroutineDispatcher = Dispatchers.IO
    }

    override suspend fun signUp(name: String, email: String, password: String): Result<Unit> =
        withContext(ioDispatchers) {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                Timber.d("User sign-up successful: ${result.user?.uid}")
                Result.success(Unit)
            } catch (e: FirebaseAuthException) {
                Timber.e(e, "FirebaseAuthException during sign-up: ${e.message}")
                Result.failure(e)
            } catch (e: Exception) {
                Timber.e(e, "Unknown exception during sign-up: ${e.message}")
                Result.failure(e)
            }
        }


    override suspend fun signIn(email: String, password: String): Result<Unit> =
        withContext(ioDispatchers) {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                Timber.d("User sign-in successful: ${auth.currentUser?.uid}")
                Result.success(Unit)
            } catch (e: FirebaseAuthException) {
                Timber.e(e, "FirebaseAuthException during sign-in: ${e.message}")
                Result.failure(e)
            } catch (e: Exception) {
                Timber.e(e, "Unknown exception during sign-in: ${e.message}")
                Result.failure(e)
            }
        }

    override suspend fun saveUserDetail(user: User): Result<Unit> = withContext(ioDispatchers) {
        val userId = auth.currentUser?.uid
            ?: throw FirebaseAuthException("USER_NULL", "User ID is null")

        val user = User(
            id = userId,
            name = user.name,
            email = user.email,
            photoUri = user.photoUri,
        )
        try {
            firestore.collection(USERS_COLLECTION).document(userId).set(user).await()
            Timber.d("User save details successful: $user")
            Result.success(Unit)
        } catch (e: FirebaseAuthException) {
            Timber.e(e, "FirebaseAuthException during save details: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Timber.e(e, "Unknown exception during save details: ${e.message}")
            Result.failure(e)
        }
    }

    override fun getUserDetails(): Flow<Result<User>> = callbackFlow {
        val userId = auth.currentUser?.uid ?: run {
            trySend(Result.failure(FirebaseAuthException("USER_NULL", "User ID is null")))
            close()
            return@callbackFlow
        }

        val listenerRegistration = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(User::class.java)
                    if (user != null) {
                        trySend(Result.success(user))
                    } else {
                        trySend(Result.failure(IllegalStateException("User object is null")))
                    }
                } else {
                    trySend(Result.failure(NoSuchElementException("User document not found")))
                }
            }

        awaitClose {
            listenerRegistration.remove()
            close()
        }
    }

    override suspend fun editUserDetail(user: User): Result<Unit> {
        val userId = auth.currentUser?.uid
            ?: throw FirebaseAuthException("USER_NULL", "User ID is null")

        return try {
            val firestoreColl = firestore.collection(USERS_COLLECTION)
                .document(userId)

            val updatedUser = mapOf(
                "name" to user.name,
                "email" to user.photoUri,
                "photoUri" to user.photoUri,
                "createdAt" to user.createdAt
            )
            firestoreColl.update(updatedUser).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun forgetPassword(email: String): Result<Unit> =
        withContext(ioDispatchers) {
            try {
                auth.sendPasswordResetEmail(email).await()
                Result.success(Unit)
            } catch (e: FirebaseAuthException) {
                Timber.e(e, "FirebaseAuthException during forgetPassword: ${e.message}")
                Result.failure(e)
            } catch (e: Exception) {
                Timber.e(e, "Unknown exception during forgetPassword: ${e.message}")
                Result.failure(e)
            }
        }


    override suspend fun signOut(): Result<Unit> =
        withContext(ioDispatchers) {
            try {
                auth.signOut()
                Timber.d("User signed out Successfully")
                Result.success(Unit)
            } catch (e: FirebaseAuthException) {
                Timber.e(e, "FirebaseAuthException during signOut: ${e.message}")
                Result.failure(e)
            } catch (e: Exception) {
                Timber.e(e, "Unknown exception during signOut: ${e.message}")
                Result.failure(e)
            }
        }

    override suspend fun deleteAccount(): Result<Unit> =
        withContext(ioDispatchers) {
            try {
                val currentUser =
                    auth.currentUser ?: throw FirebaseAuthException(
                        "CURRENT_USER",
                        "Current User is null"
                    )
                currentUser.delete().await()
                Result.success(Unit)
            } catch (e: FirebaseAuthException) {
                Timber.e(e, "FirebaseAuthException during delete: ${e.message}")
                Result.failure(e)
            } catch (e: Exception) {
                Timber.e(e, "Unknown exception during delete: ${e.message}")
                Result.failure(e)
            }
        }

    override fun checkAuthStatus(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser != null).isSuccess
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose { auth.removeAuthStateListener(authStateListener) }
    }

}