package com.dipuguide.finslice.di

import android.content.Context
import com.dipuguide.finslice.data.local.PreferencesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Provides
    @Singleton
    fun providePreferencesDataSource(@ApplicationContext context: Context) =
        PreferencesDataSource(context)


}