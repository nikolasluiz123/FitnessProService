package br.com.fitnesspro.authentication.repository.auditable

import br.com.fitnesspro.jpa.IAuditableFitnessProRepository
import br.com.fitnesspro.models.scheduler.SchedulerConfig

interface ISchedulerConfigRepository: IAuditableFitnessProRepository<SchedulerConfig>