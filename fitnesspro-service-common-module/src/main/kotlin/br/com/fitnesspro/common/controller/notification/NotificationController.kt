package br.com.fitnesspro.common.controller.notification

import br.com.fitnesspro.common.service.firebase.FirebaseNotificationService
import br.com.fitnesspro.service.communication.dtos.notification.ValidatedGlobalNotificationDTO
import br.com.fitnesspro.service.communication.dtos.notification.ValidatedNotificationDTO
import br.com.fitnesspro.service.communication.responses.ValidatedFitnessProServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
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
    fun sendNotification(@RequestBody @Valid validatedNotificationDTO: ValidatedNotificationDTO): ResponseEntity<ValidatedFitnessProServiceResponse> {
        firebaseNotificationService.sendExternalNotification(validatedNotificationDTO)
        return ResponseEntity.ok(ValidatedFitnessProServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.NOTIFICATION_NOTIFY_ALL)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun sendNotificationAllDevices(@RequestBody @Valid validatedNotificationDTO: ValidatedGlobalNotificationDTO): ResponseEntity<ValidatedFitnessProServiceResponse> {
        firebaseNotificationService.sendNotificationToAllDevices(validatedNotificationDTO)
        return ResponseEntity.ok(ValidatedFitnessProServiceResponse(code = HttpStatus.OK.value(), success = true))
    }
}