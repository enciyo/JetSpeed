package com.enciyo.data.source

import com.enciyo.data.model.SpeedTestResult
import kotlinx.coroutines.flow.Flow


interface SpeedTestSource {
    fun getDownloadSpeed(host: String): Flow<SpeedTestResult>
    fun getUploadSpeed(host: String): Flow<SpeedTestResult>
}
