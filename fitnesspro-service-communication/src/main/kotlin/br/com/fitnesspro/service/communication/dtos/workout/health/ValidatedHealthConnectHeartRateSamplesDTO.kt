package br.com.fitnesspro.service.communication.dtos.workout.health

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectHeartRateSamplesDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.Instant
import java.time.LocalDateTime

@Schema(description = "DTO para amostras de frequência cardíaca do Health Connect")
data class ValidatedHealthConnectHeartRateSamplesDTO(
    @field:Schema(description = "Identificador", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Registro ativo", required = true)
    @field:NotNull(message = "healthConnectHeartRateSamples.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "ID da sessão de frequência cardíaca associada", required = true)
    @field:NotNull(message = "healthConnectHeartRateSamples.healthConnectHeartRateId.notNull")
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var healthConnectHeartRateId: String? = null,

    @field:Schema(description = "Data/hora da amostra", required = true)
    @field:NotNull(message = "healthConnectHeartRateSamples.sampleTime.notNull")
    @field:PastOrPresent(message = "healthConnectHeartRateSamples.sampleTime.pastOrPresent")
    override var sampleTime: Instant? = null,

    @field:Schema(description = "Batimentos por minuto (BPM)", required = true)
    @field:NotNull(message = "healthConnectHeartRateSamples.bpm.notNull")
    @field:Min(value = 1, message = "healthConnectHeartRateSamples.bpm.min")
    override var bpm: Long? = null
) : IHealthConnectHeartRateSamplesDTO