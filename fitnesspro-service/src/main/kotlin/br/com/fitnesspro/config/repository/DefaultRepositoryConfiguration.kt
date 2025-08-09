package br.com.fitnesspro.config.repository

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(
    basePackages = [
        "br.com.fitnesspro.common.repository.jpa",
        "br.com.fitnesspro.scheduled.task.repository.jpa",
        "br.com.fitnesspro.scheduler.repository.jpa",
        "br.com.fitnesspro.workout.repository.jpa",
        "br.com.fitnesspro.log.repository.jpa",
        "br.com.fitnesspro.authentication.repository.jpa"
    ],
)
class DefaultRepositoryConfiguration