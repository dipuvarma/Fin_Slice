package com.dipuguide.finslice.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

interface GetDateRangeForMonth{
    fun getDateRangeForMonth(month: Int, year: Int): Pair<Long, Long>
}


@RequiresApi(Build.VERSION_CODES.O)
fun getMillisRangeForMonth(month: Int, year: Int): Pair<Long, Long> {
    val zoneId = ZoneId.systemDefault()

    // Start of selected month
    val startOfMonth = LocalDate.of(year, month, 1)
        .atStartOfDay(zoneId)
        .toInstant()
        .toEpochMilli()

    // Start of next month (exclusive upper bound)
    val startOfNextMonth = startOfMonth
        .let { Instant.ofEpochMilli(it) }
        .atZone(zoneId)
        .toLocalDate()
        .plusMonths(1)
        .atStartOfDay(zoneId)
        .toInstant()
        .toEpochMilli()

    return startOfMonth to (startOfNextMonth - 1)
}

