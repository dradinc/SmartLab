package com.example.smartlabs.Common

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SignInStatus(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="USER_DATA")
    private var dataStore = context.dataStore
    companion object {
        // Добавляем ключ, по которому будем сохранять данные
        val SIGN_IN_STATUS = booleanPreferencesKey(name="SIGN_IN_STATUS")
        val SIGN_IN_TOKEN = stringPreferencesKey(name="SIGN_IN_TOKEN")
    }

    suspend fun setSignInStatus(isSignInStatus: Boolean, isSignInToken: String) {
        dataStore.edit {
            it[SIGN_IN_STATUS] = isSignInStatus
            it[SIGN_IN_TOKEN] = isSignInToken
        }
    }

    fun getSignInStatus(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) { Log.e("DATA_STORE_ERROR", "AAAAAAA") }
                else { throw exception }
            }
            .map {
                    preferences ->
                val statusSignIn = preferences[SIGN_IN_STATUS] ?: false
                statusSignIn
            }
    }

    fun getSignInToken(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) { Log.e("DATA_STORE_ERROR", "AAAAAAA") }
                else { throw exception }
            }
            .map {
                    preferences ->
                val tokenSignIn = preferences[SIGN_IN_TOKEN] ?: ""
                tokenSignIn
            }
    }
}