package br.com.fitnesspro.repository.auditable.workout

import br.com.fitnesspro.models.workout.Workout
import br.com.fitnesspro.repository.common.IAuditableFitnessProRepository


interface IWorkoutRepository: IAuditableFitnessProRepository<Workout>