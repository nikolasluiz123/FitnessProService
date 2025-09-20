package br.com.fitnesspro.core.gson

import com.google.gson.*
import java.lang.reflect.Type
import java.time.Instant

class InstantTypeAdapter : JsonSerializer<Instant>, JsonDeserializer<Instant> {

    override fun serialize(
        src: Instant?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.toString())
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Instant? {
        return json?.asString?.let { Instant.parse(it) }
    }
}
