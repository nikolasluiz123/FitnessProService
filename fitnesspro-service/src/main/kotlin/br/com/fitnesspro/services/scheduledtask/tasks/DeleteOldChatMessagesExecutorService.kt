package br.com.fitnesspro.services.scheduledtask.tasks

import br.com.fitnesspro.manager.tasks.config.DeleteOldChatMessagesConfig
import br.com.fitnesspro.repository.jpa.firebase.IFirebaseChatRepository
import br.com.fitnesspro.services.logs.ExecutionsLogService
import br.com.fitnesspro.services.scheduledtask.tasks.common.IScheduledTaskExecutorService
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