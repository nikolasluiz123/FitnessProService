package br.com.fitnesspro.shared.communication.dtos.notification

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class GlobalNotificationDTO(
    @Schema(description = "Título que será exibido na notificação", required = true)
    @field:NotBlank(message = "O título é obrigatório")
    var title: String? = null,

    @Schema(description = "Mensagem que será exibida na notificação", required = true)
    @field:NotBlank(message = "A mensagem é obrigatória")
    var message: String? = null,
)