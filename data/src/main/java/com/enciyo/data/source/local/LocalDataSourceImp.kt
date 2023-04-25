package com.enciyo.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.enciyo.data.model.LocalDataPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSourceImp @Inject constructor(private val dataStore: DataStore<Preferences>) :
    LocalDataSource {

    private object PreferencesKeys {
        val HOST = stringPreferencesKey("host")
        val SIZE = longPreferencesKey("size")
    }

    override val store
        get() = dataStore.data
            .catch { _ ->
                emit(emptyPreferences())
            }
            .map(::mapLocalPreferences)


    override suspend fun updateHost(host: String) {
        write(key = PreferencesKeys.HOST, host)
    }

    private suspend fun <T> write(key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[key] = value
        }
    }


    private fun mapLocalPreferences(preferences: Preferences): LocalDataPreferences =
        LocalDataPreferences(
            host = preferences[PreferencesKeys.HOST] ?: "",
            size = preferences[PreferencesKeys.SIZE] ?: 10
        )


}