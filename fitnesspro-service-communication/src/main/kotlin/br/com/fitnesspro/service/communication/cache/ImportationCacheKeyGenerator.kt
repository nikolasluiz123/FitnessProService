package br.com.fitnesspro.service.communication.cache

import br.com.fitnesspro.core.enums.EnumDateTimePatterns.DATE_TIME_CACHE_KEY
import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.core.extensions.format
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.paging.SyncPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.*
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component("importationKeyGenerator")
class ImportationCacheKeyGenerator : KeyGenerator {

    override fun generate(target: Any, method: Method, vararg params: Any): Any {
        val filter = getImportFilter(params)
        val pageInfos = getPageInfos(params)

        val keyBuilder = StringBuilder()

        keyBuilder.append(String.format(KEY_SIZE, pageInfos.pageSize))
        keyBuilder.append(getDateKeyPart(method, filter))
        keyBuilder.append(getFilterSpecificKeyParts(filter))

        return keyBuilder.toString()
    }

    /**
     * Extrai o argumento [CommonImportFilter] da lista de parâmetros do método.
     *
     * @param params Os parâmetros recebidos pela função `generate`.
     * @return A instância de [CommonImportFilter] encontrada.
     * @throws IllegalStateException Se nenhum [CommonImportFilter] for encontrado.
     */
    private fun getImportFilter(params: Array<out Any>): CommonImportFilter {
        return params.firstOrNull { it is CommonImportFilter } as? CommonImportFilter
            ?: throw IllegalStateException(ERROR_NO_FILTER)
    }

    /**
     * Extrai o argumento [PageInfos] da lista de parâmetros do método.
     *
     * @param params Os parâmetros recebidos pela função `generate`.
     * @return A instância de [PageInfos] encontrada.
     * @throws IllegalStateException Se nenhum [PageInfos] for encontrado.
     */
    private fun getPageInfos(params: Array<out Any>): SyncPageInfos {
        return params.firstOrNull { it is SyncPageInfos } as? SyncPageInfos
            ?: throw IllegalStateException(ERROR_NO_PAGE_INFOS)
    }

    /**
     * Gera a parte da chave de cache referente à data de última atualização (`-date:...`).
     *
     * Esta função:
     * 1. Lê a anotação `@ImportationEntity` do método para descobrir o `entitySimpleName`.
     * 2. Usa o `entitySimpleName` para buscar a data correspondente no `lastUpdateDateMap` do filtro.
     * 3. Formata a data (ou a data/hora atual, se nula) e a retorna no formato `KEY_DATE`.
     *
     * @param method O método cacheável que está sendo interceptado.
     * @param filter O filtro de importação contendo o `lastUpdateDateMap`.
     * @return A string formatada da chave de data (ex: `-date:2023-01-01 10:00:00`).
     * @throws IllegalStateException Se a anotação `@ImportationEntity` não for encontrada no método.
     */
    private fun getDateKeyPart(method: Method, filter: CommonImportFilter): String {
        val importationEntityAnnotation = method.getAnnotation(ImportationEntity::class.java)
            ?: throw IllegalStateException(String.format(ERROR_NO_ANNOTATION_FORMAT, method.name))

        val entitySimpleName = importationEntityAnnotation.entitySimpleName
        val lastUpdateDate = filter.lastUpdateDateMap[entitySimpleName]
        val dateKey = lastUpdateDate?.format(DATE_TIME_CACHE_KEY) ?: dateTimeNow().format(DATE_TIME_CACHE_KEY)

        return String.format(KEY_DATE, dateKey)
    }

    /**
     * Gera as partes adicionais e específicas da chave de cache com base no tipo concreto do filtro.
     *
     * @param filter O filtro de importação.
     * @return Uma string contendo as partes adicionais da chave (ex: `-person:123-context:ABC`).
     */
    private fun getFilterSpecificKeyParts(filter: CommonImportFilter): String {
        val filterKeyBuilder = StringBuilder()

        when (filter) {
            is WorkoutModuleImportationFilter -> {
                filterKeyBuilder.append(String.format(KEY_PERSON, filter.personId))
            }

            is ReportImportFilter -> {
                filterKeyBuilder.append(String.format(KEY_PERSON, filter.personId))
                filterKeyBuilder.append(String.format(KEY_CONTEXT, filter.reportContext))
            }

            is SchedulerModuleImportationFilter -> {
                filterKeyBuilder.append(String.format(KEY_PERSON, filter.personId))
                filterKeyBuilder.append(String.format(KEY_CONTEXT, filter.reportContext))
            }

            is SchedulerReportImportFilter -> {
                filterKeyBuilder.append(String.format(KEY_PERSON, filter.personId))
            }
        }

        return filterKeyBuilder.toString()
    }

    private companion object {
        const val KEY_SIZE = "-size:%d"
        const val KEY_DATE = "-date:%s"
        const val KEY_PERSON = "-person:%s"
        const val KEY_CONTEXT = "-context:%s"

        const val ERROR_NO_FILTER = "Método cacheável precisa de um argumento CommonImportFilter"
        const val ERROR_NO_PAGE_INFOS = "Método cacheável precisa de um argumento PageInfos"
        const val ERROR_NO_ANNOTATION_FORMAT = "O método %s está usando 'importationKeyGenerator' mas não possui a anotação @ImportationEntity."
    }
}