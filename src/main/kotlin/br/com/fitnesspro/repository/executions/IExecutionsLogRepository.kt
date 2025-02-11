package br.com.fitnesspro.repository.executions

import br.com.fitnesspro.models.executions.ExecutionLog
import org.springframework.data.jpa.repository.JpaRepository

interface IExecutionsLogRepository: JpaRepository<ExecutionLog, String>