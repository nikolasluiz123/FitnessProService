package br.com.fitnesspro.core.gson

import com.google.gson.*
import java.lang.reflect.Type
import java.time.ZoneOffset

class ZoneOffsetTypeAdapter : JsonSerializer<ZoneOffset>, JsonDeserializer<ZoneOffset> {

    override fun serialize(
        src: ZoneOffset?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.toString())
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ZoneOffset? {
        return json?.asString?.let { ZoneOffset.of(it) } 
    }
}