package com.dipuguide.finslice.data.repo

import android.net.Uri
import com.dipuguide.finslice.utils.DataStoreUtil
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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
        dataStore.setData("isDarkMode", false)
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

    suspend fun signOut() {
        dataStore.removeKey("isLoggedIn")
    }

    suspend fun saveName(name: String?) {
        dataStore.setData("name", name)
    }

    suspend fun saveEmail(email: String?) {
        dataStore.setData("email", email)
    }

    suspend fun savePhoto(photo: Uri?) {
        dataStore.setData("photo", photo?.toString())
    }

    suspend fun savePhoneNumber(phoneNumber: String?) {
        dataStore.setData("phoneNumber", phoneNumber)
    }


    suspend fun getName(): String? {
        return dataStore.getData("name")
    }

    suspend fun getEmail(): String? {
        return dataStore.getData("email")
    }

    suspend fun getPhoto(): String? {
        return dataStore.getData("photo")
    }

    suspend fun getPhoneNumber(): String? {
        return dataStore.getData("phoneNumber")
    }

}