package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.workout.ExercisePreDefinition
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomExercisePreDefinitionRepositoryImpl: ICustomExercisePreDefinitionRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getExercisesPreDefinitionImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<ExercisePreDefinition> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select exercisePreDefinition ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExercisePreDefinition::class.java.name} exercisePreDefinition ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where exercisePreDefinition.personalTrainerPerson.id = :pPersonId ")

            params.add(Parameter(name = "pPersonId", value = filter.personId))

            filter.lastUpdateDate?.let {
                add(" and exercisePreDefinition.updateDate >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), ExercisePreDefinition::class.java)
        query.setParameters(params)
        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        return query.getResultList(ExercisePreDefinition::class.java)
    }
}