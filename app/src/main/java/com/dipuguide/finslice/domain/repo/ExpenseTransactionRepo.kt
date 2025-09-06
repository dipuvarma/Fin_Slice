package com.dipuguide.finslice.domain.repo

import com.dipuguide.finslice.domain.model.ExpenseTransaction
import com.dipuguide.finslice.utils.DateFilterType
import kotlinx.coroutines.flow.Flow

interface ExpenseTransactionRepo {
    suspend fun addExpenseTransaction(expenseTransaction: ExpenseTransaction): Result<Unit>
    fun getExpenseTransaction(): Flow<Result<List<ExpenseTransaction>>>
    fun getAllExpensesByCategory(category: String): Flow<Result<List<ExpenseTransaction>>>
    suspend fun editExpenseTransaction(expenseTransaction: ExpenseTransaction): Result<Unit>
    fun getAllExpensesByMonth(month: Int, year: Int): Flow<Result<List<ExpenseTransaction>>>
    fun getAllExpensesByDateRange(filter: DateFilterType): Flow<Result<List<ExpenseTransaction>>>
    suspend fun deleteExpenseTransaction(expenseId: String): Result<Unit>
}