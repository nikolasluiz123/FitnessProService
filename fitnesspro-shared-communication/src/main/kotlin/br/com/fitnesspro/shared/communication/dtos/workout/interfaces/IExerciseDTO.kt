package br.com.fitnesspro.shared.communication.dtos.workout.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO

interface IExerciseDTO : AuditableDTO {
    var active: Boolean
    var name: String?
    var duration: Long?
    var repetitions: Int?
    var sets: Int?
    var rest: Long?
    var observation: String?
    var exerciseOrder: Int?
    var workoutGroupDTO: IWorkoutGroupDTO?
}