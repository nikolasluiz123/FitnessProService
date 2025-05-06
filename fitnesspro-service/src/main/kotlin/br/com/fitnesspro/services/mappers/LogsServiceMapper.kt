package br.com.fitnesspro.services.mappers

import br.com.fitnesspro.models.logs.ExecutionLog
import br.com.fitnesspro.models.logs.ExecutionLogPackage
import br.com.fitnesspro.shared.communication.dtos.logs.ExecutionLogDTO
import br.com.fitnesspro.shared.communication.dtos.logs.ExecutionLogPackageDTO
import org.springframework.stereotype.Service

@Service
class LogsServiceMapper {

    fun getExecutionLogDTO(model: ExecutionLog): ExecutionLogDTO {
        return ExecutionLogDTO(
            id = model.id,
            type = model.type,
            state = model.state,
            endPoint = model.endPoint,
            methodName = model.methodName,
            userEmail = model.user?.email,
            deviceId = model.device?.id,
            applicationName = model.application?.name,
            pageSize = model.pageSize,
            lastUpdateDate = model.lastUpdateDate,
            creationDate = model.creationDate,
        )
    }

    fun getExecutionLogPackageDTO(model: ExecutionLogPackage): ExecutionLogPackageDTO {
        return ExecutionLogPackageDTO(
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
            insertedItemsCount = model.insertedItemsCount,
            updatedItemsCount = model.updatedItemsCount,
            allItemsCount = model.allItemsCount,
        )
    }
}