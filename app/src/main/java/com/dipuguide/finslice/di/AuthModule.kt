package com.dipuguide.finslice.di

import android.preference.PreferenceDataStore
import androidx.datastore.core.DataStore
import com.dipuguide.finslice.data.repo.DataStoreRepository
import com.dipuguide.finslice.data.repo.FirebaseAuthRepository
import com.dipuguide.finslice.presentation.screens.auth.AuthViewModel
import com.dipuguide.finslice.utils.DataStoreUtil
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthAndFireStore(firestore: FirebaseFirestore, auth: FirebaseAuth) =
        FirebaseAuthRepository(firestore = firestore, auth = auth)

    @Provides
    @Singleton
    fun provideRepo(authRepository: FirebaseAuthRepository, dataStoreRepo: DataStoreRepository) = AuthViewModel(authRepository, dataStoreRepo)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context) =
        DataStoreRepository(DataStoreUtil.create(context = context))


}