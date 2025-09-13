package com.dipuguide.finslice.domain.repo

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    suspend fun getDarkMode(): Flow<Boolean>
    suspend fun saveDarkMode(isDarkMode: Boolean)
    suspend fun getDynamicMode(): Flow<Boolean>
    suspend fun saveDynamicMode(isDynamicMode: Boolean)
}