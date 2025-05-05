package br.com.fitnesspro.notification

import com.google.firebase.messaging.FirebaseMessagingException

data class NotificationResult(
    val idsDevicesError: List<String> = emptyList(),
    val idsDevicesSuccess: List<String> = emptyList(),
    val exception: FirebaseMessagingException? = null
) {
    fun success() = idsDevicesError.isEmpty()
}