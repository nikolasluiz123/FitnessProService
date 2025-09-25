package br.com.fitnesspro.service.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IExecutionLogSubPackageDTO
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para leitura de um sub pacote de log de execução")
data class ValidatedExecutionLogSubPackageDTO(
    @get:Schema(description = "Identificador", required = true)
    override var id: String? = null,

    @get:Schema(description = "Nome da entidade", required = true)
    override val entityName: String? = null,

    @get:Schema(description = "ID do pacote de log de execução", required = true)
    override val executionLogPackageId: String? = null,

    @get:Schema(description = "Contagem de itens inseridos", required = false)
    override val insertedItemsCount: Int? = null,

    @get:Schema(description = "Contagem de itens atualizados", required = false)
    override val updatedItemsCount: Int? = null,

    @get:Schema(description = "Contagem total de itens", required = false)
    override val allItemsCount: Int? = null,

    @get:Schema(description = "Tamanho em KB", required = false)
    override val kbSize: Long? = null,

    @get:Schema(description = "Data da última atualização", required = false)
    override val lastUpdateDate: LocalDateTime? = null,

    @field:Schema(description = "Request body enviado na requisição", required = false)
    override var requestBody: String? = null,

    @field:Schema(description = "Response body retornado na requisição", required = false)
    override var responseBody: String? = null,
) : IExecutionLogSubPackageDTO