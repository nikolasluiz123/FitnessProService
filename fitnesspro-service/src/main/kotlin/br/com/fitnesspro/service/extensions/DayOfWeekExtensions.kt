package br.com.fitnesspro.service.extensions

import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

fun DayOfWeek.getFirstPartFullDisplayName(): String {
    val displayName = getDisplayName(TextStyle.FULL, Locale.getDefault())
    val firstPart = displayName.split("-")[0]

    return firstPart.replaceFirstChar(Char::uppercase)
}

fun DayOfWeek.getShortDisplayNameAllCaps(): String {
    return getDisplayName(TextStyle.SHORT, Locale.getDefault()).uppercase().replace(".", "")
}

fun DayOfWeek.getShortDisplayName(): String {
    val displayName = getDisplayName(TextStyle.SHORT, Locale.getDefault()).replace(".", "")

    return displayName.replaceFirstChar(Char::uppercase)
}