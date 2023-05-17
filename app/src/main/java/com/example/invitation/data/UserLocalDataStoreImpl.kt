package com.example.invitation.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserLocalDataStoreImpl @Inject constructor(private val dataStore: DataStore<Preferences>): UserLocalDataStore {

    override suspend fun setRegister(isRegistered: Boolean) {
        dataStore.edit {
            val key = booleanPreferencesKey("isRegistered")
            it[key] = isRegistered
        }
    }

    override suspend fun isRegistered(): Boolean {
        val key = booleanPreferencesKey("isRegistered")
        return dataStore.data.first()[key] ?: false
    }
}