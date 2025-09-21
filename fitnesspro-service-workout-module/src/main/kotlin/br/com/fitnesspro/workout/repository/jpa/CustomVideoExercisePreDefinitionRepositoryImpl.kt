package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.workout.VideoExercisePreDefinition
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomVideoExercisePreDefinitionRepositoryImpl: ICustomVideoExercisePreDefinitionRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getVideoExercisesPreDefinitionImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos
    ): List<VideoExercisePreDefinition> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select videoPreDefinition ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${VideoExercisePreDefinition::class.java.name} videoPreDefinition ")
            add(" inner join videoPreDefinition.exercisePreDefinition exercisePreDefinition ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where exercisePreDefinition.personalTrainerPerson.id = :pPersonId ")

            params.add(Parameter(name = "pPersonId", value = filter.personId))

            filter.lastUpdateDateMap[VideoExercisePreDefinition::class.simpleName!!]?.let {
                add(" and (videoPreDefinition.updateDate > :pLastUpdateDate OR (videoPreDefinition.updateDate = :pLastUpdateDate AND videoPreDefinition.id > :pCursorId)) ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
                params.add(Parameter(name = "pCursorId", value = pageInfos.cursorIdMap[VideoExercisePreDefinition::class.simpleName!!] ?: ""))
            }
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by videoPreDefinition.updateDate asc, videoPreDefinition.id asc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), VideoExercisePreDefinition::class.java)
        query.setParameters(params)
        query.maxResults = pageInfos.pageSize

        return query.getResultList(VideoExercisePreDefinition::class.java)
    }
}