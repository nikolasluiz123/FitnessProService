package br.com.fitnesspro.core.gson

import com.google.gson.*
import java.lang.reflect.Type
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class OffsetDateTimeTypeAdapter : JsonSerializer<OffsetDateTime>, JsonDeserializer<OffsetDateTime> {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    override fun serialize(
        src: OffsetDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.format(formatter))
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): OffsetDateTime {
        return OffsetDateTime.parse(json?.asString, formatter)
    }
}
