package com.dipuguide.finslice.data.repo

import android.util.Log
import com.dipuguide.finslice.data.model.IncomeTransaction
import com.dipuguide.finslice.presentation.mapper.toIncomeTransaction
import com.dipuguide.finslice.presentation.mapper.toIncomeTransactionUi
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionUi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IncomeTransactionRepoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : IncomeTransactionRepo {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val TRANSACTIONS_COLLECTION = "transactions"
        private const val TAG = "IncomeTransactionRepo"
    }

    override suspend fun addIncomeTransaction(incomeTransactionUi: IncomeTransactionUi): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val user = auth.currentUser
                if (user == null) {
                    Log.e(TAG, "User is null")
                    return@withContext Result.failure(Exception("User not logged in"))
                }

                val userId = user.uid

                val docRef = firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(TRANSACTIONS_COLLECTION)
                    .document() //generate a random transaction id

                val incomeTransaction = incomeTransactionUi.toIncomeTransaction()

                val transaction = IncomeTransaction(
                    id = docRef.id,
                    amount = incomeTransaction.amount,
                    note = incomeTransaction.note,
                    category = incomeTransaction.category,
                )

                val transactionMap = mapOf(
                    "id" to transaction.id,
                    "amount" to transaction.amount,
                    "note" to transaction.note,
                    "category" to transaction.category,
                    "createdAt" to System.currentTimeMillis()
                )

                Log.d(TAG, "Prepared transaction map: $transactionMap")
                Log.d(TAG, "Document path: ${docRef.path}")

                docRef.set(transactionMap).await()
                Log.d(TAG, "Transaction saved successfully")
                Result.success(Unit)

            } catch (e: Exception) {
                Log.e(TAG, "Failed to add transaction: ${e.message}", e)
                Result.failure(e)
            }
        }

    override suspend fun getIncomeTransaction(): Flow<Result<List<IncomeTransactionUi>>> =
        callbackFlow {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                trySend(Result.failure(Exception("User not logged in")))
                close()
                return@callbackFlow
            }

            val listener = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(TRANSACTIONS_COLLECTION)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Result.failure(error))
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val transactions = snapshot.documents.mapNotNull { doc ->
                            val id = doc.getString("id") ?: doc.id
                            val amount = doc.getDouble("amount") ?: 0.0
                            val note = doc.getString("note") ?: ""
                            val category = doc.getString("category") ?: ""
                            val createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()

                            if (category.isNotBlank()) {
                                IncomeTransaction(id, amount, note, category, createdAt)
                                    .toIncomeTransactionUi()
                            } else null
                        }

                        trySend(Result.success(transactions))
                    } else {
                        trySend(Result.success(emptyList()))
                    }
                }

            awaitClose { listener.remove() } // Close Firestore listener when flow is cancelled
        }.flowOn(Dispatchers.IO)


    override suspend fun editIncomeTransaction(incomeTransactionUi: IncomeTransactionUi): Result<Unit> =
        withContext(
            Dispatchers.IO
        ) {
            try {
                val userId = auth.currentUser?.uid
                    ?: return@withContext Result.failure(Exception("User not logged in"))

                val transactionId = incomeTransactionUi.id
                    ?: return@withContext Result.failure(Exception("Transaction ID is null"))

                val transactionRef = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(TRANSACTIONS_COLLECTION)
                    .document(transactionId)

                val incomeTransaction = incomeTransactionUi.toIncomeTransaction()

                val updateData = mapOf(
                    "amount" to incomeTransaction.amount,
                    "note" to incomeTransaction.note,
                    "category" to incomeTransaction.category,
                    "createdAt" to incomeTransaction.createdAt
                )

                transactionRef.update(updateData).await()

                Result.success(Unit)
            } catch (e: Exception) {
                Log.e("IncomeTransactionRepo", "Failed to edit transaction", e)
                Result.failure(e)
            }
        }

    override suspend fun deleteIncomeTransaction(id: String): Result<Unit> =
        withContext(
            Dispatchers.IO
        ) {
            try {

                val userId = auth.currentUser?.uid
                    ?: return@withContext Result.failure(Exception("User id is not found"))

                val transactionId = id

                firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(TRANSACTIONS_COLLECTION)
                    .document(transactionId)
                    .delete()
                    .await()

                Result.success(Unit)
            } catch (e: Exception) {
                Log.e("IncomeTransactionRepo", "Failed to delete transaction", e)
                Result.failure(e)
            }
        }
}
