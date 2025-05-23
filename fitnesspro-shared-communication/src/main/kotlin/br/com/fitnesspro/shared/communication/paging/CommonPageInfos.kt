package br.com.fitnesspro.shared.communication.paging

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class CommonPageInfos(
    @Schema(description = "Tamanho da página", example = "100", required = true)
    @field:Min(value = 10, message = "O tamanho da página deve ser no mínimo 10")
    @field:Max(value = 100, message = "O tamanho da página deve ser no máximo 100")
    override val pageSize: Int = 100,

    @Schema(description = "Número da página", example = "0", required = true)
    @field:Min(value = 0, message = "O número da página deve ser no mínimo 0")
    override var pageNumber: Int = 0
): PageInfos