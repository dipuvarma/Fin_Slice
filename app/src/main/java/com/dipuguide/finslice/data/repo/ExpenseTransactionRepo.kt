package com.dipuguide.finslice.data.repo

import com.dipuguide.finslice.presentation.model.ExpenseTransactionUi
import com.dipuguide.finslice.utils.DateFilterType
import kotlinx.coroutines.flow.Flow

interface ExpenseTransactionRepo {

    suspend fun addExpenseTransaction(expenseTransactionUi: ExpenseTransactionUi): Result<Unit>
    suspend fun getExpenseTransaction(): Flow<Result<List<ExpenseTransactionUi>>>
    suspend fun getAllExpensesByCategory(category: String): Flow<Result<List<ExpenseTransactionUi>>>
    suspend fun editExpenseTransaction(expenseTransactionUi: ExpenseTransactionUi): Result<Unit>
    suspend fun getAllExpensesByMonth(month: Int, year: Int): Flow<Result<List<ExpenseTransactionUi>>>
    suspend fun getAllExpensesByDateRange(filter: DateFilterType): Flow<Result<List<ExpenseTransactionUi>>>
    suspend fun deleteExpenseTransaction(id: String): Result<Unit>
}