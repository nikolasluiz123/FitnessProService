package br.com.fitnesspro.common.service.general

import br.com.fitnesspro.authentication.repository.jpa.ICustomPersonAcademyTimeRepository
import br.com.fitnesspro.common.repository.auditable.general.IAcademyRepository
import br.com.fitnesspro.common.repository.auditable.general.IPersonAcademyTimeRepository
import br.com.fitnesspro.common.repository.jpa.general.academy.ICustomAcademyRepository
import br.com.fitnesspro.common.service.mappers.AcademyServiceMapper
import br.com.fitnesspro.service.communication.dtos.general.ValidatedAcademyDTO
import br.com.fitnesspro.service.communication.dtos.general.ValidatedPersonAcademyTimeDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IAcademyDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonAcademyTimeDTO
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.AcademyFilter
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import org.springframework.stereotype.Service

@Service
class AcademyService(
    private val personAcademyTimeRepository: IPersonAcademyTimeRepository,
    private val customPersonAcademyTimeRepository: ICustomPersonAcademyTimeRepository,
    private val academyRepository: IAcademyRepository,
    private val customAcademyRepository: ICustomAcademyRepository,
    private val academyServiceMapper: AcademyServiceMapper,
) {
    fun saveAcademy(validatedAcademyDTO: ValidatedAcademyDTO) {
        val academy = academyServiceMapper.getAcademy(validatedAcademyDTO)
        academyRepository.save(academy)

        validatedAcademyDTO.id = academy.id
    }

    fun saveAcademyBatch(list: List<IAcademyDTO>) {
        val academyList = list.map(academyServiceMapper::getAcademy)
        academyRepository.saveAll(academyList)
    }

    fun savePersonAcademyTimeBatch(list: List<IPersonAcademyTimeDTO>) {
        val personAcademyTimeList = list.map(academyServiceMapper::getPersonAcademyTime)
        personAcademyTimeRepository.saveAll(personAcademyTimeList)
    }

    fun getPersonAcademyTimesImport(
        filter: CommonImportFilter,
        pageInfos: ImportPageInfos,
        personIds: List<String>,
        academyIds: List<String>
    ): List<ValidatedPersonAcademyTimeDTO> {
        return customPersonAcademyTimeRepository.getPersonAcademyTimesImport(filter, pageInfos, personIds, academyIds)
            .map(academyServiceMapper::getValidatedPersonAcademyTimeDTO)
    }

    fun getAcademiesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<ValidatedAcademyDTO> {
        return customAcademyRepository.getAcademiesImport(filter, pageInfos).map(academyServiceMapper::getValidatedAcademyDTO)
    }

    fun getListAcademy(filter: AcademyFilter, pageInfos: CommonPageInfos): List<ValidatedAcademyDTO> {
        return customAcademyRepository.getListAcademy(filter, pageInfos).map(academyServiceMapper::getValidatedAcademyDTO)
    }

    fun getCountListAcademy(filter: AcademyFilter): Int {
        return customAcademyRepository.getCountListAcademy(filter)
    }

}