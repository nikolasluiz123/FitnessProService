package br.com.fitnesspro.service.communication.gson.adapters

import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedSchedulerDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerDTO
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class SchedulerDTOTypeAdapter : TypeAdapter<ISchedulerDTO>() {
    override fun write(out: JsonWriter, value: ISchedulerDTO?) {
        val gson = GsonBuilder().defaultServiceGSon()
        gson.toJson(value as ValidatedSchedulerDTO?, ValidatedSchedulerDTO::class.java, out)
    }

    override fun read(reader: JsonReader): ISchedulerDTO {
        val gson = GsonBuilder().defaultServiceGSon()
        return gson.fromJson(reader, ValidatedSchedulerDTO::class.java)
    }
}