package com.enciyo.data.source.remote

import com.enciyo.data.model.Server

interface RemoteDataSource {
    suspend fun getSettings(): Result<List<Server>>
}


