package com.dipuguide.finslice.utils

fun formatNumberToIndianStyle(amount: Double): String {
    val format = java.text.NumberFormat.getNumberInstance(java.util.Locale("en", "IN"))
    format.maximumFractionDigits = 0
    format.minimumFractionDigits = 0
    return format.format(amount)
}
