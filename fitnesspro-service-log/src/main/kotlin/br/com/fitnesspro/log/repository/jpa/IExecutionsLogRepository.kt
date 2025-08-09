package br.com.fitnesspro.log.repository.jpa

import br.com.fitnesspro.models.logs.ExecutionLog
import org.springframework.data.jpa.repository.JpaRepository

interface IExecutionsLogRepository: JpaRepository<ExecutionLog, String>