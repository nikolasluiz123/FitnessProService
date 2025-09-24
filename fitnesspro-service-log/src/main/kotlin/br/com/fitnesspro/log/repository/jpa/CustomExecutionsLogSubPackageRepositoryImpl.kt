package br.com.fitnesspro.log.repository.jpa

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.logs.ExecutionLogSubPackage
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
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

    override fun getCountInsertedItemsFromPackage(packageId: String): Int {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select sum(subPackage.insertedItemsCount) ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogSubPackage::class.java.name} subPackage ")
            add(" inner join subPackage.executionLogPackage logPackage ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where logPackage.id = :pLogPackageId ")
            queryParams.add(Parameter(name = "pLogPackageId", value = packageId))
        }

        val groupBy = StringJoiner(QR_NL).apply {
            add(" group by logPackage.id ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(groupBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Long::class.java)
        query.setParameters(queryParams)

        return query.singleResult?.toInt() ?: 0
    }

    override fun getCountUpdatedItemsFromPackage(packageId: String): Int {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select sum(subPackage.updatedItemsCount) ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogSubPackage::class.java.name} subPackage ")
            add(" inner join subPackage.executionLogPackage logPackage ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where logPackage.id = :pLogPackageId ")
            queryParams.add(Parameter(name = "pLogPackageId", value = packageId))
        }

        val groupBy = StringJoiner(QR_NL).apply {
            add(" group by logPackage.id ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(groupBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Long::class.java)
        query.setParameters(queryParams)

        return query.singleResult?.toInt() ?: 0
    }

    override fun getCountProcessedItemsFromPackage(packageId: String): Int {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select sum(subPackage.allItemsCount) ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogSubPackage::class.java.name} subPackage ")
            add(" inner join subPackage.executionLogPackage logPackage ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where logPackage.id = :pLogPackageId ")
            queryParams.add(Parameter(name = "pLogPackageId", value = packageId))
        }

        val groupBy = StringJoiner(QR_NL).apply {
            add(" group by logPackage.id ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(groupBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Long::class.java)
        query.setParameters(queryParams)

        return query.singleResult?.toInt() ?: 0
    }
}