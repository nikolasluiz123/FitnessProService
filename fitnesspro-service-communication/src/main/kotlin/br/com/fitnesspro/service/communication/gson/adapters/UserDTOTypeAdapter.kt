package br.com.fitnesspro.service.communication.gson.adapters

import br.com.fitnesspro.service.communication.dtos.general.ValidatedUserDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IUserDTO
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class UserDTOTypeAdapter : TypeAdapter<IUserDTO>() {
    override fun write(out: JsonWriter, value: IUserDTO?) {
        val gson = GsonBuilder().defaultServiceGSon()
        gson.toJson(value as ValidatedUserDTO?, ValidatedUserDTO::class.java, out)
    }

    override fun read(reader: JsonReader): IUserDTO {
        val gson = GsonBuilder().defaultServiceGSon()
        return gson.fromJson(reader, ValidatedUserDTO::class.java)
    }
}