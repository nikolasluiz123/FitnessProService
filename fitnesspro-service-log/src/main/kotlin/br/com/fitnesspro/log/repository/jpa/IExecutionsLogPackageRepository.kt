package br.com.fitnesspro.log.repository.jpa

import br.com.fitnesspro.models.logs.ExecutionLogPackage
import org.springframework.data.jpa.repository.JpaRepository

interface IExecutionsLogPackageRepository: JpaRepository<ExecutionLogPackage, String> {
}