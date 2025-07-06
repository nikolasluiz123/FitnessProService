package br.com.fitnesspro.repository.workout

import br.com.fitnesspro.models.workout.WorkoutGroup
import br.com.fitnesspro.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.repository.common.query.Parameter
import br.com.fitnesspro.repository.common.query.getResultList
import br.com.fitnesspro.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomWorkoutGroupRepositoryImpl: ICustomWorkoutGroupRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getWorkoutGroupsImport(
        filter: WorkoutModuleImportFilter,
        pageInfos: ImportPageInfos
    ): List<WorkoutGroup> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select group ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${WorkoutGroup::class.java.name} group ")
            add(" inner join group.workout workout ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where ( ")
            add("           workout.professionalPerson.id = :pPersonId ")
            add("           or workout.academyMemberPerson.id = :pPersonId ")
            add("       ) ")

            params.add(Parameter(name = "pPersonId", value = filter.personId))

            filter.lastUpdateDate?.let {
                add(" and group.updateDate >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), WorkoutGroup::class.java)
        query.setParameters(params)
        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(WorkoutGroup::class.java)

        return result
    }

    override fun getListWorkoutGroupIdFromWorkout(workoutId: String): List<String> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select group.id as id ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${WorkoutGroup::class.java.name} group ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where group.workout.id = :pId ")
            params.add(Parameter(name = "pId", value = workoutId))
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), String::class.java)
        query.setParameters(params)

        return query.getResultList(String::class.java)
    }
}