package br.com.fitnesspro.service.communication.dtos.notification

import br.com.fitnesspro.shared.communication.dtos.notification.interfaces.INotificationDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

@Schema(description = "Classe DTO usada para enviar uma notificação para um ou mais dispositivos específicos")
data class ValidatedNotificationDTO(
    @field:Schema(description = "Título que será exibido na notificação", required = true)
    @field:NotBlank(message = "notificationDTO.title.notBlank")
    override var title: String? = null,

    @field:Schema(description = "Mensagem que será exibida na notificação", required = true)
    @field:NotBlank(message = "notificationDTO.message.notBlank")
    override var message: String? = null,

    @field:Schema(description = "Lista dos Ids dos Dispositivos que serão notificados", required = true)
    @field:NotEmpty(message = "notificationDTO.devicesIds.notEmpty")
    override var devicesIds: List<String> = emptyList()
) : INotificationDTO