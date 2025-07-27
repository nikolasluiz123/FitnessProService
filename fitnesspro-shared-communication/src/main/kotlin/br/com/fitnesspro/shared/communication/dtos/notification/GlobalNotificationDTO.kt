package br.com.fitnesspro.shared.communication.dtos.notification

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Classe DTO usada para enviar uma notificação para todos os dispositivos")
data class GlobalNotificationDTO(
    @field:Schema(description = "Título que será exibido na notificação", required = true)
    @field:NotBlank(message = "globalNotificationDTO.title.notBlank")
    var title: String? = null,

    @field:Schema(description = "Mensagem que será exibida na notificação", required = true)
    @field:NotBlank(message = "globalNotificationDTO.message.notBlank")
    var message: String? = null,
)