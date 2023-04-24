package com.enciyo.data.ext

import fr.bmartel.speedtest.SpeedTestReport
import fr.bmartel.speedtest.inter.ISpeedTestListener
import fr.bmartel.speedtest.model.SpeedTestError
import kotlin.math.nextDown
import kotlin.math.nextUp
import kotlin.math.roundToInt


fun createSpeedTestListener(
    onCompletion: (report: SpeedTestReport) -> Unit = {},
    onProgress: (percent: Int, report: SpeedTestReport) -> Unit = { _, _ -> },
    onError: (speedTestError: SpeedTestError, errorMessage: String) -> Unit = { _, _ -> }
) = object : ISpeedTestListener {
    override fun onCompletion(report: SpeedTestReport) {
        onCompletion.invoke(report)
    }

    override fun onProgress(percent: Float, report: SpeedTestReport) {
        onProgress.invoke(percent.roundToInt() + 1, report)
    }

    override fun onError(speedTestError: SpeedTestError, errorMessage: String) {
        onError.invoke(speedTestError, errorMessage)
    }
}

