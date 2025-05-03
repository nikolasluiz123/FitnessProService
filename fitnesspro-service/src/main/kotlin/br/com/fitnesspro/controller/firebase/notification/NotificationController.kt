package br.com.fitnesspro.controller.firebase.notification

import br.com.fitnesspro.services.firebase.FirebaseNotificationService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.notification.GlobalNotificationDTO
import br.com.fitnesspro.shared.communication.dtos.notification.NotificationDTO
import br.com.fitnesspro.shared.communication.responses.FitnessProServiceResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(EndPointsV1.NOTIFICATION_V1)
@Tag(name = "Notification Controller", description = "Envio de notificações para os dispositivos")
class NotificationController(
    private val firebaseNotificationService: FirebaseNotificationService
) {

    @PostMapping
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun sendNotification(@RequestBody @Valid notificationDTO: NotificationDTO): ResponseEntity<FitnessProServiceResponse> {
        firebaseNotificationService.sendExternalNotification(notificationDTO)
        return ResponseEntity.ok(FitnessProServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.NOTIFICATION_NOTIFY_ALL)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun sendNotificationAllDevices(@RequestBody @Valid notificationDTO: GlobalNotificationDTO): ResponseEntity<FitnessProServiceResponse> {
        firebaseNotificationService.sendNotificationToAllDevices(notificationDTO)
        return ResponseEntity.ok(FitnessProServiceResponse(code = HttpStatus.OK.value(), success = true))
    }
}