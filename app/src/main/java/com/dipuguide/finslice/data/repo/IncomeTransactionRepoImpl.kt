package com.dipuguide.finslice.data.repo

import com.dipuguide.finslice.domain.model.IncomeTransaction
import com.dipuguide.finslice.domain.repo.IncomeTransactionRepo
import com.dipuguide.finslice.utils.DateFilterType
import com.dipuguide.finslice.utils.getDateRangeMillis
import com.dipuguide.finslice.utils.getMillisRangeForMonth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class IncomeTransactionRepoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : IncomeTransactionRepo {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val INCOMES_COLLECTION = "incomes"
        private val ioDispatchers: CoroutineDispatcher = Dispatchers.IO
    }

    override suspend fun addIncomeTransaction(incomeTransaction: IncomeTransaction): Result<Unit> =
        withContext(ioDispatchers) {
            try {
                Timber.d("IncomeTransactionRepo: Attempting to add income transaction for user")
                val userId = auth.currentUser?.uid
                    ?: return@withContext Result.failure(Exception("User not logged in").also {
                        Timber.e(it, "IncomeTransactionRepo: User not logged in")
                    })

                val docRef = firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(INCOMES_COLLECTION)
                    .document()

                val transaction = incomeTransaction.copy(
                    id = docRef.id,
                )
                Timber.d("IncomeTransactionRepo: Adding income transaction with ID ${transaction.id}, amount: ${transaction.amount}, category: ${transaction.category}")
                docRef.set(transaction).await()
                Timber.i("IncomeTransactionRepo: Successfully added income transaction ${transaction.id}")
                Result.success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "IncomeTransactionRepo: Failed to add income transaction")
                Result.failure(e)
            }
        }

    override suspend fun getIncomeTransaction(): Flow<Result<List<IncomeTransaction>>> =
        callbackFlow {
            Timber.d("IncomeTransactionRepo: Setting up listener for all income transactions")
            val userId = auth.currentUser?.uid ?: run {
                Timber.e("IncomeTransactionRepo: User not logged in while fetching transactions")
                trySend(Result.failure(Exception("User not logged in")))
                close()
                return@callbackFlow
            }
            try {
                val listener = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(INCOMES_COLLECTION)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Timber.e(error, "IncomeTransactionRepo: Failed to fetch transactions for user $userId")
                            trySend(Result.failure(error))
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val transactions = snapshot.documents.mapNotNull { doc ->
                                try {
                                    doc.toObject(IncomeTransaction::class.java).also {
                                        Timber.d("IncomeTransactionRepo: Parsed transaction ${doc.id}")
                                    }
                                } catch (e: Exception) {
                                    Timber.w(e, "IncomeTransactionRepo: Failed to parse transaction document ${doc.id}")
                                    null
                                }
                            }
                            Timber.i("IncomeTransactionRepo: Successfully loaded ${transactions.size} income transactions for user $userId")
                            trySend(Result.success(transactions))
                        } else {
                            Timber.w("IncomeTransactionRepo: Received null snapshot for transactions")
                            trySend(Result.success(emptyList()))
                        }
                    }
                awaitClose {
                    Timber.d("IncomeTransactionRepo: Closing listener for income transactions")
                    listener.remove()
                    close()
                }
            } catch (e: Exception) {
                Timber.e(e, "IncomeTransactionRepo: Failed to set up income transactions listener for user $userId")
                trySend(Result.failure(e))
                close(e)
            }
        }

    override suspend fun getIncomeTransactionByDate(filter: DateFilterType): Flow<Result<List<IncomeTransaction>>> =
        callbackFlow {
            Timber.d("IncomeTransactionRepo: Setting up listener for transactions with filter $filter")
            val userId = auth.currentUser?.uid ?: run {
                Timber.e("IncomeTransactionRepo: User not logged in while fetching transactions by date")
                trySend(Result.failure(Exception("User not logged in")))
                close()
                return@callbackFlow
            }

            try {
                val (startDate, endDate) = getDateRangeMillis(filter)
                Timber.d("IncomeTransactionRepo: Querying transactions from $startDate to $endDate")

                val listener = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(INCOMES_COLLECTION)
                    .whereGreaterThanOrEqualTo("createdAt", startDate)
                    .whereLessThanOrEqualTo("createdAt", endDate)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Timber.e(error, "IncomeTransactionRepo: Failed to fetch transactions for user $userId with filter $filter")
                            trySend(Result.failure(error))
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val transactions = snapshot.documents.mapNotNull { doc ->
                                doc.toObject(IncomeTransaction::class.java).also {
                                    Timber.d("IncomeTransactionRepo: Parsed transaction ${doc.id} for filter $filter")
                                }
                            }
                            Timber.i("IncomeTransactionRepo: Successfully loaded ${transactions.size} transactions for filter $filter")
                            trySend(Result.success(transactions))
                        } else {
                            Timber.w("IncomeTransactionRepo: Received null snapshot for transactions with filter $filter")
                            trySend(Result.success(emptyList()))
                        }
                    }

                awaitClose {
                    Timber.d("IncomeTransactionRepo: Closing listener for transactions with filter $filter")
                    listener.remove()
                    close()
                }
            } catch (e: Exception) {
                Timber.e(e, "IncomeTransactionRepo: Failed to set up transactions listener for user $userId with filter $filter")
                trySend(Result.failure(e))
                close(e)
            }
        }

    override suspend fun getAllIncomeByMonth(
        month: Int,
        year: Int,
    ): Flow<Result<List<IncomeTransaction>>> =
        callbackFlow {
            Timber.d("IncomeTransactionRepo: Setting up listener for transactions in $month/$year")
            val userId = auth.currentUser?.uid ?: run {
                Timber.e("IncomeTransactionRepo: User not logged in while fetching transactions for $month/$year")
                trySend(Result.failure(Exception("User not logged in")))
                close()
                return@callbackFlow
            }

            try {
                val (startDate, endDate) = getMillisRangeForMonth(month, year)
                Timber.d("IncomeTransactionRepo: Querying transactions for $month/$year from $startDate to $endDate")

                val listener = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(INCOMES_COLLECTION)
                    .whereGreaterThanOrEqualTo("createdAt", startDate)
                    .whereLessThanOrEqualTo("createdAt", endDate)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Timber.e(error, "IncomeTransactionRepo: Failed to fetch transactions for user $userId in $month/$year")
                            trySend(Result.failure(error))
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val transactions = snapshot.documents.mapNotNull { doc ->
                                doc.toObject(IncomeTransaction::class.java).also {
                                    Timber.d("IncomeTransactionRepo: Parsed transaction ${doc.id} for $month/$year")
                                }
                            }
                            Timber.i("IncomeTransactionRepo: Successfully loaded ${transactions.size} transactions for $month/$year")
                            trySend(Result.success(transactions))
                        } else {
                            Timber.w("IncomeTransactionRepo: Received null snapshot for transactions in $month/$year")
                            trySend(Result.success(emptyList()))
                        }
                    }

                awaitClose {
                    Timber.d("IncomeTransactionRepo: Closing listener for transactions in $month/$year")
                    listener.remove()
                    close()
                }
            } catch (e: Exception) {
                Timber.e(e, "IncomeTransactionRepo: Failed to set up transactions listener for user $userId in $month/$year")
                trySend(Result.failure(e))
                close(e)
            }
        }

    override suspend fun editIncomeTransaction(incomeTransaction: IncomeTransaction): Result<Unit> =
        withContext(ioDispatchers) {
            try {
                Timber.d("IncomeTransactionRepo: Attempting to edit transaction ${incomeTransaction.id}")
                val userId = auth.currentUser?.uid
                    ?: return@withContext Result.failure(Exception("User not logged in").also {
                        Timber.e(it, "IncomeTransactionRepo: User not logged in while editing transaction ${incomeTransaction.id}")
                    })

                val incomeTransactionId = incomeTransaction.id
                Timber.d("IncomeTransactionRepo: Updating transaction $incomeTransactionId with amount: ${incomeTransaction.amount}, category: ${incomeTransaction.category}")

                val transactionRef = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(INCOMES_COLLECTION)
                    .document(incomeTransactionId)

                val updateData = mapOf(
                    "amount" to incomeTransaction.amount,
                    "note" to incomeTransaction.note,
                    "category" to incomeTransaction.category,
                    "createdAt" to incomeTransaction.createdAt
                )
                transactionRef.update(updateData).await()
                Timber.i("IncomeTransactionRepo: Successfully updated transaction $incomeTransactionId")
                Result.success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "IncomeTransactionRepo: Failed to update transaction ${incomeTransaction.id}")
                Result.failure(e)
            }
        }

    override suspend fun deleteIncomeTransaction(transactionId: String): Result<Unit> =
        withContext(ioDispatchers) {
            try {
                Timber.d("IncomeTransactionRepo: Attempting to delete transaction $transactionId")
                val userId = auth.currentUser?.uid
                    ?: return@withContext Result.failure(Exception("User id is not found").also {
                        Timber.e(it, "IncomeTransactionRepo: User not logged in while deleting transaction $transactionId")
                    })

                firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(INCOMES_COLLECTION)
                    .document(transactionId)
                    .delete()
                    .await()

                Timber.i("IncomeTransactionRepo: Successfully deleted transaction $transactionId")
                Result.success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "IncomeTransactionRepo: Failed to delete transaction $transactionId")
                Result.failure(e)
            }
        }
}
