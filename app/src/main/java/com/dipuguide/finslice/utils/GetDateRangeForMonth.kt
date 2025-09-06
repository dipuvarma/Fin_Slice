package com.dipuguide.finslice.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar


fun getMillisRangeForMonth(month: Int, year: Int): Pair<Long, Long> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        getMillisRangeForMonthOreo(month, year)
    } else {
        getMillisRangeForMonthLegacy(month, year)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun getMillisRangeForMonthOreo(month: Int, year: Int): Pair<Long, Long> {
    val zoneId = ZoneId.systemDefault()

    val startOfMonth = LocalDate.of(year, month, 1)
        .atStartOfDay(zoneId)
        .toInstant()
        .toEpochMilli()

    val startOfNextMonth = LocalDate.of(year, month, 1)
        .plusMonths(1)
        .atStartOfDay(zoneId)
        .toInstant()
        .toEpochMilli()

    return startOfMonth to (startOfNextMonth - 1)
}

private fun getMillisRangeForMonthLegacy(month: Int, year: Int): Pair<Long, Long> {
    val calendar = Calendar.getInstance().apply {
        clear()
        set(year, month - 1, 1, 0, 0, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val startOfMonth = calendar.timeInMillis

    calendar.add(Calendar.MONTH, 1)
    val startOfNextMonth = calendar.timeInMillis

    return startOfMonth to (startOfNextMonth - 1)
}