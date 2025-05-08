package br.com.fitnesspro.services.firebase

import br.com.fitnesspro.exception.FirebaseNotificationException
import br.com.fitnesspro.notification.NotificationResult
import br.com.fitnesspro.services.serviceauth.DeviceService
import br.com.fitnesspro.shared.communication.dtos.notification.GlobalNotificationDTO
import br.com.fitnesspro.shared.communication.dtos.notification.NotificationDTO
import br.com.fitnesspro.shared.communication.enums.notification.EnumNotificationChannel
import br.com.fitnesspro.shared.communication.notification.FitnessProNotificationData
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class FirebaseNotificationService(
    private val deviceService: DeviceService,
    private val messageSource: MessageSource
) {
    fun sendExternalNotification(notificationDTO: NotificationDTO) {
        val notification = FitnessProNotificationData(
            title = notificationDTO.title!!,
            message = notificationDTO.message!!,
            channel = EnumNotificationChannel.GENERIC_COMMUNICATION_CHANNEL,
            customJSONData = null
        )

        val devicesTokens = deviceService.getFirebaseMessagingTokenFromDevicesWithIds(notificationDTO.devicesIds)
        val results = verifyStrategyAndSendNotification(devicesTokens, notification)

        throwsExceptionIfFail(results)
    }

    private fun throwsExceptionIfFail(results: List<NotificationResult>) {
        val failedDevices = results
            .filterNot { it.success() }
            .flatMap { it.idsDevicesError }
            .distinct()

        if (failedDevices.isNotEmpty()) {
            val devicesFail = failedDevices.joinToString()
            val message = messageSource.getMessage("notification.error.send", arrayOf(devicesFail), Locale.getDefault())
            throw FirebaseNotificationException(message)
        }
    }

    fun sendNotificationToPerson(
        title: String,
        message: String,
        personIds: List<String>,
        channel: EnumNotificationChannel,
        customJSONData: String? = null
    ): List<NotificationResult> {
        val notification = FitnessProNotificationData(title, message, channel, customJSONData)
        val devicesTokens = deviceService.getFirebaseMessagingTokenFromDevicesWithPersonIds(personIds)

        return verifyStrategyAndSendNotification(devicesTokens, notification)
    }

    fun sendNotificationToAllDevices(notificationDTO: GlobalNotificationDTO) {
        val notification = FitnessProNotificationData(
            title = notificationDTO.title!!,
            message = notificationDTO.message!!,
            channel = EnumNotificationChannel.GENERIC_COMMUNICATION_CHANNEL,
            customJSONData = null
        )

        val devicesTokens = deviceService.getFirebaseMessagingTokenFromAllDevices()
        val results = verifyStrategyAndSendNotification(devicesTokens, notification)

        throwsExceptionIfFail(results)
    }

    private fun verifyStrategyAndSendNotification(
        devicesTokens: List<String>,
        notification: FitnessProNotificationData
    ): List<NotificationResult> {
        if (devicesTokens.isEmpty()) {
            val message = messageSource.getMessage("notification.error.empty.devices", null, Locale.getDefault())
            throw FirebaseNotificationException(message)
        }

        return if (devicesTokens.size > 1) {
            sendMulticastNotification(notification, devicesTokens)
        } else {
            listOf(sendNotification(notification, devicesTokens.first()))
        }
    }

    private fun sendMulticastNotification(notification: FitnessProNotificationData, deviceTokens: List<String>): MutableList<NotificationResult> {
        val results = mutableListOf<NotificationResult>()

        deviceTokens.chunked(500).forEach { batch ->
            results.add(sendMulticastNotificationBatch(notification, batch))
        }

        return results
    }

    private fun sendMulticastNotificationBatch(
        notification: FitnessProNotificationData,
        deviceTokens: List<String>
    ): NotificationResult {
        val messageBuilder = MulticastMessage
            .builder()
            .addAllTokens(deviceTokens)
            .putData(notification::title.name, notification.title)
            .putData(notification::message.name, notification.message)
            .putData(notification::channel.name, notification.channel.name)

        notification.customJSONData?.let {
            messageBuilder.putData(notification::customJSONData.name, it)
        }

        val message = messageBuilder.build()

        val batchResponse = FirebaseMessaging.getInstance().sendEachForMulticast(message)
        val successDeviceTokenList = mutableListOf<String>()
        val failDeviceTokenList = mutableListOf<String>()

        batchResponse.responses.forEachIndexed { index, sendResponse ->
            if (sendResponse.isSuccessful) {
                successDeviceTokenList.add(deviceTokens[index])
            } else {
                failDeviceTokenList.add(deviceTokens[index])
            }
        }

        val deviceIdsSuccess = deviceService.getDevicesWithFirebaseMessagingTokens(successDeviceTokenList).map { it.id!! }
        val deviceIdsFail = deviceService.getDevicesWithFirebaseMessagingTokens(failDeviceTokenList).map { it.id!! }

        return NotificationResult(
            idsDevicesError = deviceIdsFail,
            idsDevicesSuccess = deviceIdsSuccess
        )
    }

    private fun sendNotification(notification: FitnessProNotificationData, deviceToken: String): NotificationResult {
        val messageBuilder = Message
            .builder()
            .setToken(deviceToken)
            .putData(notification::title.name, notification.title)
            .putData(notification::message.name, notification.message)
            .putData(notification::channel.name, notification.channel.name)

        notification.customJSONData?.let {
            messageBuilder.putData(notification::customJSONData.name, it)
        }

        val message = messageBuilder.build()

        val deviceId = deviceService.getDevicesWithFirebaseMessagingTokens(listOf(deviceToken)).first().id!!

        return try {
            FirebaseMessaging.getInstance().send(message)
            NotificationResult(idsDevicesSuccess = listOf(deviceId))
        } catch (ex: FirebaseMessagingException) {
            ex.printStackTrace()
            NotificationResult(idsDevicesError = listOf(deviceId), exception = ex)
        }
    }
}