package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.workout.Exercise
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomExerciseRepositoryImpl: ICustomExerciseRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager
    override fun getExercisesImport(
        filter: WorkoutModuleImportationFilter,
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

            filter.lastUpdateDateMap[Exercise::class.simpleName!!]?.let {
                add(" and exercise.updateDate >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by exercise.updateDate asc, exercise.id asc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Exercise::class.java)
        query.setParameters(params)
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(Exercise::class.java)

        return result
    }
}