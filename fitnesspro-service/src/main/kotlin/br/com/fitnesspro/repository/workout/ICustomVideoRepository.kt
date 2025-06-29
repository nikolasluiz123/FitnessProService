package br.com.fitnesspro.repository.workout

import br.com.fitnesspro.models.workout.Video
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter

interface ICustomVideoRepository {

    fun getVideosImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<Video>

}