package com.dipuguide.finslice.data.repo

import android.nfc.Tag
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.dipuguide.finslice.data.model.ExpenseTransaction
import com.dipuguide.finslice.presentation.mapper.toExpenseTransaction
import com.dipuguide.finslice.presentation.mapper.toExpenseTransactionUi
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionUi
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionUi
import com.dipuguide.finslice.utils.DateFilterType
import com.dipuguide.finslice.utils.getDateRangeMillis
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.System
import javax.inject.Inject

class ExpenseTransactionRepoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ExpenseTransactionRepo {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val EXPENSE_COLLECTION = "expense"
        private const val TAG = "ExpenseTransactionRepo"
    }

    override suspend fun addExpenseTransaction(expenseTransactionUi: ExpenseTransactionUi): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {

                val userId = auth.currentUser?.uid
                    ?: return@withContext Result.failure(Exception("User ID is not found"))

                val docRef = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(EXPENSE_COLLECTION)
                    .document() //generate a random transaction id

                val transaction = expenseTransactionUi.toExpenseTransaction()

                val expenseTransaction = ExpenseTransaction(
                    id = docRef.id,
                    amount = transaction.amount,
                    note = transaction.note,
                    category = transaction.category,
                    tag = transaction.tag,
                    date = System.currentTimeMillis()
                )

                docRef.set(expenseTransaction).await()

                Result.success(Unit)
            } catch (e: Exception) {
                Log.e(TAG, "addExpenseTransaction: ", e)
                Result.failure(e)
            }
        }

    override suspend fun getExpenseTransaction(): Flow<Result<List<ExpenseTransactionUi>>> =
        callbackFlow {
            try {
                val userId = auth.currentUser?.uid
                if (userId == null) {
                    Log.w("ExpenseRepo", "User not logged in")
                    trySend(Result.failure(Exception("User not logged in")))
                    close()
                    return@callbackFlow
                }

                val listener = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(EXPENSE_COLLECTION)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Log.e(
                                "ExpenseRepo",
                                "Firestore error: ${error.localizedMessage}",
                                error
                            )
                            trySend(Result.failure(error))
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            Log.d("ExpenseRepo", "Snapshot received. Size: ${snapshot.size()}")

                            if (!snapshot.isEmpty) {
                                val transactions = snapshot.documents.mapNotNull { doc ->
                                    val id = doc.getString("id") ?: doc.id
                                    val amount = doc.getDouble("amount") ?: 0.0
                                    val note = doc.getString("note") ?: ""
                                    val category = doc.getString("category") ?: ""
                                    val tag = doc.getString("tag") ?: ""
                                    val date = doc.getLong("date") ?: System.currentTimeMillis()

                                    if (category.isNotBlank()) {
                                        ExpenseTransaction(
                                            id = id,
                                            amount = amount,
                                            note = note,
                                            category = category,
                                            tag = tag,
                                            date = date
                                        ).toExpenseTransactionUi()
                                    } else {
                                        null
                                    }
                                }

                                trySend(Result.success(transactions))
                            } else {
                                Log.d("ExpenseRepo", "No expenses found")
                                trySend(Result.success(emptyList()))
                            }
                        } else {
                            Log.w("ExpenseRepo", "Snapshot is null")
                            trySend(Result.success(emptyList()))
                        }
                    }

                awaitClose {
                    Log.d("ExpenseRepo", "Listener removed")
                    listener.remove()
                }
            } catch (e: Exception) {
                Log.e("ExpenseRepo", "Exception in callbackFlow", e)
                trySend(Result.failure(e))
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getAllExpensesByCategory(category: String): Flow<Result<List<ExpenseTransactionUi>>> =
        callbackFlow {
            try {
                val userId = auth.currentUser?.uid
                if (userId == null) {
                    Log.w("ExpenseRepo", "User not logged in")
                    trySend(Result.failure(Exception("User not logged in")))
                    close()
                    return@callbackFlow
                }

                val listener = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(EXPENSE_COLLECTION)
                    .whereEqualTo("category", category)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Log.e(
                                "ExpenseRepo",
                                "Firestore error: ${error.localizedMessage}",
                                error
                            )
                            trySend(Result.failure(error))
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            Log.d(
                                "ExpenseRepo",
                                "Snapshot for category '$category' received. Size: ${snapshot.size()}"
                            )

                            val transactions = snapshot.documents.mapNotNull { doc ->
                                val id = doc.getString("id") ?: doc.id
                                val amount = doc.getDouble("amount") ?: 0.0
                                val note = doc.getString("note") ?: ""
                                val tag = doc.getString("tag") ?: ""
                                val date = doc.getLong("date") ?: System.currentTimeMillis()

                                ExpenseTransaction(
                                    id = id,
                                    amount = amount,
                                    note = note,
                                    category = category, // Use input value
                                    tag = tag,
                                    date = date
                                ).toExpenseTransactionUi()
                            }

                            trySend(Result.success(transactions))
                        } else {
                            Log.w("ExpenseRepo", "Snapshot is null for category: $category")
                            trySend(Result.success(emptyList()))
                        }
                    }

                awaitClose {
                    Log.d("ExpenseRepo", "Listener removed for category: $category")
                    listener.remove()
                }
            } catch (e: Exception) {
                Log.e("ExpenseRepo", "Exception in getAllExpensesByCategory", e)
                trySend(Result.failure(e))
            }
        }.flowOn(Dispatchers.IO)


    override suspend fun editExpenseTransaction(expenseTransactionUi: ExpenseTransactionUi): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                Log.w("ExpenseRepo", "User not logged in - cannot edit")
                return Result.failure(Exception("User not logged in"))
            }

            val expenseRef = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(EXPENSE_COLLECTION)
                .document(expenseTransactionUi.id!!)

            val updatedData = mapOf(
                "id" to expenseTransactionUi.id,
                "amount" to expenseTransactionUi.amount,
                "note" to expenseTransactionUi.note,
                "category" to expenseTransactionUi.category,
                "tag" to expenseTransactionUi.tag,
                "date" to expenseTransactionUi.date
            )

            expenseRef.set(updatedData, SetOptions.merge()).await()

            Log.d("ExpenseRepo", "Transaction edited successfully: ${expenseTransactionUi.id}")
            Result.success(Unit)

        } catch (e: Exception) {
            Log.e("ExpenseRepo", "Failed to edit transaction", e)
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAllExpensesByDateRange(filter: DateFilterType): Flow<Result<List<ExpenseTransactionUi>>> =
        callbackFlow {
            try {
                val userId = auth.currentUser?.uid
                if (userId == null) {
                    trySend(Result.failure(Exception("User not logged in")))
                    close()
                    return@callbackFlow
                }

                val (startDate, endDate) = getDateRangeMillis(filter)

                val listener = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(EXPENSE_COLLECTION)
                    .whereGreaterThanOrEqualTo("date", startDate)
                    .whereLessThanOrEqualTo("date", endDate)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            trySend(Result.failure(error))
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            val transactions = snapshot.documents.mapNotNull { doc ->
                                val id = doc.getString("id") ?: doc.id
                                val amount = doc.getDouble("amount") ?: 0.0
                                val note = doc.getString("note") ?: ""
                                val tag = doc.getString("tag") ?: ""
                                val category = doc.getString("category") ?: ""
                                val date = doc.getLong("date") ?: System.currentTimeMillis()

                                ExpenseTransaction(
                                    id = id,
                                    amount = amount,
                                    note = note,
                                    category = category,
                                    tag = tag,
                                    date = date
                                ).toExpenseTransactionUi()
                            }
                            trySend(Result.success(transactions))
                        } else {
                            trySend(Result.success(emptyList()))
                        }
                    }

                awaitClose { listener.remove() }

            } catch (e: Exception) {
                trySend(Result.failure(e))
            }
        }.flowOn(Dispatchers.IO)



    override suspend fun deleteExpenseTransaction(id: String): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
            if (userId == null) {
                Log.w("ExpenseRepo", "User not logged in - cannot delete")
                return Result.failure(Exception("User not logged in"))
            }

            val expenseRef = firestore.collection(USERS_COLLECTION)
                .document(userId)
                .collection(EXPENSE_COLLECTION)
                .document(id)

            expenseRef.delete().await()

            Log.d("ExpenseRepo", "Transaction deleted successfully: $id")
            Result.success(Unit)

        } catch (e: Exception) {
            Log.e("ExpenseRepo", "Failed to delete transaction: $id", e)
            Result.failure(e)
        }
    }

}