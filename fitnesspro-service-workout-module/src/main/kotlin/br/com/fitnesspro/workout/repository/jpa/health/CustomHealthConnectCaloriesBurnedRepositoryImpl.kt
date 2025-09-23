package br.com.fitnesspro.workout.repository.jpa.health

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.workout.health.HealthConnectCaloriesBurned
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomHealthConnectCaloriesBurnedRepositoryImpl : ICustomHealthConnectCaloriesBurnedRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getHealthConnectCaloriesBurnedImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos
    ): List<HealthConnectCaloriesBurned> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select calories ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${HealthConnectCaloriesBurned::class.java.name} calories ")
            add(" inner join calories.exerciseExecution execution ")
            add(" inner join execution.exercise exercise ")
            add(" inner join exercise.workoutGroup wGroup ")
            add(" inner join wGroup.workout workout ")
            add(" inner join calories.healthConnectMetadata meta ") // Join explícito
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where ( ")
            add("           workout.professionalPerson.id = :pPersonId ")
            add("           or workout.academyMemberPerson.id = :pPersonId ")
            add("       ) ")

            params.add(Parameter(name = "pPersonId", value = filter.personId))

            filter.lastUpdateDateMap[HealthConnectCaloriesBurned::class.simpleName!!]?.let {
                add(" and calories.updateDate > :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by calories.updateDate asc, calories.id asc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), HealthConnectCaloriesBurned::class.java)
        query.setParameters(params)
        query.maxResults = pageInfos.pageSize

        return query.getResultList(HealthConnectCaloriesBurned::class.java)
    }
}