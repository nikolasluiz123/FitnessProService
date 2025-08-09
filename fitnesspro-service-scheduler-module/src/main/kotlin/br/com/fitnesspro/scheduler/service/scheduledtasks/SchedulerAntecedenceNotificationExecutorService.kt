package br.com.fitnesspro.scheduler.service.scheduledtasks

import br.com.fitnesspro.core.gson.defaultGSon
import br.com.fitnesspro.common.service.firebase.FirebaseNotificationService
import br.com.fitnesspro.authentication.service.DeviceService
import br.com.fitnesspro.core.enums.EnumDateTimePatterns
import br.com.fitnesspro.core.extensions.format
import br.com.fitnesspro.log.service.ExecutionsLogService
import br.com.fitnesspro.scheduler.repository.jpa.ICustomSchedulerRepository
import br.com.fitnesspro.scheduler.service.SchedulerService
import br.com.fitnesspro.scheduler.to.TOSchedulerAntecedenceNotificationInfo
import br.com.fitnesspro.scheduled.task.service.IScheduledTaskExecutorService
import br.com.fitnesspro.shared.communication.dtos.serviceauth.DeviceDTO
import br.com.fitnesspro.shared.communication.enums.notification.EnumNotificationChannel
import br.com.fitnesspro.shared.communication.notification.SchedulerNotificationCustomData
import com.google.gson.GsonBuilder
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class SchedulerAntecedenceNotificationExecutorService(
    private val firebaseNotificationService: FirebaseNotificationService,
    private val customSchedulerRepository: ICustomSchedulerRepository,
    private val schedulerService: SchedulerService,
    private val logService: ExecutionsLogService,
    private val deviceService: DeviceService,
    private val messageSource: MessageSource
): IScheduledTaskExecutorService<Nothing?> {

    override fun execute(config: Nothing?, pairIds: Pair<String, String>) {
        val toNotificationInfo = customSchedulerRepository.getListTOSchedulerAntecedenceNotificationInfo()
        val additionalInformation = StringJoiner("\n")
        val schedulerIdsSuccess = mutableListOf<String>()

        if (toNotificationInfo.isNotEmpty()) {
            val results = toNotificationInfo.flatMap { notificationInfo ->
                val customData = SchedulerNotificationCustomData(
                    recurrent = false,
                    schedulerId = notificationInfo.schedulerId,
                    schedulerDate = notificationInfo.dateTimeStart.toLocalDate()
                )

                val (title, message) = getNotificationText(notificationInfo)

                val result = firebaseNotificationService.sendNotificationToPerson(
                    title = title,
                    message = message,
                    personIds = listOf(notificationInfo.personToSendNotificationId),
                    channel = EnumNotificationChannel.SCHEDULER_CHANNEL,
                    customJSONData = GsonBuilder().defaultGSon().toJson(customData)
                )

                if (result.first().success()) {
                    schedulerIdsSuccess.add(notificationInfo.schedulerId)
                }

                result
            }

            schedulerService.updateSchedulersNotified(schedulerIdsSuccess)

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

    private fun getNotificationText(notificationInfo: TOSchedulerAntecedenceNotificationInfo): Pair<String, String> {
        val title = messageSource.getMessage("scheduler.antecedence.notification.title", null, Locale.getDefault())

        val message = messageSource.getMessage(
            "scheduler.antecedence.notification.message",
            arrayOf(
                notificationInfo.dateTimeStart.format(EnumDateTimePatterns.DAY_MONTH),
                notificationInfo.dateTimeStart.format(EnumDateTimePatterns.TIME),
                notificationInfo.otherPersonName
            ),
            Locale.getDefault()
        )

        return Pair(title, message)
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