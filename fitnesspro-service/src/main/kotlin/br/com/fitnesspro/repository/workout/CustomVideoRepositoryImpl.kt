package br.com.fitnesspro.repository.workout

import br.com.fitnesspro.models.workout.Video
import br.com.fitnesspro.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.repository.common.query.Parameter
import br.com.fitnesspro.repository.common.query.getResultList
import br.com.fitnesspro.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomVideoRepositoryImpl: ICustomVideoRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getVideosImport(
        filter: CommonImportFilter,
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
            add(" where 1=1 ")

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