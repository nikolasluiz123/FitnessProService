package br.com.fitnesspro.repository.workout

import br.com.fitnesspro.models.workout.Exercise
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
class CustomExerciseRepositoryImpl: ICustomExerciseRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager
    override fun getExercisesImport(
        filter: WorkoutModuleImportFilter,
        pageInfos: ImportPageInfos
    ): List<Exercise> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select exercise ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${Exercise::class.java.name} exercise ")
            add(" inner join exercise.workoutGroup group ")
            add(" inner join group.workout workout ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where ( ")
            add("           workout.professionalPerson.id = :pPersonId ")
            add("           or workout.academyMemberPerson.id = :pPersonId ")
            add("       ) ")

            params.add(Parameter(name = "pPersonId", value = filter.personId))

            filter.lastUpdateDate?.let {
                add(" and exercise.updateDate >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Exercise::class.java)
        query.setParameters(params)
        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(Exercise::class.java)

        return result
    }

    override fun inactivateExercisesFromWorkoutGroup(workoutGroupId: String) {
        val params = mutableListOf<Parameter>()
        params.add(Parameter(name = "pWorkoutGroupId", value = workoutGroupId))

        val update = StringJoiner(QR_NL).apply {
            add(" update ${Exercise::class.java.name} exercise ")
            add(" set exercise.active = false, ")
            add("     exercise.updateDate = current_timestamp ")
            add(" where exercise.workoutGroup.id = :pWorkoutGroupId ")
        }

        val query = entityManager.createQuery(update.toString())
        query.setParameters(params)

        query.executeUpdate()
    }
}