package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para adição de vídeos")
data class VideoDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "{baseDTO.id.size}")
    override var id: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "{videoDTO.active.notNull}")
    var active: Boolean = true,

    @Schema(description = "Extensão do vídeo", example = "mp4", required = true)
    @field:NotNull(message = "{videoDTO.extension.notNull}")
    @field:Size(max = 8, message = "{videoDTO.extension.size}")
    @field:NotEmpty(message = "{videoDTO.extension.notNull}")
    var extension: String? = null,

    @Schema(description = "Caminho do arquivo no dispositivo móvel", required = true)
    @field:NotNull(message = "{videoDTO.filePath.notNull}")
    @field:Size(max = 512, message = "{videoDTO.filePath.size}")
    @field:NotEmpty(message = "{videoDTO.filePath.notNull}")
    var filePath: String? = null,

    @Schema(description = "Data do vídeo", example = "2023-01-01T10:00:00", required = true)
    @field:NotNull(message = "{videoDTO.date.notNull}")
    @field:PastOrPresent(message = "{videoDTO.date.pastOrPresent}")
    var date: LocalDateTime? = null,

    @Schema(description = "Tamanho do arquivo em kilobytes", example = "1024", required = true)
    @field:NotNull(message = "{videoDTO.kbSize.notNull}")
    @field:Min(value = 1, message = "{videoDTO.kbSize.min}")
    var kbSize: Long? = null,

    @Schema(description = "Duração do vídeo em segundos", example = "120", required = true)
    @field:NotNull(message = "{videoDTO.seconds.notNull}")
    @field:Min(value = 1, message = "{videoDTO.seconds.min}")
    var seconds: Long? = null,

    @Schema(description = "Largura do vídeo em pixels", example = "1920", required = true)
    @field:NotNull(message = "{videoDTO.width.notNull}")
    @field:Min(value = 1, message = "{videoDTO.width.min}")
    var width: Int? = null,

    @Schema(description = "Altura do vídeo em pixels", example = "1080", required = true)
    @field:NotNull(message = "videoDTO.height.notNull")
    @field:Min(value = 1, message = "{videoDTO.height.min}")
    var height: Int? = null
): AuditableDTO