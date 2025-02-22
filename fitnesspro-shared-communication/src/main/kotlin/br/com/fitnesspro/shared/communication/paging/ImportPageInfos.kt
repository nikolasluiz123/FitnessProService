package br.com.fitnesspro.shared.communication.paging

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class ImportPageInfos(
    @Schema(description = "Tamanho da página", example = "200", required = true)
    @field:Min(value = 1, message = "O tamanho da página deve ser no mínimo 1")
    @field:Max(value = 1000, message = "O tamanho da página deve ser no máximo 1000")
    override val pageSize: Int = 200,

    @Schema(description = "Número da página", example = "0", required = true)
    @field:Min(value = 0, message = "O número da página deve ser no mínimo 0")
    override var pageNumber: Int = 0
): PageInfos
