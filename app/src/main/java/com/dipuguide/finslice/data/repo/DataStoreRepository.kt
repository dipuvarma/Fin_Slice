package com.dipuguide.finslice.data.repo

import android.preference.Preference
import javax.inject.Inject
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dipuguide.finslice.utils.DataStoreUtil

class DataStoreRepository @Inject constructor(
    private val dataStore: DataStoreUtil
) {

    suspend fun onLoggedIn(){
        dataStore.setData("isLoggedIn", true)
    }

    suspend fun isLoggedIn(): Boolean {
        return dataStore.getData("isLoggedIn") ?: false
    }

    suspend fun onLogout(){
        dataStore.clear()
    }

}