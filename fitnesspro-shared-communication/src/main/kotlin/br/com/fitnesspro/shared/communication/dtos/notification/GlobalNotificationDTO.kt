package br.com.fitnesspro.shared.communication.dtos.notification

import br.com.fitnesspro.shared.communication.dtos.notification.interfaces.IGlobalNotificationDTO

data class GlobalNotificationDTO(
    override var title: String? = null,
    override var message: String? = null,
) : IGlobalNotificationDTO