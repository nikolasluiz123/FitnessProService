package br.com.fitnesspro.core.extensions

import br.com.fitnesspro.core.enums.EnumDateTimePatterns
import java.time.*
import java.time.format.DateTimeFormatter

fun String.parseToLocalDate(enumDateTimePatterns: EnumDateTimePatterns): LocalDate? {
    if (this.isEmpty()) return null

    return try {
        LocalDate.parse(this, DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
    } catch (ex: DateTimeException) {
        null
    }
}

fun String.parseToLocalTime(enumDateTimePatterns: EnumDateTimePatterns): LocalTime? {
    if (this.isEmpty()) return null

    return try {
        LocalTime.parse(this, DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
    } catch (ex: DateTimeException) {
        null
    }
}

fun String.parseToLocalDateTime(enumDateTimePatterns: EnumDateTimePatterns): LocalDateTime? {
    if (this.isEmpty()) return null

    return try {
        LocalDateTime.parse(this, DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
    } catch (ex: DateTimeException) {
        null
    }
}

fun String.formatFileDateToDateTime(): String? {
    return parseToLocalDateTime(EnumDateTimePatterns.DATE_TIME_FILE_NAME)?.format(EnumDateTimePatterns.DATE_TIME)
}

fun LocalDate.format(enumDateTimePatterns: EnumDateTimePatterns): String {
    return this.format(DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}

fun LocalTime.format(enumDateTimePatterns: EnumDateTimePatterns): String {
    return this.format(DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}

fun LocalDateTime.format(enumDateTimePatterns: EnumDateTimePatterns): String {
    return this.format(DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}

fun YearMonth.format(enumDateTimePatterns: EnumDateTimePatterns): String {
    return this.format(DateTimeFormatter.ofPattern(enumDateTimePatterns.pattern))
}

fun Duration.formatAsHMS(): String {
    val hours = this.toHours()
    val minutes = this.toMinutesPart()
    val seconds = this.toSecondsPart()

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun timeNow(): LocalTime = LocalTime.now()

fun dateNow(): LocalDate = LocalDate.now()

fun dateTimeNow(): LocalDateTime = LocalDateTime.now()

fun yearMonthNow(): YearMonth = YearMonth.now()

fun LocalDateTime.toEpochSeconds(): Long {
    return toEpochSecond(ZoneOffset.of(ZoneId.systemDefault().id))
}