package com.dipuguide.finslice.domain.repo

import com.dipuguide.finslice.domain.model.IncomeTransaction
import com.dipuguide.finslice.utils.DateFilterType
import kotlinx.coroutines.flow.Flow

interface IncomeTransactionRepo {

    suspend fun addIncomeTransaction(incomeTransaction: IncomeTransaction): Result<Unit>
    suspend fun getIncomeTransaction(): Flow<Result<List<IncomeTransaction>>>

    suspend fun getIncomeTransactionByDate(filter: DateFilterType): Flow<Result<List<IncomeTransaction>>>
    suspend fun getAllIncomeByMonth(month: Int, year: Int): Flow<Result<List<IncomeTransaction>>>

    suspend fun editIncomeTransaction(incomeTransaction: IncomeTransaction): Result<Unit>
    suspend fun deleteIncomeTransaction(transactionId: String): Result<Unit>
}