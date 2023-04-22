package com.enciyo.data.source

import com.enciyo.data.model.Server

interface RemoteDataSource {
    suspend fun getSettings(): Result<List<Server>>
}


