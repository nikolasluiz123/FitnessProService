package br.com.fitnesspro.shared.communication.dtos.logs.interfaces

import java.time.LocalDateTime

interface IUpdatableExecutionLogSubPackageInfosDTO {
    val entityCounts: Map<String, IUpdatableExecutionLogSubPackageEntityCountsDTO>
    val lastUpdateDateMap: Map<String, LocalDateTime?>
}