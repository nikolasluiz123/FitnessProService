package br.com.fitnesspro.workout.repository.jpa.health

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.workout.health.SleepSessionExerciseExecution
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomSleepSessionExerciseExecutionRepositoryImpl : ICustomSleepSessionExerciseExecutionRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getSleepSessionExerciseExecutionImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos
    ): List<SleepSessionExerciseExecution> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select assoc ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${SleepSessionExerciseExecution::class.java.name} assoc ")
            add(" inner join assoc.exerciseExecution execution ")
            add(" inner join execution.exercise exercise ")
            add(" inner join exercise.workoutGroup wGroup ")
            add(" inner join wGroup.workout workout ")
            add(" inner join assoc.healthConnectSleepSession session ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where ( ")
            add("           workout.professionalPerson.id = :pPersonId ")
            add("           or workout.academyMemberPerson.id = :pPersonId ")
            add("       ) ")

            params.add(Parameter(name = "pPersonId", value = filter.personId))

            filter.lastUpdateDateMap[SleepSessionExerciseExecution::class.simpleName!!]?.let {
                add(" and assoc.updateDate > :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by assoc.updateDate asc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), SleepSessionExerciseExecution::class.java)
        query.setParameters(params)
        query.maxResults = pageInfos.pageSize

        return query.getResultList(SleepSessionExerciseExecution::class.java)
    }
}