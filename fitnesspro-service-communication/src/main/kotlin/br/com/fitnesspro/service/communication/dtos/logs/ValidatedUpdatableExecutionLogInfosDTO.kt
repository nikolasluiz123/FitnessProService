package br.com.fitnesspro.service.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IUpdatableExecutionLogInfosDTO
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Classe DTO usada para atualizar um log de execução")
data class ValidatedUpdatableExecutionLogInfosDTO(
    @field:Schema(description = "Tamanho da página da execução. Utilizado na exportação e importação de dados.", required = false)
    override var pageSize: Int? = null,

    @field:Schema(description = "Estado da execução.", required = false)
    override var state: EnumExecutionState? = null
): IUpdatableExecutionLogInfosDTO
