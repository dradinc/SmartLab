package com.example.smartlabs.Common

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class OnBoardingStatus(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="APP_DATA")

        // Добавляем ключ, по которому будем сохранять данные
        val ON_BOARDING_STATUS = booleanPreferencesKey(name="ON_BOARDING_STATUS")
    }

    suspend fun setOnBoardingStatus(isOnBoardingStatus: Boolean) {
        // Функция для сохранения данных по ключу
        context.dataStore.edit { preferences ->
            preferences[ON_BOARDING_STATUS] = isOnBoardingStatus
        }
    }

    fun getOnBoardingStatus(): Flow<Boolean> {
        // Функция для получения данных по ключу
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) { Log.e("DATA_STORE_ERROR", "AAAAAAA") }
                else { throw exception }
            }
            .map {
                preferences ->
                val statusOnBoarding = preferences[ON_BOARDING_STATUS] ?: false
                statusOnBoarding
            }
    }
}