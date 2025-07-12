package br.com.fitnesspro.config.repository

import br.com.fitnesspro.repository.common.AuditableRepositoryImpl
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(
    basePackages = ["br.com.fitnesspro.repository.auditable"],
    repositoryBaseClass = AuditableRepositoryImpl::class
)
class AuditableRepositoryConfiguration