package br.com.fitnesspro.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.util.*

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["br.com.fitnesspro.service", "br.com.fitnesspro.data.access.repository"])
class FitnessproApplication

fun main(args: Array<String>) {
	TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	runApplication<FitnessproApplication>(*args)
}
