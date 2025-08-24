package br.com.fitnesspro.shared.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.dtos.common.StorageModelDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para adição de relatórios")
data class ReportDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Data em que o relatório foi transmitido para a storage", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var storageTransmissionDate: LocalDateTime? = null,

    @field:Schema(description = "Url utilizada para baixar o relatório", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var storageUrl: String? = null,

    @field:Schema(description = "Nome do relatório", example = "Relatório de Exercicios", required = true)
    @field:NotNull(message = "reportDTO.name.notNull")
    @field:Size(max = 256, message = "reportDTO.name.size")
    @field:NotEmpty(message = "reportDTO.name.notNull")
    var name: String? = null,

    @field:Schema(description = "Extensão do vídeo", example = "mp4", required = true)
    @field:NotNull(message = "reportDTO.extension.notNull")
    @field:Size(max = 8, message = "reportDTO.extension.size")
    @field:NotEmpty(message = "reportDTO.extension.notNull")
    var extension: String? = null,

    @field:Schema(description = "Caminho do arquivo no dispositivo móvel", required = true)
    @field:NotNull(message = "reportDTO.filePath.notNull")
    @field:Size(max = 512, message = "reportDTO.filePath.size")
    @field:NotEmpty(message = "reportDTO.filePath.notNull")
    var filePath: String? = null,

    @field:Schema(description = "Data do vídeo", example = "2023-01-01T10:00:00", required = true)
    @field:NotNull(message = "reportDTO.date.notNull")
    @field:PastOrPresent(message = "reportDTO.date.pastOrPresent")
    var date: LocalDateTime? = null,

    @field:Schema(description = "Tamanho do arquivo em kilobytes", example = "1024", required = true)
    @field:NotNull(message = "reportDTO.kbSize.notNull")
    @field:Min(value = 1, message = "reportDTO.kbSize.min")
    var kbSize: Long? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "reportDTO.active.notNull")
    var active: Boolean = true,
): AuditableDTO, StorageModelDTO