package com.dipuguide.finslice.data.repo

import com.dipuguide.finslice.data.model.IncomeTransaction
import com.dipuguide.finslice.presentation.screens.main.IncomeTransactionUi
import kotlinx.coroutines.flow.Flow

interface IncomeTransactionRepo {

    suspend fun addIncomeTransaction(incomeTransactionUi: IncomeTransactionUi): Result<Unit>

    suspend fun getIncomeTransaction(): Flow<Result<List<IncomeTransactionUi>>>

    suspend fun editIncomeTransaction(incomeTransactionUi: IncomeTransactionUi): Result<Unit>

    suspend fun deleteIncomeTransaction(id: String): Result<Unit>
}