package br.com.fitnesspro.config.repository

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["br.com.fitnesspro.repository.jpa"])
class DefaultRepositoryConfiguration