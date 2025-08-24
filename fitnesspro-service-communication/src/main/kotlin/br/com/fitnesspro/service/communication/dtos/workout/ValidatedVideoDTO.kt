package br.com.fitnesspro.service.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IVideoDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para adição de vídeos")
data class ValidatedVideoDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Data em que o vídeo foi transmitido para a storage", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var storageTransmissionDate: LocalDateTime? = null,

    @field:Schema(description = "Url utilizada para baixar o vídeo", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var storageUrl: String? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "videoDTO.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "Extensão do vídeo", example = "mp4", required = true)
    @field:Size(max = 8, message = "videoDTO.extension.size")
    @field:NotEmpty(message = "videoDTO.extension.notNull")
    override var extension: String? = null,

    @field:Schema(description = "Caminho do arquivo no dispositivo móvel", required = true)
    @field:Size(max = 512, message = "videoDTO.filePath.size")
    @field:NotEmpty(message = "videoDTO.filePath.notNull")
    override var filePath: String? = null,

    @field:Schema(description = "Data do vídeo", example = "2023-01-01T10:00:00", required = true)
    @field:NotNull(message = "videoDTO.date.notNull")
    @field:PastOrPresent(message = "videoDTO.date.pastOrPresent")
    override var date: LocalDateTime? = null,

    @field:Schema(description = "Tamanho do arquivo em kilobytes", example = "1024", required = true)
    @field:NotNull(message = "videoDTO.kbSize.notNull")
    @field:Min(value = 1, message = "videoDTO.kbSize.min")
    override var kbSize: Long? = null,

    @field:Schema(description = "Duração do vídeo em segundos", example = "120", required = true)
    @field:NotNull(message = "videoDTO.seconds.notNull")
    @field:Min(value = 1, message = "videoDTO.seconds.min")
    override var seconds: Long? = null,

    @field:Schema(description = "Largura do vídeo em pixels", example = "1920", required = true)
    @field:NotNull(message = "videoDTO.width.notNull")
    @field:Min(value = 1, message = "videoDTO.width.min")
    override var width: Int? = null,

    @field:Schema(description = "Altura do vídeo em pixels", example = "1080", required = true)
    @field:NotNull(message = "videoDTO.height.notNull")
    @field:Min(value = 1, message = "videoDTO.height.min")
    override var height: Int? = null
): IVideoDTO