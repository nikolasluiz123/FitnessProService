package br.com.fitnesspro.service.service.serviceauth

import br.com.fitnesspro.service.models.serviceauth.Application
import br.com.fitnesspro.service.repository.serviceauth.IApplicationRepository
import br.com.fitnesspro.service.repository.serviceauth.ICustomApplicationRepository
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ApplicationDTO
import br.com.fitnesspro.shared.communication.query.filter.ApplicationFilter
import org.springframework.stereotype.Service

@Service
class ApplicationService(
    private val applicationRepository: IApplicationRepository,
    private val customApplicationRepository: ICustomApplicationRepository
) {

    fun saveApplication(applicationDTO: ApplicationDTO) {
        applicationRepository.save(applicationDTO.toApplication())
    }

    fun getListApplications(filter: ApplicationFilter): List<ApplicationDTO> {
        return customApplicationRepository.getListApplication(filter).map { it.toApplicationDTO() }
    }

    private fun ApplicationDTO.toApplication(): Application {
        val model = id?.let { applicationRepository.findById(it) }

        return when {
            id == null -> {
                Application(
                    name = name,
                    active = active
                )
            }

            model?.isPresent ?: false -> {
                model!!.get().copy(
                    active = active,
                    name = name
                )
            }

            else -> {
                Application(
                    id = id,
                    active = active,
                    name = name
                )
            }
        }
    }

    private fun Application.toApplicationDTO(): ApplicationDTO {
        return ApplicationDTO(
            id = id,
            name = name,
            active = active
        )
    }
}