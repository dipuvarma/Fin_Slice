package com.dipuguide.finslice.utils

import androidx.datastore.preferences.core.booleanPreferencesKey

object Constants {
    const val DATASTORE_PREFS = "datastore_prefs"
    val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    val DYNAMIC_MODE_KEY = booleanPreferencesKey("dynamic_mode")
}