package br.com.fitnesspro.repository.common.extensions

import jakarta.persistence.Tuple
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun Tuple.getString(alias: String): String? = get(alias, String::class.java)

fun Tuple.getInt(alias: String): Int? = get(alias, Int::class.java)

fun Tuple.getLong(alias: String): Long? = get(alias, Long::class.java)

fun Tuple.getLocalDateTimeFromTimeStamp(alias: String): LocalDateTime? {
    val ts: Timestamp? = this.get(alias, Timestamp::class.java)
    return ts?.toLocalDateTime()
}

fun Tuple.getLocalDateFromDate(alias: String): LocalDate? {
    val date: Date? = this.get(alias, Date::class.java)
    return date?.toLocalDate()
}

fun Tuple.getOffsetDateTime(alias: String): OffsetDateTime? {
    val timeStamp = this.get(alias, Timestamp::class.java)
    return timeStamp?.toInstant()?.atOffset(ZoneOffset.UTC)
}

fun Tuple.getShortAsInt(alias: String): Int? = get(alias, java.lang.Short::class.java)?.toInt()

fun Tuple.getBoolean(alias: String): Boolean {
    return this.get(alias, java.lang.Boolean::class.java)?.booleanValue()?: false
}

fun <T : Enum<T>> Tuple.getEnum(alias: String, enumClass: Class<T>): T? {
    val position = getShortAsInt(alias)
    return enumClass.enumConstants?.firstOrNull { it.ordinal == position }
}