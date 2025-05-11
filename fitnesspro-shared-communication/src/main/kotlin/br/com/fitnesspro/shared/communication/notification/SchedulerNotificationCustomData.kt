package br.com.fitnesspro.shared.communication.notification

import java.time.LocalDate

data class SchedulerNotificationCustomData(
    val recurrent: Boolean,
    val schedulerId: String?,
    val schedulerDate: LocalDate,
)