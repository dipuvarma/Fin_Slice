package com.dipuguide.finslice.di

import com.dipuguide.finslice.data.repo.DataStoreRepository
import com.dipuguide.finslice.data.repo.ExpenseTransactionRepo
import com.dipuguide.finslice.data.repo.IncomeTransactionRepo
import com.dipuguide.finslice.data.repo.IncomeTransactionRepoImpl
import com.dipuguide.finslice.data.repo.ExpenseTransactionRepoImpl
import com.dipuguide.finslice.presentation.screens.addTransaction.expense.AddExpenseViewModel
import com.dipuguide.finslice.presentation.screens.addTransaction.income.AddIncomeViewModel
import com.dipuguide.finslice.presentation.screens.main.category.CategoryViewModel
import com.dipuguide.finslice.presentation.screens.main.history.TransactionHistoryViewModel
import com.dipuguide.finslice.presentation.screens.main.home.HomeViewModel
import com.dipuguide.finslice.presentation.screens.main.report.ReportViewModel
import com.dipuguide.finslice.presentation.screens.main.setting.SettingViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.ExpenseTransactionViewModel
import com.dipuguide.finslice.presentation.screens.main.transaction.IncomeTransactionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TransactionModule {

    @Provides
    @Singleton
    fun provideFireStoreAndAuth(
        firebaseFireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ) =
        ExpenseTransactionRepoImpl(firebaseFireStore, firebaseAuth)


    @Provides
    @Singleton
    fun provideIncomeTransactionRepo(
        incomeTransactionRepo: IncomeTransactionRepo,
    ) =
        IncomeTransactionViewModel(incomeTransactionRepo)


    @Provides
    @Singleton
    fun provideIncomeFireStoreAndAuth(
        firebaseFireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): IncomeTransactionRepo {
        return IncomeTransactionRepoImpl(firebaseFireStore, firebaseAuth)
    }


    @Provides
    @Singleton
    fun provideExpenseFireStoreAndAuth(
        firebaseFireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): ExpenseTransactionRepo {
        return ExpenseTransactionRepoImpl(
            firebaseFireStore,
            firebaseAuth
        )
    }

    @Provides
    @Singleton
    fun provideExpenseTransactionRepo(expenseTransactionRepo: ExpenseTransactionRepo) =
        ExpenseTransactionViewModel(expenseTransactionRepo)


    @Provides
    @Singleton
    fun provideIncomeAndExpenseRepo(
        incomeTransactionRepo: IncomeTransactionRepo,
        expenseTransactionRepo: ExpenseTransactionRepo,
    ) =
        HomeViewModel(
            incomeTransactionRepo = incomeTransactionRepo,
            expenseTransactionRepo = expenseTransactionRepo
        )


    @Provides
    @Singleton
    fun provideCategoryExpenseRepo(expenseTransactionRepo: ExpenseTransactionRepo) =
        CategoryViewModel(expenseTransactionRepo)

    @Provides
    @Singleton
    fun provideAddIncomeRep(incomeTransactionRepo: IncomeTransactionRepo) =
        AddIncomeViewModel(incomeTransactionRepo)


    @Provides
    @Singleton
    fun provideAddExpenseRepo(expenseTransactionRepo: ExpenseTransactionRepo) =
        AddExpenseViewModel(expenseTransactionRepo)

    @OptIn(ExperimentalStdlibApi::class)
    @Provides
    @Singleton
    fun provideHistoryIncomeAndExpenseRepo(
        expenseTransactionRepo: ExpenseTransactionRepo,
        incomeTransactionRepo: IncomeTransactionRepo,
    ) = TransactionHistoryViewModel(
        expenseTransactionRepo = expenseTransactionRepo,
        incomeTransactionRepo = incomeTransactionRepo
    )

    @Provides
    @Singleton
    fun provideSettingDataStore(dataStoreRepository: DataStoreRepository) =
        SettingViewModel(dataStoreRepository)

    @Provides
    @Singleton
    fun provideReportExpenseRepo(expenseTransactionRepo: ExpenseTransactionRepo) =
        ReportViewModel(expenseTransactionRepo)
}
