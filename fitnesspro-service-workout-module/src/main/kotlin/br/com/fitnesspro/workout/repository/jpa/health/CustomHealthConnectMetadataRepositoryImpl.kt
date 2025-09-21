package br.com.fitnesspro.workout.repository.jpa.health

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.workout.health.*
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomHealthConnectMetadataRepositoryImpl : ICustomHealthConnectMetadataRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getHealthConnectMetadataImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos
    ): List<HealthConnectMetadata> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select meta ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${HealthConnectMetadata::class.java.name} meta ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where ( ")
            add("           exists ( ")
            add("               select 1 ")
            add("               from ${HealthConnectSteps::class.java.name} steps ")
            add("               inner join steps.exerciseExecution execution ")
            add("               inner join execution.exercise exercise ")
            add("               inner join exercise.workoutGroup wGroup ")
            add("               inner join wGroup.workout workout ")
            add("               where steps.healthConnectMetadata.id = meta.id ")
            add("               and ( workout.professionalPerson.id = :pPersonId or workout.academyMemberPerson.id = :pPersonId ) ")
            add("           ) ")

            add("           or exists ( ")
            add("               select 1 ")
            add("               from ${HealthConnectCaloriesBurned::class.java.name} calories ")
            add("               inner join calories.exerciseExecution execution ")
            add("               inner join execution.exercise exercise ")
            add("               inner join exercise.workoutGroup wGroup ")
            add("               inner join wGroup.workout workout ")
            add("               where calories.healthConnectMetadata.id = meta.id ")
            add("               and ( workout.professionalPerson.id = :pPersonId or workout.academyMemberPerson.id = :pPersonId ) ")
            add("           ) ")

            add("           or exists ( ")
            add("               select 1 ")
            add("               from ${HealthConnectHeartRate::class.java.name} hr ")
            add("               inner join hr.exerciseExecution execution ")
            add("               inner join execution.exercise exercise ")
            add("               inner join exercise.workoutGroup wGroup ")
            add("               inner join wGroup.workout workout ")
            add("               where hr.healthConnectMetadata.id = meta.id ")
            add("               and ( workout.professionalPerson.id = :pPersonId or workout.academyMemberPerson.id = :pPersonId ) ")
            add("           ) ")

            add("           or exists ( ")
            add("               select 1 ")
            add("               from ${HealthConnectSleepSession::class.java.name} session ")
            add("               where session.healthConnectMetadata.id = meta.id ")
            add("               and exists ( ")
            add("                   select 1 ")
            add("                   from ${SleepSessionExerciseExecution::class.java.name} assoc ")
            add("                   inner join assoc.exerciseExecution execution ")
            add("                   inner join execution.exercise exercise ")
            add("                   inner join exercise.workoutGroup wGroup ")
            add("                   inner join wGroup.workout workout ")
            add("                   where assoc.healthConnectSleepSession.id = session.id ")
            add("                   and ( workout.professionalPerson.id = :pPersonId or workout.academyMemberPerson.id = :pPersonId ) ")
            add("               ) ")
            add("           ) ")

            add("       ) ")

            params.add(Parameter(name = "pPersonId", value = filter.personId))

            filter.lastUpdateDateMap[HealthConnectMetadata::class.simpleName!!]?.let {
                add(" and (meta.updateDate > :pLastUpdateDate OR (meta.updateDate = :pLastUpdateDate AND meta.id > :pCursorId)) ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
                params.add(Parameter(name = "pCursorId", value = pageInfos.cursorIdMap[HealthConnectMetadata::class.simpleName!!] ?: ""))
            }
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by meta.updateDate asc, meta.id asc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), HealthConnectMetadata::class.java)
        query.setParameters(params)
        query.maxResults = pageInfos.pageSize

        return query.getResultList(HealthConnectMetadata::class.java)
    }
}