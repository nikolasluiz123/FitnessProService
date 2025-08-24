package br.com.fitnesspro.shared.communication.dtos.workout.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO

interface IExercisePreDefinitionDTO : AuditableDTO {
    var active: Boolean
    var name: String?
    var duration: Long?
    var repetitions: Int?
    var sets: Int?
    var rest: Long?
    var exerciseOrder: Int?
    var workoutGroupPreDefinitionId: String?
    var personalTrainerPersonId: String?
}