package br.com.fitnesspro.workout.repository.auditable

import br.com.fitnesspro.jpa.IAuditableFitnessProRepository
import br.com.fitnesspro.models.workout.WorkoutGroup


interface IWorkoutGroupRepository: IAuditableFitnessProRepository<WorkoutGroup>