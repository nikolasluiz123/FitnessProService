package br.com.fitnesspro.service.repository.executions

import br.com.fitnesspro.service.models.logs.ExecutionLogPackage
import org.springframework.data.jpa.repository.JpaRepository

interface IExecutionsLogPackageRepository: JpaRepository<ExecutionLogPackage, String> {
}