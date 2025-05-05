package br.com.fitnesspro.repository.firebase

import br.com.fitnesspro.manager.tasks.config.DeleteOldChatMessagesConfig
import br.com.fitnesspro.models.firestore.MessageNotificationDocument
import com.google.cloud.firestore.QueryDocumentSnapshot

interface IFirebaseChatRepository {

    fun deleteOldMessages(config: DeleteOldChatMessagesConfig): Int

    fun getListQueryReferenceMessageNotification(): Map<String, List<QueryDocumentSnapshot>>

    fun getListMessageNotificationDocument(querySnapshots: Map<String, List<QueryDocumentSnapshot>>): Map<String, List<MessageNotificationDocument>>
}