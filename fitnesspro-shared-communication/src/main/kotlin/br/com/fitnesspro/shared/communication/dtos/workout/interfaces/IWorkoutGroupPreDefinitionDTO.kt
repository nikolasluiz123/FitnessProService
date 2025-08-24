package br.com.fitnesspro.shared.communication.dtos.workout.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO

interface IWorkoutGroupPreDefinitionDTO : AuditableDTO {
    var active: Boolean
    var name: String?
    var personalTrainerPersonId: String?
}