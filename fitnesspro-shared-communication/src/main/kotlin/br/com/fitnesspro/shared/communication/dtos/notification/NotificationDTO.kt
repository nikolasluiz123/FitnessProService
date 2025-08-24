package br.com.fitnesspro.shared.communication.dtos.notification

import br.com.fitnesspro.shared.communication.dtos.notification.interfaces.INotificationDTO

data class NotificationDTO(
    override var title: String? = null,
    override var message: String? = null,
    override var devicesIds: List<String> = emptyList()
) : INotificationDTO