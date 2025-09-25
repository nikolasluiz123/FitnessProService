package br.com.fitnesspro.log.repository.jpa.subpackage

import br.com.fitnesspro.jpa.extensions.getLong
import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.logs.ExecutionLogSubPackage
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
}