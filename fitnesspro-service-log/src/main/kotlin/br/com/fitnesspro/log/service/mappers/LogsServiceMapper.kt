package br.com.fitnesspro.log.service.mappers

import br.com.fitnesspro.models.logs.ExecutionLog
import br.com.fitnesspro.models.logs.ExecutionLogPackage
import br.com.fitnesspro.models.logs.ExecutionLogSubPackage
import br.com.fitnesspro.service.communication.dtos.logs.ValidatedExecutionLogDTO
import br.com.fitnesspro.service.communication.dtos.logs.ValidatedExecutionLogPackageDTO
import br.com.fitnesspro.service.communication.dtos.logs.ValidatedExecutionLogSubPackageDTO
import org.springframework.stereotype.Service

@Service
class LogsServiceMapper {

    fun getValidatedExecutionLogDTO(model: ExecutionLog): ValidatedExecutionLogDTO {
        return ValidatedExecutionLogDTO(
            id = model.id,
            type = model.type,
            state = model.state,
            endPoint = model.endPoint,
            methodName = model.methodName,
            userEmail = model.user?.email,
            deviceId = model.device?.id,
            applicationName = model.application?.name,
            pageSize = model.pageSize,
            creationDate = model.creationDate,
        )
    }

    fun getValidatedExecutionLogPackageDTO(model: ExecutionLogPackage): ValidatedExecutionLogPackageDTO {
        return ValidatedExecutionLogPackageDTO(
            id = model.id,
            executionLogId = model.executionLog.id,
            clientExecutionStart = model.clientExecutionStart,
            clientExecutionEnd = model.clientExecutionEnd,
            serviceExecutionStart = model.serviceExecutionStart,
            serviceExecutionEnd = model.serviceExecutionEnd,
            requestBody = model.requestBody,
            responseBody = model.responseBody,
            executionAdditionalInfos = model.executionAdditionalInfos,
            error = model.error,
        )
    }

    fun getValidatedExecutionLogSubPackageDTO(model: ExecutionLogSubPackage): ValidatedExecutionLogSubPackageDTO {
        return ValidatedExecutionLogSubPackageDTO(
            id = model.id,
            entityName = model.entityName,
            executionLogPackageId = model.executionLogPackage?.id,
            insertedItemsCount = model.insertedItemsCount ?: 0,
            updatedItemsCount = model.updatedItemsCount ?: 0,
            allItemsCount = model.allItemsCount ?: 0,
            kbSize = model.kbSize,
            lastUpdateDate = model.lastUpdateDate
        )
    }
}