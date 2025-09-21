package br.com.fitnesspro.workout.repository.jpa.health

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.workout.health.HealthConnectSleepSession
import br.com.fitnesspro.models.workout.health.SleepSessionExerciseExecution
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomHealthConnectSleepSessionRepositoryImpl : ICustomHealthConnectSleepSessionRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getHealthConnectSleepSessionImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos,
        metadataIds: List<String>
    ): List<HealthConnectSleepSession> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select session ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${HealthConnectSleepSession::class.java.name} session ")
            add(" inner join session.healthConnectMetadata meta ")
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

            if (metadataIds.isNotEmpty()) {
                add(" and meta.id in (:pMetadataIds) ")
                params.add(Parameter(name = "pMetadataIds", value = metadataIds))
            }

            filter.lastUpdateDateMap[HealthConnectSleepSession::class.simpleName!!]?.let {
                add(" and (session.updateDate > :pLastUpdateDate OR (session.updateDate = :pLastUpdateDate AND session.id > :pCursorId)) ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
                params.add(Parameter(name = "pCursorId", value = pageInfos.cursorIdMap[HealthConnectSleepSession::class.simpleName!!] ?: ""))
            }
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by session.updateDate asc, session.id asc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), HealthConnectSleepSession::class.java)
        query.setParameters(params)
        query.maxResults = pageInfos.pageSize

        return query.getResultList(HealthConnectSleepSession::class.java)
    }
}