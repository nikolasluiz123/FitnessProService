package br.com.fitnesspro.workout.repository.jpa.health

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.workout.health.HealthConnectHeartRate
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomHealthConnectHeartRateRepositoryImpl : ICustomHealthConnectHeartRateRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getHealthConnectHeartRateImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos
    ): List<HealthConnectHeartRate> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select hr ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${HealthConnectHeartRate::class.java.name} hr ")
            add(" inner join hr.exerciseExecution execution ")
            add(" inner join execution.exercise exercise ")
            add(" inner join exercise.workoutGroup wGroup ")
            add(" inner join wGroup.workout workout ")
            add(" inner join hr.healthConnectMetadata meta ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where ( ")
            add("           workout.professionalPerson.id = :pPersonId ")
            add("           or workout.academyMemberPerson.id = :pPersonId ")
            add("       ) ")

            params.add(Parameter(name = "pPersonId", value = filter.personId))

            filter.lastUpdateDateMap[HealthConnectHeartRate::class.simpleName!!]?.let {
                add(" and hr.updateDate > :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by hr.updateDate asc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), HealthConnectHeartRate::class.java)
        query.setParameters(params)
        query.maxResults = pageInfos.pageSize

        return query.getResultList(HealthConnectHeartRate::class.java)
    }
}