package br.com.fitnesspro.service.communication.gson.adapters

import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedApplicationDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IApplicationDTO
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class ApplicationDTOTypeAdapter : TypeAdapter<IApplicationDTO>() {
    override fun write(out: JsonWriter, value: IApplicationDTO?) {
        val gson = GsonBuilder().defaultServiceGSon()
        gson.toJson(value as ValidatedApplicationDTO?, ValidatedApplicationDTO::class.java, out)
    }

    override fun read(reader: JsonReader): IApplicationDTO {
        val gson = GsonBuilder().defaultServiceGSon()
        return gson.fromJson(reader, ValidatedApplicationDTO::class.java)
    }
}