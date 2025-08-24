package br.com.fitnesspro.service.communication.gson.adapters

import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedDeviceDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IDeviceDTO
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class DeviceDTOTypeAdapter : TypeAdapter<IDeviceDTO>() {
    override fun write(out: JsonWriter, value: IDeviceDTO?) {
        val gson = GsonBuilder().defaultServiceGSon()
        gson.toJson(value as ValidatedDeviceDTO?, ValidatedDeviceDTO::class.java, out)
    }

    override fun read(reader: JsonReader): IDeviceDTO {
        val gson = GsonBuilder().defaultServiceGSon()
        return gson.fromJson(reader, ValidatedDeviceDTO::class.java)
    }
}