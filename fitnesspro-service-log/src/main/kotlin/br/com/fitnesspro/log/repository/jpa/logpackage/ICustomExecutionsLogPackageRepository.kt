package br.com.fitnesspro.log.repository.jpa.logpackage

interface ICustomExecutionsLogPackageRepository {

    fun getExecutionProcessingTime(logId: String): ExecutionProcessingTime
}