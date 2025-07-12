package br.com.fitnesspro.repository.jpa.workout

import br.com.fitnesspro.models.workout.VideoExercise
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
class CustomVideoExerciseRepositoryImpl: ICustomVideoExerciseRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getVideoExercisesImport(
        filter: WorkoutModuleImportFilter,
        pageInfos: ImportPageInfos
    ): List<VideoExercise> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select videoExercise ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${VideoExercise::class.java.name} videoExercise ")
            add(" inner join videoExercise.exercise exercise ")
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
                add(" and videoExercise.updateDate >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), VideoExercise::class.java)
        query.setParameters(params)
        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(VideoExercise::class.java)

        return result
    }
}