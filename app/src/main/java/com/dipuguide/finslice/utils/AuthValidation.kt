package com.dipuguide.finslice.utils

import androidx.compose.ui.graphics.Color

fun getValidPassword(password: String): List<String> {

    val errors = mutableListOf<String>()
    val specialChars = """^$*.[]{}()?"!@#%&/\\,><':;|_~"""

    if (password.length < 6) errors.add("Minimum 6 characters required")
    if (password.length > 4096) errors.add("Password too long")
    if (!password.any { it.isLowerCase() }) errors.add("At least one lowercase letter required")
    if (!password.any { it.isUpperCase() }) errors.add("At least one uppercase letter required")
    if (!password.any { it.isDigit() }) errors.add("At least one number required")
    if (!password.any { it in specialChars }) errors.add("At least one special character required")

    return errors
}

fun getPasswordStrength(password: String): PasswordStrength {
    var score = 0
    if (password.length >= 6) score++
    if (password.length >= 12) score++
    if (password.any { it.isLowerCase() }) score++
    if (password.any { it.isUpperCase() }) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { it in """^$*.[]{}()?"!@#%&/\\,><':;|_~""" }) score++

    return when {
        score <= 2 -> PasswordStrength.WEAK
        score <= 4 -> PasswordStrength.MEDIUM
        else -> PasswordStrength.STRONG
    }
}

enum class PasswordStrength(val label: String, val color: Color, val progress: Float) {
    WEAK("Weak", Color.Red, 0.33f),
    MEDIUM("Medium", Color(0xFFFFA500), 0.66f),
    STRONG("Hard", Color(0xFF4CAF50), 1.0f)
}