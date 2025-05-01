package br.com.fitnesspro.repository.executions

import br.com.fitnesspro.models.logs.ExecutionLogPackage
import org.springframework.data.jpa.repository.JpaRepository

interface IExecutionsLogPackageRepository: JpaRepository<ExecutionLogPackage, String> {
}