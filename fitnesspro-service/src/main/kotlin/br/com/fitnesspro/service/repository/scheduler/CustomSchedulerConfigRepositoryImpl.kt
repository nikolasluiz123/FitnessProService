package br.com.fitnesspro.service.repository.scheduler

import br.com.fitnesspro.service.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.service.repository.common.query.Parameter
import br.com.fitnesspro.service.repository.common.query.getResultList
import br.com.fitnesspro.service.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerConfigDTO
import br.com.fitnesspro.shared.communication.query.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomSchedulerConfigRepositoryImpl: ICustomSchedulerConfigRepository {
    
    @PersistenceContext
    private lateinit var entityManager: EntityManager
    
    override fun getSchedulerConfigImport(
        filter: CommonImportFilter,
        pageInfos: ImportPageInfos
    ): List<SchedulerConfigDTO> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select config.id as id, ")
            add("        config.creation_date as creationDate, ")
            add("        config.update_date as updateDate, ")
            add("        config.active as active, ")
            add("        config.alarm as alarm, ")
            add("        config.notification as notification, ")
            add("        config.min_schedule_density as minScheduleDensity, ")
            add("        config.max_schedule_density as maxScheduleDensity, ")
            add("        config.person_id as personId ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from scheduler_config config ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            filter.lastUpdateDate?.let {
                add(" and config.update_date >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createNativeQuery(sql.toString())
        query.setParameters(params)
        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(SchedulerConfigDTO::class.java)

        return result
    }
}