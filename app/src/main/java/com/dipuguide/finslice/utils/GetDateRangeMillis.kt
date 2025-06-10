package com.dipuguide.finslice.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
fun getDateRangeMillis(filter: DateFilterType): Pair<Long, Long> {
    val zoneId = ZoneId.systemDefault()
    val now = LocalDate.now(zoneId)

    return when (filter) {
        is DateFilterType.Today -> {
            val start = now.atStartOfDay(zoneId).toInstant().toEpochMilli()
            val end = now.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1
            start to end
        }

        is DateFilterType.Yesterday -> {
            val start = now.minusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli()
            val end = now.atStartOfDay(zoneId).toInstant().toEpochMilli() - 1
            start to end
        }

        is DateFilterType.Last7Days -> {
            val start = now.minusDays(6).atStartOfDay(zoneId).toInstant().toEpochMilli()
            val end = now.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1
            start to end
        }

        is DateFilterType.ThisMonth -> {
            val start = now.withDayOfMonth(1).atStartOfDay(zoneId).toInstant().toEpochMilli()
            val end = now.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1
            start to end
        }

        is DateFilterType.ThisYear -> {
            val start = now.withDayOfYear(1).atStartOfDay(zoneId).toInstant().toEpochMilli()
            val end = now.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli() - 1
            start to end
        }
    }
}


sealed class DateFilterType {
    object Today : DateFilterType()
    object Yesterday : DateFilterType()
    object Last7Days : DateFilterType()
    object ThisMonth : DateFilterType()
    object ThisYear : DateFilterType()
}

