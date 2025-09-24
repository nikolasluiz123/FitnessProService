package br.com.fitnesspro.log.repository.jpa

import br.com.fitnesspro.models.logs.ExecutionLogSubPackage
import org.springframework.data.jpa.repository.JpaRepository

interface IExecutionsLogSupPackageRepository: JpaRepository<ExecutionLogSubPackage, String>