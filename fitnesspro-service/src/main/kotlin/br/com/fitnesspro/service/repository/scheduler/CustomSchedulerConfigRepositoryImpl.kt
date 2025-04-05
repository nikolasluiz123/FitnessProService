package br.com.fitnesspro.service.repository.scheduler

import br.com.fitnesspro.service.models.scheduler.SchedulerConfig
import br.com.fitnesspro.service.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.service.repository.common.query.Parameter
import br.com.fitnesspro.service.repository.common.query.getResultList
import br.com.fitnesspro.service.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.CommonImportFilter
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
    ): List<SchedulerConfig> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select config.* ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${SchedulerConfig::class.java.name} config ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            filter.lastUpdateDate?.let {
                add(" and config.updateDate >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), SchedulerConfig::class.java)
        query.setParameters(params)
        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        return query.getResultList(SchedulerConfig::class.java)
    }
}