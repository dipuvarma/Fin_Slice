package com.dipuguide.finslice.data.repo

import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionUi
import com.dipuguide.finslice.utils.DateFilterType
import kotlinx.coroutines.flow.Flow

interface IncomeTransactionRepo {

    suspend fun addIncomeTransaction(incomeTransactionUi: IncomeTransactionUi): Result<Unit>

    suspend fun getIncomeTransaction(): Flow<Result<List<IncomeTransactionUi>>>
    suspend fun getIncomeTransactionByDate(filter: DateFilterType): Flow<Result<List<IncomeTransactionUi>>>
    suspend fun editIncomeTransaction(incomeTransactionUi: IncomeTransactionUi): Result<Unit>

    suspend fun deleteIncomeTransaction(id: String): Result<Unit>
}