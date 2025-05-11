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

fun LocalTime.format(pattern: EnumDateTimePatterns, zoneId: ZoneId? = null): String {
    return if (zoneId != null) {
        this.atDate(LocalDate.now(ZoneOffset.UTC))
            .atZone(ZoneOffset.UTC)
            .withZoneSameInstant(zoneId)
            .toLocalTime()
            .format(DateTimeFormatter.ofPattern(pattern.pattern))
    } else {
        this.format(DateTimeFormatter.ofPattern(pattern.pattern))
    }
}

fun LocalDateTime.format(pattern: EnumDateTimePatterns, zoneId: ZoneId? = null): String {
    val formatter = DateTimeFormatter.ofPattern(pattern.pattern)

    return if (zoneId != null) {
        this.atZone(ZoneOffset.UTC).withZoneSameInstant(zoneId).format(formatter)
    } else {
        this.format(formatter)
    }
}

fun OffsetDateTime.format(pattern: EnumDateTimePatterns, zoneId: ZoneId? = null): String {
    val formatter = DateTimeFormatter.ofPattern(pattern.pattern)

    return if (zoneId != null) {
        this.atZoneSameInstant(zoneId).format(formatter)
    } else {
        this.format(formatter)
    }
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

fun timeNow(zoneId: ZoneId? = ZoneId.systemDefault()): LocalTime {
    return LocalTime.now(zoneId)
}

fun dateNow(): LocalDate = LocalDate.now()

fun dateTimeNow(): LocalDateTime = LocalDateTime.now()

fun yearMonthNow(): YearMonth = YearMonth.now()

fun LocalDateTime.toEpochSeconds(): Long {
    return toEpochSecond(ZoneOffset.of(ZoneId.systemDefault().id))
}