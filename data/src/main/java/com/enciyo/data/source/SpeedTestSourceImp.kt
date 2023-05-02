package com.enciyo.data.source

import com.enciyo.data.ext.createSpeedTestListener
import com.enciyo.data.ext.toHumanReadable
import com.enciyo.data.model.SpeedTestResult
import fr.bmartel.speedtest.SpeedTestSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject

class SpeedTestSourceImp @Inject constructor() : SpeedTestSource {

    override fun getDownloadSpeed(host: String) = getSpeed(isDownload = true, host = host)

    override fun getUploadSpeed(host: String) = getSpeed(isDownload = false, host = host)

    private fun getSpeed(isDownload: Boolean, host: String) =
        callbackFlow<SpeedTestResult> {
            val socket = SpeedTestSocket()
            val transferBit = arrayListOf<BigDecimal>()
            val listener = createSpeedTestListener(
                onCompletion = {
                    val ort = transferBit.sumOf { it }.div(transferBit.size.toBigDecimal())
                    trySendBlocking(SpeedTestResult.OnProgress(1.0f, ort.toHumanReadable()))
                    trySendBlocking(SpeedTestResult.OnComplete)
                },
                onProgress = { percent, report ->
                    val animPercent = 0.125f + (percent * 0.001f)
                    val humanReadable = report.transferRateBit.toHumanReadable()
                    transferBit.add(report.transferRateBit)
                    val progress = SpeedTestResult.OnProgress(animPercent, humanReadable)
                    trySendBlocking(progress)
                },
                onError = { _, errorMessage ->
                    close(Throwable(errorMessage))
                }
            )
            socket.addSpeedTestListener(listener)
            if (isDownload)
                socket.startDownload("https://$host/download?nocache${UUID.randomUUID()}&size=100000000&guid=${UUID.randomUUID()}")
            else
                socket.startUpload(
                    "https://$host/upload?nocache=${UUID.randomUUID()}&guid=${UUID.randomUUID()}",
                    1000000000
                )

            awaitClose {
                socket.removeSpeedTestListener(listener)
            }
        }
            .flowOn(Dispatchers.IO)


}