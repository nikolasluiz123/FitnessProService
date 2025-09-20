package br.com.fitnesspro.service.communication.dtos.workout.health

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectHeartRateDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Schema(description = "DTO para 'sessão' de frequência cardíaca do Health Connect")
data class ValidatedHealthConnectHeartRateDTO(
    @field:Schema(description = "Identificador", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Registro ativo", required = true)
    @field:NotNull(message = "healthConnectHeartRate.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "ID dos metadados associados", required = true)
    @field:NotNull(message = "healthConnectHeartRate.healthConnectMetadataId.notNull")
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var healthConnectMetadataId: String? = null,

    @field:Schema(description = "ID da execução do exercício associado", required = true)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var exerciseExecutionId: String? = null,

    @field:Schema(description = "Início do período", required = true)
    @field:NotNull(message = "healthConnectHeartRate.startTime.notNull")
    @field:PastOrPresent(message = "healthConnectHeartRate.startTime.pastOrPresent")
    override var startTime: Instant? = null,

    @field:Schema(description = "Fim do período", required = true)
    @field:NotNull(message = "healthConnectHeartRate.endTime.notNull")
    @field:PastOrPresent(message = "healthConnectHeartRate.endTime.pastOrPresent")
    override var endTime: Instant? = null,

    @field:Schema(description = "Fuso horário inicial em segundos", required = false)
    override var startZoneOffset: ZoneOffset? = null,

    @field:Schema(description = "Fuso horário final em segundos", required = false)
    override var endZoneOffset: ZoneOffset? = null
) : IHealthConnectHeartRateDTO