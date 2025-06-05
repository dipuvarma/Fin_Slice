package com.dipuguide.finslice.data.repo

import android.nfc.Tag
import android.util.Log
import com.dipuguide.finslice.data.model.ExpenseTransaction
import com.dipuguide.finslice.presentation.mapper.toExpenseTransaction
import com.dipuguide.finslice.presentation.mapper.toExpenseTransactionUi
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionUi
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionUi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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

    override suspend fun getExpenseTransaction(): Flow<Result<List<ExpenseTransactionUi>>> {
        TODO("Not yet implemented")
    }

    override suspend fun editExpenseTransaction(expenseTransactionUi: ExpenseTransactionUi): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteExpenseTransaction(id: String): Result<Unit> {
        TODO("Not yet implemented")
    }


}