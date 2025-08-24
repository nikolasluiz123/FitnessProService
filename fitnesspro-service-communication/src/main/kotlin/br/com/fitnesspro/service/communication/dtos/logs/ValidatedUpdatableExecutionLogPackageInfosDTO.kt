package br.com.fitnesspro.service.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IUpdatableExecutionLogPackageInfosDTO
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para atualização de um pacote de log de execução")
data class ValidatedUpdatableExecutionLogPackageInfosDTO(
    @field:Schema(description = "Quantidade de itens inseridos. Utilizado na importação de dados.", required = false)
    override var insertedItemsCount: Int? = null,

    @field:Schema(description = "Quantidade de itens atualizados. Utilizado na importação de dados.", required = false)
    override var updatedItemsCount: Int? = null,

    @field:Schema(description = "Quantidade de itens enviados na exportação.", required = false)
    override var allItemsCount: Int? = null,

    @field:Schema(description = "Momento que o client começou a executar a operação.", required = false)
    override var clientExecutionStart: LocalDateTime? = null,

    @field:Schema(description = "Momento que o client terminou de executar a operação.", required = false)
    override var clientExecutionEnd: LocalDateTime? = null,

    @field:Schema(description = "Stack de erro.", required = false)
    override var error: String? = null
): IUpdatableExecutionLogPackageInfosDTO
