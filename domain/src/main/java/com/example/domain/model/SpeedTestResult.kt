package com.example.domain.model

sealed interface SpeedTestResult {
    data class OnProgress(
        val percent: Float,
        val transferRateBit: String
    ) : SpeedTestResult

    data class OnComplete(
        val time: String
    ) : SpeedTestResult
}