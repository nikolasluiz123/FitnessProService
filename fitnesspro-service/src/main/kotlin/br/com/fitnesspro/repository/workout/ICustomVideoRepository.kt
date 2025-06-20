package br.com.fitnesspro.repository.workout

import br.com.fitnesspro.models.workout.Video
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter

interface ICustomVideoRepository {

    fun getVideosImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<Video>

}