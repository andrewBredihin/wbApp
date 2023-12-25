package com.bav.core.api

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TokenManager {
    val getToken: Flow<String?>
    suspend fun saveToken(token: String)
    suspend fun removeToken()
}

class AppTokenManager(private val context: Context) : TokenManager {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_data_store")
        private val key = stringPreferencesKey("token_key")
    }

    override val getToken: Flow<String?>
        get() = context.dataStore.data.map { it[key] }


    override suspend fun saveToken(token: String) {
        context.dataStore.edit { it[key] = token }
    }

    override suspend fun removeToken() {
        context.dataStore.edit { it.clear() }
    }
}