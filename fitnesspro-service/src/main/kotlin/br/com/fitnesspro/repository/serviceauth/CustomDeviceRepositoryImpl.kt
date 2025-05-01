package br.com.fitnesspro.repository.serviceauth

import br.com.fitnesspro.models.serviceauth.Device
import br.com.fitnesspro.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.repository.common.query.Parameter
import br.com.fitnesspro.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.DeviceFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*
import kotlin.jvm.java

@Repository
class CustomDeviceRepositoryImpl: ICustomDeviceRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getListDevice(filter: DeviceFilter, pageInfos: PageInfos): List<Device> {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select d ")
        }

        val from = getFromListDevice()
        val where = getWhereListDevice(filter, queryParams)

        val orderBy = StringJoiner(QR_NL).apply {
            if (filter.sort.isEmpty()) {
                add(" order by d.model, d.creationDate desc ")
            } else {
                add(" order by ")

                filter.sort.forEachIndexed { index, sort ->
                    val sortField = sort.field.fieldName
                    val order = if (sort.asc) "asc" else "desc"
                    val comma = if (index == filter.sort.lastIndex) "" else ", "

                    add(" d.$sortField $order$comma ")
                }
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Device::class.java)
        query.setParameters(queryParams)

        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        return query.resultList
    }

    private fun getFromListDevice(): StringJoiner {
        return StringJoiner(QR_NL).apply {
            add(" from ${Device::class.java.name} d ")
        }
    }

    private fun getWhereListDevice(filter: DeviceFilter, queryParams: MutableList<Parameter>): StringJoiner {
        return StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            filter.id?.let {
                add(" and d.id = :pId ")
                queryParams.add(Parameter(name = "pId", value = it))
            }

            filter.model?.let {
                add(" and lower(d.model) like lower(:pModel) ")
                queryParams.add(Parameter(name = "pModel", value = "%$it%"))
            }

            filter.brand?.let {
                add(" and lower(d.brand) like lower(:pBrand) ")
                queryParams.add(Parameter(name = "pBrand", value = "%$it%"))
            }

            filter.androidVersion?.let {
                add(" and d.androidVersion = :pAndroidVersion ")
                queryParams.add(Parameter(name = "pAndroidVersion", value = it))
            }

            filter.creationDate?.let {
                add(" and d.creationDate between :pCreationDateStart and :pCreationDateEnd ")
                queryParams.add(Parameter(name = "pCreationDateStart", value = it.first))
                queryParams.add(Parameter(name = "pCreationDateEnd", value = it.second))
            }

            filter.updateDate?.let {
                add(" and d.updateDate between :pUpdateDateStart and :pUpdateDateEnd ")
                queryParams.add(Parameter(name = "pUpdateDateStart", value = it.first))
                queryParams.add(Parameter(name = "pUpdateDateEnd", value = it.second))
            }
        }
    }

    override fun getCountListDevice(filter: DeviceFilter): Int {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select count(d.id) ")
        }

        val from = getFromListDevice()

        val where = getWhereListDevice(filter, queryParams)

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Long::class.java)
        query.setParameters(queryParams)

        return query.singleResult.toInt()
    }
}