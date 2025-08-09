package br.com.fitnesspro.scheduler.to

import java.time.OffsetDateTime

data class TOSchedulerAntecedenceNotificationInfo(
    val personToSendNotificationId: String,
    val schedulerId: String,
    val otherPersonName: String,
    val dateTimeStart: OffsetDateTime,
)