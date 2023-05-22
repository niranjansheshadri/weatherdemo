package com.example.weatherdemo.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun convertUnixTimestampToHumanReadable(unixTimestamp: Long): String {
    val instant = Instant.ofEpochSecond(unixTimestamp)
    val zoneId = ZoneId.systemDefault()

    val localDateTime = LocalDateTime.ofInstant(instant, zoneId)

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return localDateTime.format(formatter)
}

fun getCurrentDateTime(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")
    return currentDateTime.format(formatter)
}
