package br.com.fitnesspro.repository.executions

import br.com.fitnesspro.models.logs.ExecutionLog
import org.springframework.data.jpa.repository.JpaRepository

interface IExecutionsLogRepository: JpaRepository<ExecutionLog, String>