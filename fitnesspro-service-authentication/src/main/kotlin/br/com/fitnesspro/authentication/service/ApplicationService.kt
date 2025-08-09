package br.com.fitnesspro.authentication.service

import br.com.fitnesspro.authentication.repository.jpa.IApplicationRepository
import br.com.fitnesspro.authentication.service.mappers.ApplicationServiceMapper
import br.com.fitnesspro.authentication.repository.jpa.ICustomApplicationRepository
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ApplicationDTO
import org.springframework.stereotype.Service

@Service
class ApplicationService(
    private val applicationRepository: IApplicationRepository,
    private val customApplicationRepository: ICustomApplicationRepository,
    private val applicationServiceMapper: ApplicationServiceMapper
) {

    fun saveApplication(applicationDTO: ApplicationDTO) {
        val application = applicationServiceMapper.getApplication(applicationDTO)

        applicationRepository.save(application)
        applicationDTO.id = application.id
    }

    fun getListApplications(): List<ApplicationDTO> {
        return customApplicationRepository.getListApplication().map(applicationServiceMapper::getApplicationDTO)
    }
}