package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.workout.Video
import br.com.fitnesspro.models.workout.VideoExercise
import br.com.fitnesspro.models.workout.VideoExerciseExecution
import br.com.fitnesspro.models.workout.VideoExercisePreDefinition
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomVideoRepositoryImpl: ICustomVideoRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getVideosImport(
        filter: WorkoutModuleImportFilter,
        pageInfos: ImportPageInfos
    ): List<Video> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select video ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${Video::class.java.name} video ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where ( ")
            add("           exists ( ")
            add("                       select 1 ")
            add("                       from ${VideoExercise::class.java.name} videoExercise ")
            add("                       inner join videoExercise.exercise e ")
            add("                       inner join e.workoutGroup group ")
            add("                       inner join group.workout workout ")
            add("                       where workout.professionalPerson.id = :pPersonId ")
            add("                       or workout.academyMemberPerson.id = :pPersonId ")
            add("                       and video.id = videoExercise.video.id ")
            add("                  ) ")
            add("           or ")
            add("           exists ( ")
            add("                       select 1 ")
            add("                       from ${VideoExerciseExecution::class.java.name} videoExecution ")
            add("                       inner join videoExecution.exerciseExecution exerciseExec ")
            add("                       inner join exerciseExec.exercise e ")
            add("                       inner join e.workoutGroup group ")
            add("                       inner join group.workout workout ")
            add("                       where workout.professionalPerson.id = :pPersonId ")
            add("                       or workout.academyMemberPerson.id = :pPersonId ")
            add("                       and video.id = videoExecution.video.id ")
            add("                  ) ")
            add("           or ")
            add("           exists ( ")
            add("                       select 1 ")
            add("                       from ${VideoExercisePreDefinition::class.java.name} videoPreDef ")
            add("                       inner join videoPreDef.exercisePreDefinition exercisePreDef ")
            add("                       where exercisePreDef.personalTrainerPerson.id = :pPersonId ")
            add("                       and video.id = videoPreDef.video.id ")
            add("                  ) ")
            add("       ) ")

            params.add(Parameter(name = "pPersonId", value = filter.personId))

            filter.lastUpdateDate?.let {
                add(" and video.updateDate >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Video::class.java)
        query.setParameters(params)
        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(Video::class.java)

        return result
    }
}