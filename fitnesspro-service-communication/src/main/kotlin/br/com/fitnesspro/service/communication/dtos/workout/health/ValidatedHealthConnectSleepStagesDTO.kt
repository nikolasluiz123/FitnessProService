package br.com.fitnesspro.service.communication.dtos.workout.health

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectSleepStagesDTO
import br.com.fitnesspro.shared.communication.enums.health.EnumSleepStage
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.Instant
import java.time.LocalDateTime

@Schema(description = "DTO para estágios de sono do Health Connect")
data class ValidatedHealthConnectSleepStagesDTO(
    @field:Schema(description = "Identificador", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Registro ativo", required = true)
    @field:NotNull(message = "healthConnectSleepStages.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "ID da sessão de sono associada", required = true)
    @field:NotNull(message = "healthConnectSleepStages.healthConnectSleepSessionId.notNull")
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var healthConnectSleepSessionId: String? = null,

    @field:Schema(description = "Início do estágio", required = true)
    @field:NotNull(message = "healthConnectSleepStages.startTime.notNull")
    @field:PastOrPresent(message = "healthConnectSleepStages.startTime.pastOrPresent")
    override var startTime: Instant? = null,

    @field:Schema(description = "Fim do estágio", required = true)
    @field:NotNull(message = "healthConnectSleepStages.endTime.notNull")
    @field:PastOrPresent(message = "healthConnectSleepStages.endTime.pastOrPresent")
    override var endTime: Instant? = null,

    @field:Schema(description = "Tipo do estágio (AWAKE, LIGHT, DEEP, REM)", required = true)
    @field:NotNull(message = "healthConnectSleepStages.stage.notNull")
    override var stage: EnumSleepStage? = null
) : IHealthConnectSleepStagesDTO