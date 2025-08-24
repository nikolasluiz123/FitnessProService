package br.com.fitnesspro.service.communication.gson.adapters

import br.com.fitnesspro.service.communication.dtos.workout.ValidatedWorkoutGroupDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutGroupDTO
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class WorkoutGroupDTOTypeAdapter : TypeAdapter<IWorkoutGroupDTO>() {
    override fun write(out: JsonWriter, value: IWorkoutGroupDTO?) {
        val gson = GsonBuilder().defaultServiceGSon()
        gson.toJson(value as ValidatedWorkoutGroupDTO?, ValidatedWorkoutGroupDTO::class.java, out)
    }

    override fun read(reader: JsonReader): IWorkoutGroupDTO {
        val gson = GsonBuilder().defaultServiceGSon()
        return gson.fromJson(reader, ValidatedWorkoutGroupDTO::class.java)
    }
}