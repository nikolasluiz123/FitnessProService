package br.com.fitnesspro.jpa.extensions

import jakarta.persistence.Tuple
import java.lang.Short
import java.sql.Date
import java.sql.Timestamp
import java.time.*
import kotlin.Boolean
import kotlin.Enum
import kotlin.Int
import kotlin.Long
import kotlin.String

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
    val instant = this.get(alias, Instant::class.java)
    return instant?.atOffset(ZoneOffset.UTC)
}

fun Tuple.getShortAsInt(alias: String): Int? = get(alias, Short::class.java)?.toInt()

fun Tuple.getBoolean(alias: String): Boolean {
    return this.get(alias, java.lang.Boolean::class.java)?.booleanValue()?: false
}

fun <T : Enum<T>> Tuple.getEnum(alias: String, enumClass: Class<T>): T? {
    val position = getShortAsInt(alias)
    return enumClass.enumConstants?.firstOrNull { it.ordinal == position }
}