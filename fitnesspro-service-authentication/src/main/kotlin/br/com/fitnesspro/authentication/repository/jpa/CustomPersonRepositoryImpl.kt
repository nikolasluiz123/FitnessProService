package br.com.fitnesspro.authentication.repository.jpa

import br.com.fitnesspro.jpa.extensions.*
import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.service.communication.dtos.general.ValidatedPersonDTO
import br.com.fitnesspro.service.communication.dtos.general.ValidatedUserDTO
import br.com.fitnesspro.shared.communication.enums.general.EnumUserType
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.PersonFilter
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Tuple
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomPersonRepositoryImpl: ICustomPersonRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getPersonsImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<Person> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select p ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${Person::class.java.name} p ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where p.user.type != :pAdministrator ")

            params.add(Parameter(name = "pAdministrator", value = EnumUserType.ADMINISTRATOR))

            filter.lastUpdateDateMap[Person::class.simpleName!!]?.let {
                add(" and (p.updateDate > :pLastUpdateDate OR (p.updateDate = :pLastUpdateDate AND p.id > :pCursorId)) ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
                params.add(Parameter(name = "pCursorId", value = pageInfos.cursorIdMap[Person::class.simpleName!!] ?: ""))
            }
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by p.updateDate asc, p.id asc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Person::class.java)
        query.setParameters(params)
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(Person::class.java)

        return result
    }

    override fun getUsersImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<User> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select u ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${User::class.java.name} u ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where u.type != :pAdministrator ")

            params.add(Parameter(name = "pAdministrator", value = EnumUserType.ADMINISTRATOR))

            filter.lastUpdateDateMap[User::class.simpleName!!]?.let {
                add(" and (u.updateDate > :pLastUpdateDate OR (u.updateDate = :pLastUpdateDate AND u.id > :pCursorId)) ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
                params.add(Parameter(name = "pCursorId", value = pageInfos.cursorIdMap[User::class.simpleName!!] ?: ""))
            }
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by u.updateDate asc, u.id asc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), User::class.java)
        query.setParameters(params)
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(User::class.java)

        return result
    }

    override fun findByEmail(email: String, password: String?): Person? {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select person ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${Person::class.java.name} person ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where person.user.email = :pEmail ")
            params.add(Parameter("pEmail", email))

            password?.let {
                add(" and person.user.password = :pPassword ")
                params.add(Parameter("pPassword", it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Person::class.java)
        query.setParameters(params)

        return try {
            query.singleResult
        } catch (ex: NoResultException) {
            null
        }
    }

    override fun getListPersons(filter: PersonFilter, pageInfos: CommonPageInfos): List<ValidatedPersonDTO> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select p.id as id, ")
            add(" p.creation_date as creationDate, ")
            add(" p.update_date as updateDate, ")
            add(" p.active as active, ")
            add(" p.name as name, ")
            add(" p.birth_date as birthDate, ")
            add(" p.phone as phone, ")
            add(" u.id as userId, ")
            add(" u.creation_date as userCreationDate, ")
            add(" u.update_date as userUpdateDate, ")
            add(" u.active as userActive, ")
            add(" u.email as userEmail, ")
            add(" u.password as userPassword, ")
            add(" u.type as userType ")
        }

        val from = getFromListPersons()

        val where = getWhereListPersons(filter, params)

        val orderBy = StringJoiner(QR_NL).apply {
            if (filter.sort.isEmpty()) {
                add(" order by p.name, p.creation_date desc ")
            } else {
                add(" order by ")

                filter.sort.forEachIndexed { index, sort ->
                    val sortField = sort.field.getDBColumn()
                    val order = if (sort.asc) "asc" else "desc"
                    val comma = if (index == filter.sort.lastIndex) "" else ", "

                    add(" p.$sortField $order$comma ")
                }
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createNativeQuery(sql.toString(), Tuple::class.java)
        query.setParameters(params)

        return query.getResultList(Tuple::class.java).map {
            ValidatedPersonDTO(
                id = it.getString("id"),
                creationDate = it.getLocalDateTimeFromTimeStamp("creationDate"),
                updateDate = it.getLocalDateTimeFromTimeStamp("updateDate"),
                active = it.getBoolean("active"),
                name = it.getString("name"),
                birthDate = it.getLocalDateFromDate("birthDate"),
                phone = it.getString("phone"),
                createDefaultSchedulerConfig = false,
                user = ValidatedUserDTO(
                    id = it.getString("userId"),
                    creationDate = it.getLocalDateTimeFromTimeStamp("userCreationDate"),
                    updateDate = it.getLocalDateTimeFromTimeStamp("userUpdateDate"),
                    active = it.getBoolean("userActive"),
                    email = it.getString("userEmail"),
                    password = it.getString("userPassword"),
                    type = it.getEnum("userType", EnumUserType::class.java),
                )
            )
        }
    }

    private fun getFromListPersons(): StringJoiner {
        val from = StringJoiner(QR_NL).apply {
            add(" from person p ")
            add(" inner join fitness_user u on p.user_id = u.id ")
        }
        return from
    }

    private fun getWhereListPersons(
        filter: PersonFilter,
        params: MutableList<Parameter>
    ): StringJoiner {
        val where = StringJoiner(QR_NL).apply {
            add(" where p.active = true ")
            add(" and u.active = true ")

            filter.name?.let {
                add(" and lower(p.name) like lower(:pName) ")
                params.add(Parameter(name = "pName", value = "%$it%"))
            }

            filter.email?.let {
                add(" and lower(u.email) like lower(:pEmail) ")
                params.add(Parameter(name = "pEmail", value = "%$it%"))
            }

            filter.userType?.let {
                add(" and u.type = :pUserType ")
                params.add(Parameter(name = "pUserType", value = it))
            }

            filter.creationDate?.let {
                add(" and p.creation_date between :pCreationDateStart and :pCreationDateEnd ")
                params.add(Parameter(name = "pCreationDateStart", value = it.first))
                params.add(Parameter(name = "pCreationDateEnd", value = it.second))
            }

            filter.updateDate?.let {
                add(" and p.update_date between :pUpdateDateStart and :pUpdateDateEnd ")
                params.add(Parameter(name = "pUpdateDateStart", value = it.first))
                params.add(Parameter(name = "pUpdateDateEnd", value = it.second))
            }
        }
        return where
    }

    override fun getCountListPersons(filter: PersonFilter): Int {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select count(p.id) ")
        }

        val from = getFromListPersons()

        val where = getWhereListPersons(filter, queryParams)

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createNativeQuery(sql.toString(), Long::class.java)
        query.setParameters(queryParams)

        return (query.singleResult as Long).toInt()
    }
}