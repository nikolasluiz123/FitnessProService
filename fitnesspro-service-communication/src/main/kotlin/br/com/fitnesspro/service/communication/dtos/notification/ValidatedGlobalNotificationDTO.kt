package br.com.fitnesspro.service.communication.dtos.notification

import br.com.fitnesspro.shared.communication.dtos.notification.interfaces.IGlobalNotificationDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Classe DTO usada para enviar uma notificação para todos os dispositivos")
data class ValidatedGlobalNotificationDTO(
    @field:Schema(description = "Título que será exibido na notificação", required = true)
    @field:NotBlank(message = "globalNotificationDTO.title.notBlank")
    override var title: String? = null,

    @field:Schema(description = "Mensagem que será exibida na notificação", required = true)
    @field:NotBlank(message = "globalNotificationDTO.message.notBlank")
    override var message: String? = null,
) : IGlobalNotificationDTO