package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.workout.Video
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomVideoRepository {

    fun getVideosImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<Video>

}