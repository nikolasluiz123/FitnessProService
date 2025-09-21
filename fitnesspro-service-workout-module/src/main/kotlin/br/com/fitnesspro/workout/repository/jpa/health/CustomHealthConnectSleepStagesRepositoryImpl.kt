package br.com.fitnesspro.workout.repository.jpa.health

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.workout.health.HealthConnectSleepStages
import br.com.fitnesspro.models.workout.health.SleepSessionExerciseExecution
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomHealthConnectSleepStagesRepositoryImpl : ICustomHealthConnectSleepStagesRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getHealthConnectSleepStagesImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos,
        sleepSessionIds: List<String>
    ): List<HealthConnectSleepStages> {
        if (sleepSessionIds.isEmpty()) return emptyList()

        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select stage ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${HealthConnectSleepStages::class.java.name} stage ")
            add(" inner join stage.healthConnectSleepSession session ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where exists ( ")
            add("    select 1 ")
            add("    from ${SleepSessionExerciseExecution::class.java.name} assoc ")
            add("    inner join assoc.exerciseExecution execution ")
            add("    inner join execution.exercise exercise ")
            add("    inner join exercise.workoutGroup wGroup ")
            add("    inner join wGroup.workout workout ")
            add("    where assoc.healthConnectSleepSession.id = session.id ")
            add("    and ( ")
            add("           workout.professionalPerson.id = :pPersonId ")
            add("           or workout.academyMemberPerson.id = :pPersonId ")
            add("        ) ")
            add(" ) ")

            params.add(Parameter(name = "pPersonId", value = filter.personId))

            add(" and session.id in (:pSleepSessionIds) ")
            params.add(Parameter(name = "pSleepSessionIds", value = sleepSessionIds))

            filter.lastUpdateDateMap[HealthConnectSleepStages::class.simpleName!!]?.let {
                add(" and (stage.updateDate > :pLastUpdateDate OR (stage.updateDate = :pLastUpdateDate AND stage.id > :pCursorId)) ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
                params.add(Parameter(name = "pCursorId", value = pageInfos.cursorIdMap[HealthConnectSleepStages::class.simpleName!!] ?: ""))
            }
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by stage.updateDate asc, stage.id asc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), HealthConnectSleepStages::class.java)
        query.setParameters(params)
        query.maxResults = pageInfos.pageSize

        return query.getResultList(HealthConnectSleepStages::class.java)
    }
}