package com.enciyo.data.ext

import java.math.BigDecimal

fun BigDecimal.toHumanReadable(): String {
    val bytes = this
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
