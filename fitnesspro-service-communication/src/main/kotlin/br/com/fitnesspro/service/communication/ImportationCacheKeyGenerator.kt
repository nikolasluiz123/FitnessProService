package br.com.fitnesspro.service.communication // Ou qualquer pacote de configuração

import br.com.fitnesspro.core.enums.EnumDateTimePatterns.DATE_TIME_SQLITE
import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.core.extensions.format
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.*
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component("importationKeyGenerator")
class ImportationCacheKeyGenerator : KeyGenerator {

    override fun generate(target: Any, method: Method, vararg params: Any): Any {
        val filter = params.firstOrNull { it is CommonImportFilter } as? CommonImportFilter
            ?: throw IllegalStateException("Método cacheável precisa de um argumento CommonImportFilter")

        val pageInfos = params.firstOrNull { it is PageInfos } as? PageInfos
            ?: throw IllegalStateException("Método cacheável precisa de um argumento PageInfos")

        val keyBuilder = StringBuilder()

        keyBuilder.append("page:${pageInfos.pageNumber}-size:${pageInfos.pageSize}")

        val dateKey = filter.lastUpdateDate?.format(DATE_TIME_SQLITE) ?: dateTimeNow().format(DATE_TIME_SQLITE)
        keyBuilder.append("-date:$dateKey")

        when (filter) {
            is WorkoutModuleImportationFilter -> {
                keyBuilder.append("-person:${filter.personId}")
            }

            is ReportImportFilter -> {
                keyBuilder.append("-person:${filter.personId}")
                keyBuilder.append("-context:${filter.reportContext}")
            }

            is SchedulerModuleImportationFilter -> {
                keyBuilder.append("-person:${filter.personId}")
                keyBuilder.append("-context:${filter.reportContext}")
            }

            is SchedulerReportImportFilter -> {
                keyBuilder.append("-person:${filter.personId}")
            }
        }

        return keyBuilder.toString()
    }
}