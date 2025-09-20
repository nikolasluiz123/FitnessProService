package br.com.fitnesspro.service.communication.dtos.workout.health

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectSleepSessionDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.Instant
import java.time.LocalDateTime

@Schema(description = "DTO para 'sessão' de sono do Health Connect")
data class ValidatedHealthConnectSleepSessionDTO(
    @field:Schema(description = "Identificador", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Registro ativo", required = true)
    @field:NotNull(message = "healthConnectSleepSession.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "ID dos metadados associados", required = true)
    @field:NotNull(message = "healthConnectSleepSession.healthConnectMetadataId.notNull")
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var healthConnectMetadataId: String? = null,

    @field:Schema(description = "Início da sessão de sono", required = true)
    @field:NotNull(message = "healthConnectSleepSession.startTime.notNull")
    @field:PastOrPresent(message = "healthConnectSleepSession.startTime.pastOrPresent")
    override var startTime: Instant? = null,

    @field:Schema(description = "Fim da sessão de sono", required = true)
    @field:NotNull(message = "healthConnectSleepSession.endTime.notNull")
    @field:PastOrPresent(message = "healthConnectSleepSession.endTime.pastOrPresent")
    override var endTime: Instant? = null,

    @field:Schema(description = "Título da sessão", required = false)
    @field:Size(max = 255, message = "healthConnectSleepSession.title.size")
    override var title: String? = null,

    @field:Schema(description = "Notas da sessão", required = false)
    @field:Size(max = 4096, message = "healthConnectSleepSession.notes.size")
    override var notes: String? = null
) : IHealthConnectSleepSessionDTO