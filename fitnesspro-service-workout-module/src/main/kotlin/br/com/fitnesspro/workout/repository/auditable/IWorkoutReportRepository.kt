package br.com.fitnesspro.workout.repository.auditable

import br.com.fitnesspro.jpa.IAuditableFitnessProRepository
import br.com.fitnesspro.models.general.WorkoutReport

interface IWorkoutReportRepository: IAuditableFitnessProRepository<WorkoutReport>