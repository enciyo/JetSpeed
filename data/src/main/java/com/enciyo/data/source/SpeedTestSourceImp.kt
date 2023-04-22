package com.enciyo.data.source

import android.util.Log
import com.enciyo.data.model.SpeedTestResult
import fr.bmartel.speedtest.SpeedTestReport
import fr.bmartel.speedtest.SpeedTestSocket
import fr.bmartel.speedtest.inter.ISpeedTestListener
import fr.bmartel.speedtest.model.SpeedTestError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.UUID
import javax.inject.Inject

class SpeedTestSourceImp @Inject constructor() : SpeedTestSource {

    override fun getDownloadSpeed(host: String) = callbackFlow<SpeedTestResult> {
        val speedTestSocket = SpeedTestSocket()
        val transferBit =  arrayListOf<BigDecimal>()
        val listener = object : ISpeedTestListener {
            override fun onCompletion(report: SpeedTestReport?) {
                Log.i("MyLogger","onCompletion")
                val ort = transferBit.sumOf { it }.div(transferBit.size.toBigDecimal())
                Log.i("MyLogger","onCompletion $ort")
                trySendBlocking(SpeedTestResult.OnProgress(1f,bytesIntoHumanReadable(ort)))

            }

            override fun onProgress(percent: Float, report: SpeedTestReport) {
                Log.i("MyLogger","onProgress $percent ${report.transferRateBit}")
                transferBit.add(report.transferRateBit)
                trySendBlocking(SpeedTestResult.OnProgress(percent, bytesIntoHumanReadable(report.transferRateBit)))
            }

            override fun onError(speedTestError: SpeedTestError?, errorMessage: String?) {
                Log.i("MyLogger","$errorMessage")
                Log.i("MyLogger","${speedTestError?.name}")
            }
        }
        speedTestSocket.addSpeedTestListener(listener)

        withContext(Dispatchers.IO){
            speedTestSocket.startDownload("https://$host/download?nocache${UUID.randomUUID()}&size=100000000&guid=${UUID.randomUUID()}")
        }
        awaitClose {
            speedTestSocket.removeSpeedTestListener(listener)
        }
    }


    override fun getUploadSpeed(host: String) = callbackFlow<SpeedTestResult> {
        val speedTestSocket = SpeedTestSocket()
        val listener = object : ISpeedTestListener {
            override fun onCompletion(report: SpeedTestReport?) {
                trySend(SpeedTestResult.OnComplete)
            }

            override fun onProgress(percent: Float, report: SpeedTestReport) {
                trySend(SpeedTestResult.OnProgress(percent, bytesIntoHumanReadable(report.transferRateBit)))
            }

            override fun onError(speedTestError: SpeedTestError?, errorMessage: String?) {
                close(Throwable(errorMessage))
            }
        }
        speedTestSocket.addSpeedTestListener(listener)

        speedTestSocket.startUpload(
            "$host/upload?nocache=${UUID.randomUUID()}&guid=${UUID.randomUUID()}",
            1000000
        )
        awaitClose {
            speedTestSocket.removeSpeedTestListener(listener)
        }
    }
        .flowOn(Dispatchers.IO)

    private fun bytesIntoHumanReadable(bytes: BigDecimal): String {
        val kilobyte: BigDecimal = (1024).toLong().toBigDecimal()
        val megabyte = kilobyte * kilobyte
        val gigabyte = megabyte * kilobyte
        val terabyte = gigabyte * kilobyte
        return if (bytes >= BigDecimal(0) && (bytes < kilobyte)) {
            "$bytes B"
        } else if (bytes >= kilobyte && (bytes < megabyte)) {
            (bytes / kilobyte).toString() + " KB"
        } else if (bytes >= megabyte && (bytes < gigabyte)) {
            (bytes / megabyte).toString() + " MB"
        } else if (bytes >= gigabyte && (bytes < terabyte)) {
            (bytes / gigabyte).toString() + " GB"
        } else if (bytes >= terabyte) {
            (bytes / terabyte).toString() + " TB"
        } else {
            "$bytes Bytes"
        }
    }

    fun Long.toBigDecimal(): BigDecimal = BigDecimal.valueOf(this)

}