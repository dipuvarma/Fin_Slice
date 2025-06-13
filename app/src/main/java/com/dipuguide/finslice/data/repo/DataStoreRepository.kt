package com.dipuguide.finslice.data.repo

import android.preference.Preference
import javax.inject.Inject
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dipuguide.finslice.utils.DataStoreUtil
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class DataStoreRepository @Inject constructor(
    private val dataStore: DataStoreUtil,
) {

    suspend fun onLoggedIn() {
        dataStore.setData("isLoggedIn", true)
    }

    suspend fun isLoggedIn(): Boolean {
        return dataStore.getData("isLoggedIn") ?: false
    }

    suspend fun darkModeOn() {
        dataStore.setData("isDarkMode", true)
    }

    suspend fun isDarkMode(): Flow<Boolean> {
        return dataStore.isDarkMode()
    }

    suspend fun darkModeOff() {
        dataStore.removeKey("isDarkMode") // ✅ safer
    }

    suspend fun dynamicModeOn() {
        dataStore.setData("isDynamicMode", true)
    }

    suspend fun dynamicModeOff() {
        dataStore.removeKey("isDynamicMode")
    }

    suspend fun isDynamicMode(): Flow<Boolean> {
        return dataStore.isDynamicMode()
    }

    suspend fun onLogout() {
        dataStore.removeKey("isLoggedIn")
    }

}