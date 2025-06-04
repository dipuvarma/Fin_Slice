package com.dipuguide.finslice.di

import com.dipuguide.finslice.data.repo.IncomeTransactionRepo
import com.dipuguide.finslice.data.repo.IncomeTransactionRepoImpl
import com.dipuguide.finslice.data.repo.TransactionRepository
import com.dipuguide.finslice.presentation.screens.main.IncomeTransactionViewModel
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
        TransactionRepository(firebaseFireStore, firebaseAuth)


    @Provides
    @Singleton
    fun provideTransactionRepo(transactionRepository: TransactionRepository, incomeTransactionRepo: IncomeTransactionRepo) =
        IncomeTransactionViewModel(transactionRepository, incomeTransactionRepo)


    @Provides
    @Singleton
    fun provideIncomeFireStoreAndAuth(
        firebaseFireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): IncomeTransactionRepo {
        return IncomeTransactionRepoImpl(firebaseFireStore, firebaseAuth)
    }
}
