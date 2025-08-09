package br.com.fitnesspro.scheduler.service.scheduledtasks

import br.com.fitnesspro.log.service.ExecutionsLogService
import br.com.fitnesspro.scheduler.repository.jpa.firebase.IFirebaseChatRepository
import br.com.fitnesspro.scheduler.scheduledtasks.config.DeleteOldChatMessagesConfig
import br.com.fitnesspro.scheduled.task.service.IScheduledTaskExecutorService
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeleteOldChatMessagesExecutorService(
    private val firebaseChatRepository: IFirebaseChatRepository,
    private val logService: ExecutionsLogService
): IScheduledTaskExecutorService<DeleteOldChatMessagesConfig> {

    override fun execute(config: DeleteOldChatMessagesConfig?, pairIds: Pair<String, String>) {
        val messagesCount = firebaseChatRepository.deleteOldMessages(config!!)

        val additionalInformation = StringJoiner("\n").apply {
            add(" ------------------------------------ Mensagens Deletadas ------------------------------------ ")
            add(" $messagesCount registros de Mensagens ")
        }

        logService.updateScheduledTaskLogWithAdditionalInfos(pairIds.second, additionalInformation)
    }

}