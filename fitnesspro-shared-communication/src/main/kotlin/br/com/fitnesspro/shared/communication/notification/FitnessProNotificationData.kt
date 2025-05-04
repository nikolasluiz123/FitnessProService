package br.com.fitnesspro.shared.communication.notification

import br.com.fitnesspro.shared.communication.enums.notification.EnumNotificationChannel

data class FitnessProNotificationData(
    val title: String,
    val message: String,
    val channel: EnumNotificationChannel,
    val customJSONData: String?
)