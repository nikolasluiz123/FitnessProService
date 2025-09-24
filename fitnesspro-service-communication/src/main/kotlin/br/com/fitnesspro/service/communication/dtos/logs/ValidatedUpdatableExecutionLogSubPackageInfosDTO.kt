package br.com.fitnesspro.service.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IUpdatableExecutionLogSubPackageEntityCountsDTO
import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IUpdatableExecutionLogSubPackageInfosDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para atualização de um sub pacote de log de execução")
data class ValidatedUpdatableExecutionLogSubPackageInfosDTO(
    @field:Schema(description = "Map com informações de quantidade de entidades processadas segregado por entidade", required = true)
    @field:NotNull("updatableExecutionLogSubPackageInfosDTO.entityCounts.notNull")
    override val entityCounts: Map<String, IUpdatableExecutionLogSubPackageEntityCountsDTO> = emptyMap(),

    @field:Schema(description = "Map com a data de corte de cada entidade, utilizada apenas na importação", required = false)
    override val lastUpdateDateMap: Map<String, LocalDateTime?> = emptyMap()
): IUpdatableExecutionLogSubPackageInfosDTO
