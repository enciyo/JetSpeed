package com.enciyo.data.source.local

import com.enciyo.data.model.LocalDataPreferences
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    val store: Flow<LocalDataPreferences>
    suspend fun updateHost(host: String)
}


