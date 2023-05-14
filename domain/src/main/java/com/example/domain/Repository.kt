package com.example.domain

import com.example.domain.model.Server
import com.example.domain.model.SpeedTestResult
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getSettings(): Flow<Result<List<Server>>>
    fun updateHost(server: Server): Flow<Result<Server>>
    fun getDownloadSpeed(): Flow<SpeedTestResult>
    fun getUploadSpeed(): Flow<SpeedTestResult>
}