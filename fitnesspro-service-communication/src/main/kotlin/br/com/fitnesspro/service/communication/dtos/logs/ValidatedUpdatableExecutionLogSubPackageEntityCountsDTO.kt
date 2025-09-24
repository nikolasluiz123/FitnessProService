package br.com.fitnesspro.service.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IUpdatableExecutionLogSubPackageEntityCountsDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "Classe DTO usada para atualização de um sub pacote de log de execução")
data class ValidatedUpdatableExecutionLogSubPackageEntityCountsDTO(
    @field:Schema(description = "Quantidade de itens inseridos. Utilizado na importação de dados.", required = true)
    @field:NotNull("updatableExecutionLogSubPackageInfosDTO.insertedItemsCount.notNull")
    override var insertedItemsCount: Int = 0,

    @field:Schema(description = "Quantidade de itens atualizados. Utilizado na importação de dados.", required = true)
    @field:NotNull("updatableExecutionLogSubPackageInfosDTO.updatedItemsCount.notNull")
    override var updatedItemsCount: Int = 0,
): IUpdatableExecutionLogSubPackageEntityCountsDTO
