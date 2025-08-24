package br.com.fitnesspro.core.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime

fun GsonBuilder.defaultGSon(registerTypes: GsonBuilder.() -> Unit = {}): Gson {
    val builder = this.registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
        .registerTypeAdapter(LocalTime::class.java, LocalTimeTypeAdapter())
        .registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeTypeAdapter())

    builder.registerTypes()

    return builder.create()
}