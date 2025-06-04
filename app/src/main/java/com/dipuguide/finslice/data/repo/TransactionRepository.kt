package com.dipuguide.finslice.data.repo

import android.util.Log
import com.dipuguide.finslice.data.model.Transaction
import com.dipuguide.finslice.utils.formatPrice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    companion object {
        private const val USERS_COLLECTION = "users"
        private const val TRANSACTIONS_COLLECTION = "transactions"
    }

    suspend fun addTransaction(transaction: Transaction): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val userId = auth.currentUser?.uid
                    ?: return@withContext Result.failure(Exception("User not logged in"))



                val docRef = firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(TRANSACTIONS_COLLECTION)
                    .document()


                val txWithId = transaction.copy(id = docRef.id)

                docRef.set(txWithId).await()

                Result.success(Unit)
            } catch (e: Exception) {
                Log.e("TransactionRepository", "Error adding transaction", e)
                Result.failure(e)
            }
        }

    suspend fun getAllTransactions(): Result<List<Transaction>> =
        withContext(Dispatchers.IO) {
            try {
                val userId = auth.currentUser?.uid
                    ?: return@withContext Result.failure(Exception("User not logged in"))

                val snapshot = firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(TRANSACTIONS_COLLECTION)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .await()

                val transactions = snapshot.toObjects(Transaction::class.java)
                Result.success(transactions)

            } catch (e: Exception) {
                Result.failure(e)
            }
        }


}