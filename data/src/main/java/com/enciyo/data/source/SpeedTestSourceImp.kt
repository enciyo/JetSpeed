package com.enciyo.data.source

import com.enciyo.data.ext.createSpeedTestListener
import com.enciyo.data.ext.toHumanReadable
import com.example.domain.model.SpeedTestResult
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

    companion object {
        private const val OCTETS_TO_MB = 1_048_576
        const val SIZE_DOWNLOAD = 100 * OCTETS_TO_MB
        const val SIZE_UPLOAD = 10 * OCTETS_TO_MB
    }

    override fun getDownloadSpeed(host: String) = getSpeed(isDownload = true, host = host)

    override fun getUploadSpeed(host: String) = getSpeed(isDownload = false, host = host)

    //TO-DO reorganize code by business logic, it contains the presentation logic.
    private fun getSpeed(isDownload: Boolean, host: String) = callbackFlow {
            val socket = SpeedTestSocket()
            val listener = createSpeedTestListener(
                onCompletion = {
                    trySendBlocking(SpeedTestResult.OnComplete(it.transferRateBit.toHumanReadable()))
                },
                onProgress = { percent, report ->
                    val animPercent = 0.125f + (percent * 0.001f)
                    trySendBlocking(
                        SpeedTestResult.OnProgress(
                            percent = animPercent,
                            transferRateBit = report.transferRateBit.toHumanReadable()
                        )
                    )
                },
                onError = { _, errorMessage ->
                    close(Throwable(errorMessage))
                }
            )
            socket.addSpeedTestListener(listener)
            if (isDownload)
                socket.startDownload("${host.toHttps()}download?${uniqueQueries()}&size=$SIZE_DOWNLOAD")
            else
                socket.startUpload(
                    "${host.toHttps()}upload?${uniqueQueries()}",
                    SIZE_UPLOAD
                )

            awaitClose {
                socket.removeSpeedTestListener(listener)
            }
        }
            .flowOn(Dispatchers.IO)


    private fun String.toHttps() =
        "https://$this/"

    private fun uniqueQueries() =
        "nocache=${UUID.randomUUID()}&guid=${UUID.randomUUID()}"

}