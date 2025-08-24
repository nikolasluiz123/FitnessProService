package br.com.fitnesspro.service.communication.gson

import br.com.fitnesspro.core.gson.defaultGSon
import br.com.fitnesspro.service.communication.gson.adapters.*
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IUserDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IApplicationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IDeviceDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutGroupDTO
import com.google.gson.Gson
import com.google.gson.GsonBuilder

fun GsonBuilder.defaultServiceGSon(): Gson {
    return this.defaultGSon {
        this.registerTypeAdapter(IApplicationDTO::class.java, ApplicationDTOTypeAdapter())
        this.registerTypeAdapter(IDeviceDTO::class.java, DeviceDTOTypeAdapter())
        this.registerTypeAdapter(IPersonDTO::class.java, PersonDTOTypeAdapter())
        this.registerTypeAdapter(ISchedulerDTO::class.java, SchedulerDTOTypeAdapter())
        this.registerTypeAdapter(IUserDTO::class.java, UserDTOTypeAdapter())
        this.registerTypeAdapter(IWorkoutDTO::class.java, WorkoutDTOTypeAdapter())
        this.registerTypeAdapter(IWorkoutGroupDTO::class.java, WorkoutGroupDTOTypeAdapter())
    }
}