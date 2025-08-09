package br.com.fitnesspro.scheduler.repository.auditable

import br.com.fitnesspro.jpa.IAuditableFitnessProRepository
import br.com.fitnesspro.models.scheduler.Scheduler


interface ISchedulerRepository: IAuditableFitnessProRepository<Scheduler>