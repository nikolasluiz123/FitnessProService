package br.com.fitnesspro.shared.communication.dtos.workout.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO

interface IVideoExerciseDTO : AuditableDTO {
    var active: Boolean
    var exerciseId: String?
    var videoId: String?
}