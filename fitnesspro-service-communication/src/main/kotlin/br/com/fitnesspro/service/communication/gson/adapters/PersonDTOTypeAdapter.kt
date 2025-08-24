package br.com.fitnesspro.service.communication.gson.adapters

import br.com.fitnesspro.service.communication.dtos.general.ValidatedPersonDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonDTO
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class PersonDTOTypeAdapter : TypeAdapter<IPersonDTO>() {
    override fun write(out: JsonWriter, value: IPersonDTO?) {
        val gson = GsonBuilder().defaultServiceGSon()
        gson.toJson(value as ValidatedPersonDTO?, ValidatedPersonDTO::class.java, out)
    }

    override fun read(reader: JsonReader): IPersonDTO {
        val gson = GsonBuilder().defaultServiceGSon()
        return gson.fromJson(reader, ValidatedPersonDTO::class.java)
    }
}