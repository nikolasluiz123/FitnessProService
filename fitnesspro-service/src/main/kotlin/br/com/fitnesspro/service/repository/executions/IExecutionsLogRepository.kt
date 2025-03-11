package br.com.fitnesspro.service.repository.executions

import br.com.fitnesspro.service.models.logs.ExecutionLog
import org.springframework.data.jpa.repository.JpaRepository

interface IExecutionsLogRepository: JpaRepository<ExecutionLog, String>