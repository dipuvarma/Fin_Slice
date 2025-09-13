package com.dipuguide.finslice.data.repo

import com.dipuguide.finslice.data.local.PreferencesDataSource
import com.dipuguide.finslice.domain.repo.ThemeRepository
import com.dipuguide.finslice.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
) : ThemeRepository {

    override suspend fun getDarkMode(): Flow<Boolean> {
        return preferencesDataSource.getPreference(Constants.DARK_MODE_KEY, false)
    }

    override suspend fun saveDarkMode(isDarkMode: Boolean) {
        preferencesDataSource.putPreference(Constants.DARK_MODE_KEY, isDarkMode)
    }

    override suspend fun getDynamicMode(): Flow<Boolean> {
        return preferencesDataSource.getPreference(Constants.DYNAMIC_MODE_KEY, false)
    }

    override suspend fun saveDynamicMode(isDynamicMode: Boolean) {
        preferencesDataSource.putPreference(Constants.DYNAMIC_MODE_KEY, isDynamicMode)
    }

}