package br.com.fitnesspro.scheduler.service.scheduledtasks

import br.com.fitnesspro.core.gson.defaultGSon
import br.com.fitnesspro.common.service.firebase.FirebaseNotificationService
import br.com.fitnesspro.authentication.service.DeviceService
import br.com.fitnesspro.log.service.ExecutionsLogService
import br.com.fitnesspro.scheduler.repository.jpa.firebase.FirebaseChatRepository
import br.com.fitnesspro.scheduled.task.service.IScheduledTaskExecutorService
import br.com.fitnesspro.shared.communication.dtos.serviceauth.DeviceDTO
import br.com.fitnesspro.shared.communication.enums.notification.EnumNotificationChannel
import com.google.gson.GsonBuilder
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatNotificationExecutorService(
    private val firebaseNotificationService: FirebaseNotificationService,
    private val firebaseChatRepository: FirebaseChatRepository,
    private val logService: ExecutionsLogService,
    private val deviceService: DeviceService
): IScheduledTaskExecutorService<Nothing> {

    override fun execute(config: Nothing?, pairIds: Pair<String, String>) {
        val mapPersonNotificationsQuerySnapshots = firebaseChatRepository.getListQueryReferenceMessageNotification()
        val additionalInformation = StringJoiner("\n")

        if (mapPersonNotificationsQuerySnapshots.isNotEmpty()) {
            val mapPersonNotificationDocuments = firebaseChatRepository.getListMessageNotificationDocument(mapPersonNotificationsQuerySnapshots)

            val results = mapPersonNotificationDocuments.flatMap { (personId, notifications) ->
                firebaseNotificationService.sendNotificationToPerson(
                    title = "",
                    message = "",
                    personIds = listOf(personId),
                    channel = EnumNotificationChannel.NEW_MESSAGE_CHAT_CHANNEL,
                    customJSONData = GsonBuilder().defaultGSon().toJson(notifications)
                )
            }

            val successDevicesIds = results.flatMap { it.idsDevicesSuccess }
            val errorDevicesIds = results.flatMap { it.idsDevicesError }
            val exceptions = results.mapNotNull { it.exception }.distinctBy { it.message }

            val successDevices = deviceService.getDeviceDTOWithIds(successDevicesIds)
            val errorDevices = deviceService.getDeviceDTOWithIds(errorDevicesIds)

            additionalInformation.addProcessLog(
                successDevicesIds = successDevicesIds,
                successDevices = successDevices,
                failDevicesIds = errorDevicesIds,
                failDevices = errorDevices,
                exceptions = exceptions,
            )
        } else {
            additionalInformation.addProcessLog(
                successDevicesIds = emptyList(),
                successDevices = emptyList(),
                failDevicesIds = emptyList(),
                failDevices = emptyList(),
                exceptions = emptyList(),
            )
        }

        logService.updateScheduledTaskLogWithAdditionalInfos(pairIds.second, additionalInformation)
    }

    fun StringJoiner.addProcessLog(
        successDevicesIds: List<String>,
        successDevices: List<DeviceDTO>,
        failDevicesIds: List<String>,
        failDevices: List<DeviceDTO>,
        exceptions: List<Exception>,
    ) {
        val successMessageIds = successDevicesIds.joinToStringOrLabel("Nenhum dispositivo foi notificado")
        val successPersonNames = successDevices.map { it.person?.name!! }.joinToStringOrLabel("Ninguém foi notificado")

        val failMessageIds = failDevicesIds.joinToStringOrLabel("Nenhum erro ocorreu ao notificar")
        val failPersonNames = failDevices.map { it.person?.name!! }.joinToStringOrLabel("Nenhum erro ocorreu ao notificar")

        add(" ------------------------------------ Notificações Enviadas ------------------------------------ ")
        add("  ")
        add(" Dispositivos Notificados com Sucesso: $successMessageIds ")
        add(" Pessoas Notificadas com Sucesso: $successPersonNames ")
        add(" ")
        add(" --------------------------------------- Erros ao Enviar --------------------------------------- ")
        add("  ")
        add(" Dispositivos Não Notificados por Erro: $failMessageIds ")
        add(" Pessoas Não Notificadas por Erro: $failPersonNames ")
        add(" ")
        add(" Erros Ocorridos: ")

        if (exceptions.isNotEmpty()) {
            exceptions.forEachIndexed { index, exception ->
                add(" ${index + 1}-) ${exception.message} ")
            }
        } else {
            add(" Nenhum erro ocorreu ")
        }
    }

    private fun List<String>.joinToStringOrLabel(label: String): String {
        return if (this.isNotEmpty()) this.joinToString() else label
    }
}