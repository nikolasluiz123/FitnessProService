package br.com.fitnesspro.repository.firebase

import br.com.fitnesspro.manager.tasks.config.DeleteOldChatMessagesConfig

interface IFirebaseChatRepository {

    fun deleteOldMessages(config: DeleteOldChatMessagesConfig): Int
}