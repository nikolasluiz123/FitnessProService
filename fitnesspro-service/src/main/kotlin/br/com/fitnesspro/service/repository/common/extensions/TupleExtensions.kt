package br.com.fitnesspro.service.repository.common.extensions

import jakarta.persistence.Tuple
import java.time.LocalDateTime

fun Tuple.getString(alias: String): String? = get(alias, String::class.java)

fun Tuple.getInt(alias: String): Int? = get(alias, Int::class.java)

fun Tuple.getLong(alias: String): Long? = get(alias, Long::class.java)

fun Tuple.getLocalDateTime(alias: String): LocalDateTime? = get(alias, LocalDateTime::class.java)

fun Tuple.getShortAsInt(alias: String): Int? = get(alias, Short::class.java)?.toInt()

fun <T : Enum<T>> Tuple.getEnum(alias: String, enumClass: Class<T>): T? {
    val position = getShortAsInt(alias)
    return enumClass.enumConstants?.firstOrNull { it.ordinal == position }
}