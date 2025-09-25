package br.com.fitnesspro.log.repository.jpa.subpackage

import br.com.fitnesspro.jpa.extensions.getLong
import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.logs.ExecutionLogSubPackage
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogSubPackageFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Tuple
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomExecutionsLogSubPackageRepositoryImpl: ICustomExecutionsLogSubPackageRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun findSubPackagesByPackageId(packageId: String): List<ExecutionLogSubPackage> {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select subPackage ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogSubPackage::class.java.name} subPackage ")
            add(" inner join subPackage.executionLogPackage logPackage ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where logPackage.id = :pLogPackageId ")
            queryParams.add(Parameter(name = "pLogPackageId", value = packageId))
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), ExecutionLogSubPackage::class.java)
        query.setParameters(queryParams)

        return query.resultList
    }

    override fun calculateSubPackageInformation(logId: String?, packageId: String?): SubPackageCalculatedInformation? {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select sum(subPackage.insertedItemsCount) as insertedItemsCount, ")
            add("        sum(subPackage.updatedItemsCount) as updatedItemsCount, ")
            add("        sum(subPackage.allItemsCount) as allItemsCount, ")
            add("        sum(subPackage.kbSize) as kbSize ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogSubPackage::class.java.name} subPackage ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where 1=1 ")

            packageId?.let {
                add(" and subPackage.executionLogPackage.id = :pLogPackageId ")
                queryParams.add(Parameter(name = "pLogPackageId", value = packageId))
            }

            logId?.let {
                add(" and subPackage.executionLogPackage.executionLog.id = :pLogId ")
                queryParams.add(Parameter(name = "pLogId", value = logId))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Tuple::class.java)
        query.setParameters(queryParams)

        return query.getResultList(Tuple::class.java).firstOrNull()?.let {  tuple ->
            SubPackageCalculatedInformation(
                insertedItemsCount = tuple.getLong("insertedItemsCount") ?: 0,
                updatedItemsCount = tuple.getLong("updatedItemsCount") ?: 0,
                allItemsCount = tuple.getLong("allItemsCount") ?: 0,
                kbSize = tuple.getLong("kbSize") ?: 0
            )
        }
    }

    override fun getListExecutionLogSubPackage(filter: ExecutionLogSubPackageFilter, pageInfos: PageInfos): List<ExecutionLogSubPackage> {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select subPackage ")
        }

        val from = getFromListExecutionLogSubPackage()
        val where = getWhereListExecutionLogSubPackage(filter, queryParams)
        val orderBy = getOrderByListExecutionLogSubPackage(filter)

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), ExecutionLogSubPackage::class.java)
        query.setParameters(queryParams)

        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        return query.resultList
    }

    override fun getCountListExecutionLogSubPackage(filter: ExecutionLogSubPackageFilter): Int {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select count(subPackage.id) ")
        }

        val from = getFromListExecutionLogSubPackage()
        val where = getWhereListExecutionLogSubPackage(filter, queryParams)

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Long::class.java)
        query.setParameters(queryParams)

        return query.singleResult.toInt()
    }

    private fun getFromListExecutionLogSubPackage(): StringJoiner {
        return StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogSubPackage::class.java.name} subPackage ")
        }
    }

    private fun getWhereListExecutionLogSubPackage(filter: ExecutionLogSubPackageFilter, queryParams: MutableList<Parameter>): StringJoiner {
        return StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            filter.executionLogPackageId?.let {
                add(" and subPackage.executionLogPackage.id = :executionLogPackageId ")
                queryParams.add(Parameter(name = "executionLogPackageId", value = it))
            }

            filter.entityName?.let {
                add(" and lower(subPackage.entityName) like lower(:entityName) ")
                queryParams.add(Parameter(name = "entityName", value = "%$it%"))
            }
        }
    }

    private fun getOrderByListExecutionLogSubPackage(filter: ExecutionLogSubPackageFilter): StringJoiner {
        return StringJoiner(QR_NL).apply {
            if (filter.sort.isEmpty()) {
                add(" order by subPackage.entityName asc ")
            } else {
                add(" order by ")
                filter.sort.forEachIndexed { index, sort ->
                    val sortField = sort.field.fieldName
                    val order = if (sort.asc) "asc" else "desc"
                    val comma = if (index == filter.sort.lastIndex) "" else ", "
                    add(" subPackage.$sortField $order$comma ")
                }
            }
        }
    }
}