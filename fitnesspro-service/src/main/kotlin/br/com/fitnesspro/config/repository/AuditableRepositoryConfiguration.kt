package br.com.fitnesspro.config.repository

import br.com.fitnesspro.jpa.AuditableRepositoryImpl
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(
    basePackages = [
        "br.com.fitnesspro.common.repository.auditable",
        "br.com.fitnesspro.scheduled.task.repository.auditable",
        "br.com.fitnesspro.scheduler.repository.auditable",
        "br.com.fitnesspro.workout.repository.auditable",
        "br.com.fitnesspro.authentication.repository.auditable"
    ],
    repositoryBaseClass = AuditableRepositoryImpl::class
)
class AuditableRepositoryConfiguration