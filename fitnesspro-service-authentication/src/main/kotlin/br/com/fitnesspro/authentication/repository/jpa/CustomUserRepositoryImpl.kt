package br.com.fitnesspro.authentication.repository.jpa

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.setParameters
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomUserRepositoryImpl : ICustomUserRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun isEmailInUse(email: String, userId: String?): Boolean {
        val params = mutableListOf<Parameter>()
        params.add(Parameter(name = "pEmail", value = email))


        val select = StringJoiner(QR_NL).apply {
            add(" select 1 ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from fitness_user u ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where u.email = :pEmail ")
            add(" and u.active = true ")

            userId?.let {
                add(" and u.id != :pUserId ")
                params.add(Parameter(name = "pUserId", value = userId))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(" select exists ( ")
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(" ) ")
        }

        val query = entityManager.createNativeQuery(sql.toString())
        query.setParameters(params)

        return try {
            query.singleResult as Boolean
        } catch (ex: NoResultException) {
            false
        }
    }
}