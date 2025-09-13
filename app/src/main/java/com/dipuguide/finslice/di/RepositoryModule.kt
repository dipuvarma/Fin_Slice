package com.dipuguide.finslice.di

import com.dipuguide.finslice.data.repo.ExpenseTransactionRepoImpl
import com.dipuguide.finslice.data.repo.IncomeTransactionRepoImpl
import com.dipuguide.finslice.data.repo.ThemeRepositoryImpl
import com.dipuguide.finslice.data.repo.UserAuthRepositoryImpl
import com.dipuguide.finslice.domain.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.domain.repo.IncomeTransactionRepo
import com.dipuguide.finslice.domain.repo.ThemeRepository
import com.dipuguide.finslice.domain.repo.UserAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserAuthRepo(userAuthRepositoryImpl: UserAuthRepositoryImpl): UserAuthRepository


    @Binds
    @Singleton
    abstract fun bindExpenseTransactionRepo(expenseTransactionRepoImpl: ExpenseTransactionRepoImpl): ExpenseTransactionRepo

    @Binds
    @Singleton
    abstract fun bindIncomeTransactionRepo(incomeTransactionRepoImpl: IncomeTransactionRepoImpl): IncomeTransactionRepo


    @Binds
    @Singleton
    abstract fun bindSharedPreferenceRepository(
        themePreferencesImpl: ThemeRepositoryImpl,
    ): ThemeRepository


}