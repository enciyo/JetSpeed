package com.example.domain.model

sealed interface SpeedTestResult {
    data class OnProgress(
        val percent: Float,
        val transferRateBit: String
    ) : SpeedTestResult

    object OnComplete : SpeedTestResult
}