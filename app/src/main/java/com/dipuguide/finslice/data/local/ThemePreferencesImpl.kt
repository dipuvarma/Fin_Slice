package com.dipuguide.finslice.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.dipuguide.finslice.domain.repo.ThemePreferencesRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ThemePreferencesImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : ThemePreferencesRepo {

    companion object {
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    }

    override suspend fun clearSharedPreferences() {
        withContext(ioDispatcher) {
            sharedPreferences.edit { clear() }
        }
    }

    override suspend fun saveString(key: String, value: String) {
        withContext(ioDispatcher) {
            try {
                sharedPreferences.edit { putString(key, value) }
            } catch (e: Exception) {
                Timber.e(e, "Failed to save string to SharedPreferences")
            }
        }
    }

    override suspend fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    override suspend fun saveBoolean(key: String, value: Boolean) {
        withContext(ioDispatcher) {
            sharedPreferences.edit { putBoolean(key, value) }
        }
    }

    override suspend fun getBoolean(
        key: String,
        defaultValue: Boolean,
    ): Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, changedKey ->
            if (changedKey == key) {
                trySend(prefs.getBoolean(key, defaultValue)).isSuccess
            }
        }

        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        // Emit the current value immediately
        trySend(sharedPreferences.getBoolean(key, defaultValue)).isSuccess

        // Clean up when the flow is closed
        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

}