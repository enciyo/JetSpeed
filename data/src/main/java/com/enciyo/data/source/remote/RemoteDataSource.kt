package com.enciyo.data.source.remote

import com.example.domain.model.Server

interface RemoteDataSource {
    suspend fun getSettings(): Result<List<Server>>
}


