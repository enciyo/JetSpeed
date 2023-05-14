package com.enciyo.data.model

import java.math.BigDecimal

sealed interface SpeedTestResult {
    data class OnProgress(
        val percent: Float,
        val transferRateBit: String
    ) : SpeedTestResult

    object OnComplete : SpeedTestResult
}