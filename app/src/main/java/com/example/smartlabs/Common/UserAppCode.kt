package com.example.smartlabs.Common

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserAppCode(private val context: Context) {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="APP_CODE")
        val CODE_STATUS = booleanPreferencesKey("CODE_STATUS")
        val APP_CODE = intPreferencesKey("APP_CODE")
    }

    fun getAppCodeStatus(): Flow<Boolean> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) { Log.e("DATA_STORE_ERROR", "AAAAAAA") }
                else { throw exception }
            }
            .map { preferences ->
                val appCodeStatus = preferences[CODE_STATUS] ?: false
                appCodeStatus
            }
    }

    fun getAppCode(): Flow<Int> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) { Log.e("DATA_STORE_ERROR", "AAAAAAA") }
                else { throw exception }
            }
            .map { preferences ->
                val appCode = preferences[APP_CODE] ?: 0
                appCode
            }
    }

    suspend fun setAppCode(code: String) {
        context.dataStore.edit {
            it[CODE_STATUS] = true
            it[APP_CODE] = code.hashCode()
        }
    }

    suspend fun delAppCode() {
        context.dataStore.edit {
            it[CODE_STATUS] = false
            it[APP_CODE] = 0
        }
    }
}