package br.com.fitnesspro.workout.repository.auditable

import br.com.fitnesspro.jpa.IAuditableFitnessProRepository
import br.com.fitnesspro.models.workout.ExerciseExecution

interface IExerciseExecutionRepository: IAuditableFitnessProRepository<ExerciseExecution>