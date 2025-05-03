package br.com.fitnesspro.services.firebase

import br.com.fitnesspro.exception.FirebaseNotificationException
import br.com.fitnesspro.services.serviceauth.DeviceService
import br.com.fitnesspro.shared.communication.dtos.notification.GlobalNotificationDTO
import br.com.fitnesspro.shared.communication.dtos.notification.NotificationDTO
import br.com.fitnesspro.shared.communication.enums.notification.EnumNotificationChannel
import br.com.fitnesspro.shared.communication.notification.FitnessProNotificationData
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.SendResponse
import org.springframework.stereotype.Service

@Service
class FirebaseNotificationService(
    private val deviceService: DeviceService
) {

    fun sendExternalNotification(notificationDTO: NotificationDTO) {
        val notification = FitnessProNotificationData(
            title = notificationDTO.title!!,
            message = notificationDTO.message!!,
            channel = EnumNotificationChannel.GENERIC_COMMUNICATION_CHANNEL
        )

        val devicesTokens = deviceService.getFirebaseMessagingTokenFromDevicesWithIds(notificationDTO.devicesIds)

        verifyStrategyAndSendNotification(devicesTokens, notification)
    }

    fun sendNotificationToPerson(title: String, message: String, personIds: List<String>, channel: EnumNotificationChannel) {
        val notification = FitnessProNotificationData(title, message, channel)
        val devicesTokens = deviceService.getFirebaseMessagingTokenFromDevicesWithPersonIds(personIds)

        verifyStrategyAndSendNotification(devicesTokens, notification)
    }

    fun sendNotificationToAllDevices(notificationDTO: GlobalNotificationDTO) {
        val notification = FitnessProNotificationData(
            title = notificationDTO.title!!,
            message = notificationDTO.message!!,
            channel = EnumNotificationChannel.GENERIC_COMMUNICATION_CHANNEL
        )

        val devicesTokens = deviceService.getFirebaseMessagingTokenFromAllDevices()

        verifyStrategyAndSendNotification(devicesTokens, notification)
    }

    private fun verifyStrategyAndSendNotification(
        devicesTokens: List<String>,
        notification: FitnessProNotificationData
    ) {
        if (devicesTokens.isEmpty()) {
            throw FirebaseNotificationException("Nenhum dispositivo encontrado para realizar a notificação.")
        }

        if (devicesTokens.size > 1) {
            sendMulticastNotification(notification, devicesTokens.toMutableList())
        } else {
            sendNotification(notification, devicesTokens.first())
        }
    }

    private fun sendMulticastNotification(notification: FitnessProNotificationData, deviceTokens: MutableList<String>) {
        val batch = deviceTokens.take(500).toMutableList()
        val remaining = deviceTokens.drop(500).toMutableList()

        sendMulticastNotificationBatch(notification, batch)

        if (remaining.isNotEmpty()) {
            sendMulticastNotification(notification, remaining)
        }
    }

    private fun sendMulticastNotificationBatch(notification: FitnessProNotificationData, deviceTokens: MutableList<String>) {
        val message = MulticastMessage
            .builder()
            .addAllTokens(deviceTokens)
            .putData(notification::title.name, notification.title)
            .putData(notification::message.name, notification.message)
            .putData(notification::channel.name, notification.channel.name)
            .build()

        val batchResponse = FirebaseMessaging.getInstance().sendEachForMulticast(message)

        if (batchResponse.failureCount > 0) {
            val fails = batchResponse.responses.mapIndexed { index, sendResponse ->
                val exception = sendResponse.exception
                val deviceToken = getDeviceTokenFromSendResponse(sendResponse, deviceTokens, index)

                Pair(deviceToken, exception)
            }.filter { it.first != null }

            val failedTokens = fails.map { it.first!! }
            val deviceIdsFail = deviceService.getDevicesWithFirebaseMessagingTokens(failedTokens).map { it.id }

            throw FirebaseNotificationException("Não foi possível enviar a notificação para os seguintes dispositivos: ${deviceIdsFail.joinToString()}")
        }
    }

    private fun getDeviceTokenFromSendResponse(sendResponse: SendResponse, deviceTokens: MutableList<String>, index: Int): String? {
        return if (!sendResponse.isSuccessful) deviceTokens[index] else null
    }

    private fun sendNotification(notification: FitnessProNotificationData, deviceToken: String) {
        val message = Message
            .builder()
            .setToken(deviceToken)
            .putData(notification::title.name, notification.title)
            .putData(notification::message.name, notification.message)
            .putData(notification::channel.name, notification.channel.name)
            .build()

        FirebaseMessaging.getInstance().send(message)
    }
}