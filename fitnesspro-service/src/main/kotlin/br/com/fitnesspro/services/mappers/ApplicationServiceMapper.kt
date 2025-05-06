package br.com.fitnesspro.services.mappers

import br.com.fitnesspro.models.serviceauth.Application
import br.com.fitnesspro.repository.serviceauth.IApplicationRepository
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ApplicationDTO
import org.springframework.stereotype.Service

@Service
class ApplicationServiceMapper(
    private val applicationRepository: IApplicationRepository
) {

    fun getApplication(dto: ApplicationDTO): Application {
        val model = dto.id?.let { applicationRepository.findById(it) }

        return when {
            dto.id == null -> {
                Application(
                    name = dto.name,
                    active = dto.active
                )
            }

            model?.isPresent == true -> {
                model.get().copy(
                    active = dto.active,
                    name = dto.name
                )
            }

            else -> {
                Application(
                    id = dto.id!!,
                    active = dto.active,
                    name = dto.name
                )
            }
        }
    }

    fun getApplicationDTO(model: Application): ApplicationDTO {
        return ApplicationDTO(
            id = model.id,
            name = model.name,
            active = model.active
        )
    }
}