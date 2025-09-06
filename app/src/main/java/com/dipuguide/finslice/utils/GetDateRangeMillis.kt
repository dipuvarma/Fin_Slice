package com.dipuguide.finslice.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.TimeZone


fun getDateRangeMillis(filter: DateFilterType): Pair<Long, Long> {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return when (filter) {
        DateFilterType.Today -> getTodayRange(calendar)
        DateFilterType.Yesterday -> getYesterdayRange(calendar)
        DateFilterType.Last7Days -> getLast7DaysRange(calendar)
        DateFilterType.Last30Days -> getLast30DaysRange(calendar)
        DateFilterType.ThisMonth -> getThisMonthRange(calendar)
        DateFilterType.LastMonth -> getLastMonthRange(calendar)
        DateFilterType.ThisYear -> getThisYearRange(calendar)
    }
}

private fun getTodayRange(calendar: Calendar): Pair<Long, Long> {
    val start = calendar.timeInMillis
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    val end = calendar.timeInMillis - 1
    return start to end
}

private fun getYesterdayRange(calendar: Calendar): Pair<Long, Long> {
    calendar.add(Calendar.DAY_OF_MONTH, -1)
    val start = calendar.timeInMillis
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    val end = calendar.timeInMillis - 1
    return start to end
}

private fun getLast7DaysRange(calendar: Calendar): Pair<Long, Long> {
    calendar.add(Calendar.DAY_OF_MONTH, -6)
    val start = calendar.timeInMillis
    calendar.add(Calendar.DAY_OF_MONTH, 7)
    val end = calendar.timeInMillis - 1
    return start to end
}

private fun getLast30DaysRange(calendar: Calendar): Pair<Long, Long> {
    calendar.add(Calendar.DAY_OF_MONTH, -29)
    val start = calendar.timeInMillis
    calendar.add(Calendar.DAY_OF_MONTH, 30)
    val end = calendar.timeInMillis - 1
    return start to end
}

private fun getThisMonthRange(calendar: Calendar): Pair<Long, Long> {
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val start = calendar.timeInMillis
    calendar.add(Calendar.MONTH, 1)
    val end = calendar.timeInMillis - 1
    return start to end
}

private fun getLastMonthRange(calendar: Calendar): Pair<Long, Long> {
    calendar.add(Calendar.MONTH, -1)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val start = calendar.timeInMillis
    calendar.add(Calendar.MONTH, 1)
    val end = calendar.timeInMillis - 1
    return start to end
}

private fun getThisYearRange(calendar: Calendar): Pair<Long, Long> {
    calendar.set(Calendar.DAY_OF_YEAR, 1)
    val start = calendar.timeInMillis
    calendar.add(Calendar.YEAR, 1)
    val end = calendar.timeInMillis - 1
    return start to end
}


sealed class DateFilterType {
    object Today : DateFilterType()
    object Yesterday : DateFilterType()
    object Last7Days : DateFilterType()
    object Last30Days : DateFilterType()
    object ThisMonth : DateFilterType()
    object LastMonth : DateFilterType()
    object ThisYear : DateFilterType()
}

