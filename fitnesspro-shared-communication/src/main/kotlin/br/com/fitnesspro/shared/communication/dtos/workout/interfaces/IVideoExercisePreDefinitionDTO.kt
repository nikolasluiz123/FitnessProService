package br.com.fitnesspro.shared.communication.dtos.workout.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO

interface IVideoExercisePreDefinitionDTO : AuditableDTO {
    var active: Boolean
    var exercisePreDefinitionId: String?
    var videoId: String?
}