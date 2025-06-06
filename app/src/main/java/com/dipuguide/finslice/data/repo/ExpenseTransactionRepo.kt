package com.dipuguide.finslice.data.repo

import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionUi
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionUi
import kotlinx.coroutines.flow.Flow

interface ExpenseTransactionRepo {

    suspend fun addExpenseTransaction(expenseTransactionUi: ExpenseTransactionUi): Result<Unit>

    suspend fun getExpenseTransaction(): Flow<Result<List<ExpenseTransactionUi>>>
    suspend fun getAllExpensesByCategory(category: String): Flow<Result<List<ExpenseTransactionUi>>>
    suspend fun editExpenseTransaction(expenseTransactionUi: ExpenseTransactionUi): Result<Unit>

    suspend fun deleteExpenseTransaction(id: String): Result<Unit>
}