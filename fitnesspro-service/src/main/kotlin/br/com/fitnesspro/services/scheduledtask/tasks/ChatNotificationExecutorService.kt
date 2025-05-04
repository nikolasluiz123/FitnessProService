package br.com.fitnesspro.services.scheduledtask.tasks

import br.com.fitnesspro.config.gson.defaultGSon
import br.com.fitnesspro.repository.firebase.FirebaseChatRepository
import br.com.fitnesspro.services.firebase.FirebaseNotificationService
import br.com.fitnesspro.services.logs.ExecutionsLogService
import br.com.fitnesspro.services.scheduledtask.tasks.common.IScheduledTaskExecutorService
import br.com.fitnesspro.services.serviceauth.DeviceService
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
        val querySnapshots = firebaseChatRepository.getListQueryReferenceMessageNotification()
        val additionalInformation = StringJoiner("\n")

        if (querySnapshots.isNotEmpty()) {
            val documents = firebaseChatRepository.getListMessageNotificationDocument(querySnapshots)
            val notificationsIdsSuccess = mutableListOf<String>()

            val results = documents.flatMap { document ->
                val result = firebaseNotificationService.sendNotificationToPerson(
                    title = "Mensagem do Chat",
                    message = document.text!!,
                    personIds = listOf(document.personReceiverId!!),
                    channel = EnumNotificationChannel.NEW_MESSAGE_CHAT_CHANNEL,
                    customJSONData = GsonBuilder().defaultGSon().toJson(document)
                )

                if (result.first().success()) {
                    notificationsIdsSuccess.add(document.id)
                }

                result
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
                exceptions = exceptions
            )

            val notificationsToDelete = querySnapshots.filter { notificationsIdsSuccess.contains(it.id) }
            firebaseChatRepository.deleteNotifications(notificationsToDelete)

            additionalInformation.apply {
                add(" ")
                add(" Notificações Deletadas: ${notificationsIdsSuccess.size} ")
            }
        } else {
            additionalInformation.addProcessLog(
                successDevicesIds = emptyList(),
                successDevices = emptyList(),
                failDevicesIds = emptyList(),
                failDevices = emptyList(),
                exceptions = emptyList()
            )
        }

        logService.updateScheduledTaskLogWithAdditionalInfos(pairIds.second, additionalInformation)
    }

    fun StringJoiner.addProcessLog(
        successDevicesIds: List<String>,
        successDevices: List<DeviceDTO>,
        failDevicesIds: List<String>,
        failDevices: List<DeviceDTO>,
        exceptions: List<Exception>
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