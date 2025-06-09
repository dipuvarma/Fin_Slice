package com.dipuguide.finslice.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@SuppressLint("DefaultLocale")
fun formatPrice(input: Double): String {
    return try {
        String.format("%.2f", input)
    } catch (e: Exception) {
        "0.00"
    }
}



fun formatTimestampToDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
    sdf.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
    return sdf.format(Date(timestamp))
}
