package com.enciyo.jetspeed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.enciyo.jetspeed.ui.home.HomeRoute
import com.enciyo.jetspeed.ui.theme.JetSpeedTheme
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetSpeedTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeRoute()
                }
            }
        }

    }
}

private fun bytesIntoHumanReadable(bytes: BigDecimal): String? {
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