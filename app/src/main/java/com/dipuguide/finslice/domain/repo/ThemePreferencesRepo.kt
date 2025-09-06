package com.dipuguide.finslice.domain.repo

import kotlinx.coroutines.flow.Flow

interface ThemePreferencesRepo {
    suspend fun clearSharedPreferences()
    suspend fun saveString(key: String, value: String)
    suspend fun getString(key: String, defaultValue: String): String
    suspend fun saveBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String, defaultValue: Boolean): Flow<Boolean>
}