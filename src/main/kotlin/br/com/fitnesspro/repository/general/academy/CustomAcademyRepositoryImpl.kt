package br.com.fitnesspro.repository.general.academy

import br.com.fitnesspro.models.general.PersonAcademyTime
import br.com.fitnesspro.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.repository.common.query.Parameter
import br.com.fitnesspro.repository.common.query.setParameters
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.time.DayOfWeek
import java.util.*

@Repository
class CustomAcademyRepositoryImpl: ICustomAcademyRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getPersonAcademyTimeList(personId: String, academyId: String?, dayOfWeek: DayOfWeek?): List<PersonAcademyTime> {
        val params = mutableListOf<Parameter>()
        params.add(Parameter("pPersonId", personId))

        val select = StringJoiner(QR_NL).apply {
            add(" select pat ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${PersonAcademyTime::class.java.name} pat ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where pat.active = true ")
            add(" and pat.person.id = :pPersonId ")

            academyId?.let {
                params.add(Parameter("pAcademyId", academyId))
                add(" and pat.academy.id = :pAcademyId ")
            }

            dayOfWeek?.let {
                params.add(Parameter("pDayOfWeek", dayOfWeek))
                add(" and pat.dayOfWeek = :pDayOfWeek ")
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), PersonAcademyTime::class.java)
        query.setParameters(params)

        return query.resultList
    }
}