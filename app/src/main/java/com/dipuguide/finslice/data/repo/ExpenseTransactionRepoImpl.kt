package com.dipuguide.finslice.data.repo

import com.dipuguide.finslice.domain.model.ExpenseTransaction
import com.dipuguide.finslice.domain.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.utils.DateFilterType
import com.dipuguide.finslice.utils.getDateRangeMillis
import com.dipuguide.finslice.utils.getMillisRangeForMonth
import com.google.firebase.auth.FirebaseAuth
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

class ExpenseTransactionRepoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ExpenseTransactionRepo {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val EXPENSES_COLLECTION = "expenses"
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    }

    override suspend fun addExpenseTransaction(expenseTransaction: ExpenseTransaction): Result<Unit> =
        withContext(ioDispatcher) {
            try {
                Timber.d("ExpenseTransactionRepo: Attempting to add expense transaction for user")
                val userId = auth.currentUser?.uid ?: run {
                    Timber.e("ExpenseTransactionRepo: User not logged in while adding transaction")
                    return@withContext Result.failure(Exception("User ID is not found"))
                }

                val docRef = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(EXPENSES_COLLECTION)
                    .document()

                val transaction = expenseTransaction.copy(
                    id = docRef.id,
                )
                Timber.d("ExpenseTransactionRepo: Adding expense transaction with ID ${transaction.id}, amount: ${transaction.amount}, category: ${transaction.category}")
                docRef.set(transaction).await()
                Timber.i("ExpenseTransactionRepo: Successfully added expense transaction ${transaction.id}")
                Result.success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "ExpenseTransactionRepo: Failed to add expense transaction")
                Result.failure(e)
            }
        }

    override fun getExpenseTransaction(): Flow<Result<List<ExpenseTransaction>>> =
        callbackFlow {
            Timber.d("ExpenseTransactionRepo: Setting up listener for all expense transactions")

            val userId = auth.currentUser?.uid ?: run {
                Timber.e("ExpenseTransactionRepo: User not logged in while fetching transactions")
                trySend(Result.failure(Exception("User not logged in")))
                close()
                return@callbackFlow
            }
            try {
                val listener = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(EXPENSES_COLLECTION)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Timber.e(error, "ExpenseTransactionRepo: Failed to fetch transactions for user $userId")
                            trySend(Result.failure(error))
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val transactions = snapshot.documents.mapNotNull { doc ->
                                try {
                                    doc.toObject(ExpenseTransaction::class.java).also {
                                        Timber.d("ExpenseTransactionRepo: Parsed transaction ${doc.id}")
                                    }
                                } catch (e: Exception) {
                                    Timber.w(e, "ExpenseTransactionRepo: Failed to parse transaction document ${doc.id}")
                                    null
                                }
                            }
                            Timber.i("ExpenseTransactionRepo: Successfully loaded ${transactions.size} expense transactions for user $userId")
                            trySend(Result.success(transactions))
                        } else {
                            Timber.w("ExpenseTransactionRepo: Received null snapshot for transactions")
                            trySend(Result.success(emptyList()))
                        }
                    }
                awaitClose {
                    Timber.d("ExpenseTransactionRepo: Closing listener for expense transactions")
                    listener.remove()
                    close()
                }
            } catch (e: Exception) {
                Timber.e(e, "ExpenseTransactionRepo: Failed to set up expense transactions listener for user $userId")
                trySend(Result.failure(e))
                close(e)
            }
        }

    override fun getAllExpensesByCategory(category: String): Flow<Result<List<ExpenseTransaction>>> =
        callbackFlow {
            Timber.d("ExpenseTransactionRepo: Setting up listener for transactions with category $category")
            val userId = auth.currentUser?.uid ?: run {
                Timber.e("ExpenseTransactionRepo: User not logged in while fetching transactions by category $category")
                trySend(Result.failure(Exception("User not logged in")))
                close()
                return@callbackFlow
            }
            try {
                val listener = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(EXPENSES_COLLECTION)
                    .whereEqualTo("category", category)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Timber.e(error, "ExpenseTransactionRepo: Failed to fetch transactions for user $userId with category $category")
                            trySend(Result.failure(error))
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val transactions = snapshot.documents.mapNotNull { doc ->
                                try {
                                    doc.toObject(ExpenseTransaction::class.java).also {
                                        Timber.d("ExpenseTransactionRepo: Parsed transaction ${doc.id} for category $category")
                                    }
                                } catch (e: Exception) {
                                    Timber.w(e, "ExpenseTransactionRepo: Failed to parse transaction document ${doc.id} for category $category")
                                    null
                                }
                            }
                            Timber.i("ExpenseTransactionRepo: Successfully loaded ${transactions.size} transactions for category $category")
                            trySend(Result.success(transactions))
                        } else {
                            Timber.w("ExpenseTransactionRepo: Received null snapshot for transactions with category $category")
                            trySend(Result.success(emptyList()))
                        }
                    }

                awaitClose {
                    Timber.d("ExpenseTransactionRepo: Closing listener for transactions with category $category")
                    listener.remove()
                    close()
                }
            } catch (e: Exception) {
                Timber.e(e, "ExpenseTransactionRepo: Failed to set up transactions listener for user $userId with category $category")
                trySend(Result.failure(e))
                close(e)
            }
        }

    override suspend fun editExpenseTransaction(expenseTransaction: ExpenseTransaction): Result<Unit> =
        withContext(ioDispatcher) {
            try {
                Timber.d("ExpenseTransactionRepo: Attempting to edit transaction ${expenseTransaction.id}")
                val userId = auth.currentUser?.uid ?: run {
                    Timber.e("ExpenseTransactionRepo: User not logged in while editing transaction ${expenseTransaction.id}")
                    return@withContext Result.failure(Exception("User not logged in"))
                }

                val expenseRef = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(EXPENSES_COLLECTION)
                    .document(expenseTransaction.id)

                val updatedData = mapOf(
                    "amount" to expenseTransaction.amount,
                    "note" to expenseTransaction.note,
                    "category" to expenseTransaction.category,
                    "tag" to expenseTransaction.tag,
                    "createdAt" to expenseTransaction.createdAt
                )
                Timber.d("ExpenseTransactionRepo: Updating transaction ${expenseTransaction.id} with amount: ${expenseTransaction.amount}, category: ${expenseTransaction.category}")
                expenseRef.set(updatedData).await()
                Timber.i("ExpenseTransactionRepo: Successfully updated transaction ${expenseTransaction.id}")
                Result.success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "ExpenseTransactionRepo: Failed to update transaction ${expenseTransaction.id}")
                Result.failure(e)
            }
        }

    override fun getAllExpensesByMonth(
        month: Int,
        year: Int,
    ): Flow<Result<List<ExpenseTransaction>>> =
        callbackFlow {
            Timber.d("ExpenseTransactionRepo: Setting up listener for transactions in $month/$year")
            try {
                val userId = auth.currentUser?.uid ?: run {
                    Timber.e("ExpenseTransactionRepo: User not logged in while fetching transactions for $month/$year")
                    trySend(Result.failure(Exception("User not logged in")))
                    close()
                    return@callbackFlow
                }

                val (startDate, endDate) = getMillisRangeForMonth(month, year)
                Timber.d("ExpenseTransactionRepo: Querying transactions for $month/$year from $startDate to $endDate")

                val listener = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(EXPENSES_COLLECTION)
                    .whereGreaterThanOrEqualTo("createdAt", startDate)
                    .whereLessThanOrEqualTo("createdAt", endDate)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Timber.e(error, "ExpenseTransactionRepo: Failed to fetch transactions for user $userId in $month/$year")
                            trySend(Result.failure(error))
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val transactions = snapshot.documents.mapNotNull { doc ->
                                try {
                                    doc.toObject(ExpenseTransaction::class.java).also {
                                        Timber.d("ExpenseTransactionRepo: Parsed transaction ${doc.id} for $month/$year")
                                    }
                                } catch (e: Exception) {
                                    Timber.w(e, "ExpenseTransactionRepo: Failed to parse transaction document ${doc.id} for $month/$year")
                                    null
                                }
                            }
                            Timber.i("ExpenseTransactionRepo: Successfully loaded ${transactions.size} transactions for $month/$year")
                            trySend(Result.success(transactions))
                        } else {
                            Timber.w("ExpenseTransactionRepo: Received null snapshot for transactions in $month/$year")
                            trySend(Result.success(emptyList()))
                        }
                    }

                awaitClose {
                    Timber.d("ExpenseTransactionRepo: Closing listener for transactions in $month/$year")
                    listener.remove()
                    close()
                }
            } catch (e: Exception) {
                Timber.e(e, "ExpenseTransactionRepo: Failed to set up transactions listener for user in $month/$year")
                trySend(Result.failure(e))
                close(e)
            }
        }

    override fun getAllExpensesByDateRange(filter: DateFilterType): Flow<Result<List<ExpenseTransaction>>> =
        callbackFlow {
            Timber.d("ExpenseTransactionRepo: Setting up listener for transactions with filter $filter")
            try {
                val userId = auth.currentUser?.uid ?: run {
                    Timber.e("ExpenseTransactionRepo: User not logged in while fetching transactions by date range $filter")
                    trySend(Result.failure(Exception("User not logged in")))
                    close()
                    return@callbackFlow
                }

                val (startDate, endDate) = getDateRangeMillis(filter)
                Timber.d("ExpenseTransactionRepo: Querying transactions from $startDate to $endDate")

                val listener = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(EXPENSES_COLLECTION)
                    .whereGreaterThanOrEqualTo("createdAt", startDate)
                    .whereLessThanOrEqualTo("createdAt", endDate)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Timber.e(error, "ExpenseTransactionRepo: Failed to fetch transactions for user $userId with filter $filter")
                            trySend(Result.failure(error))
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val transactions = snapshot.documents.mapNotNull { doc ->
                                try {
                                    doc.toObject(ExpenseTransaction::class.java).also {
                                        Timber.d("ExpenseTransactionRepo: Parsed transaction ${doc.id} for filter $filter")
                                    }
                                } catch (e: Exception) {
                                    Timber.w(e, "ExpenseTransactionRepo: Failed to parse transaction document ${doc.id} for filter $filter")
                                    null
                                }
                            }
                            Timber.i("ExpenseTransactionRepo: Successfully loaded ${transactions.size} transactions for filter $filter")
                            trySend(Result.success(transactions))
                        } else {
                            Timber.w("ExpenseTransactionRepo: Received null snapshot for transactions with filter $filter")
                            trySend(Result.success(emptyList()))
                        }
                    }

                awaitClose {
                    Timber.d("ExpenseTransactionRepo: Closing listener for transactions with filter $filter")
                    listener.remove()
                    close()
                }
            } catch (e: Exception) {
                Timber.e(e, "ExpenseTransactionRepo: Failed to set up transactions listener for user with filter $filter")
                trySend(Result.failure(e))
                close(e)
            }
        }

    override suspend fun deleteExpenseTransaction(expenseId: String): Result<Unit> =
        withContext(ioDispatcher) {
            try {
                Timber.d("ExpenseTransactionRepo: Attempting to delete transaction $expenseId")
                val userId = auth.currentUser?.uid ?: run {
                    Timber.e("ExpenseTransactionRepo: User not logged in while deleting transaction $expenseId")
                    return@withContext Result.failure(Exception("User not logged in"))
                }

                val expenseRef = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(EXPENSES_COLLECTION)
                    .document(expenseId)

                expenseRef.delete().await()
                Timber.i("ExpenseTransactionRepo: Successfully deleted transaction $expenseId")
                Result.success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "ExpenseTransactionRepo: Failed to delete transaction $expenseId")
                Result.failure(e)
            }
        }
}