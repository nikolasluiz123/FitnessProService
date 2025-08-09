package br.com.fitnesspro.scheduler.repository.jpa.firebase

import br.com.fitnesspro.models.firestore.MessageNotificationDocument
import br.com.fitnesspro.scheduler.scheduledtasks.config.DeleteOldChatMessagesConfig
import com.google.cloud.firestore.QueryDocumentSnapshot

interface IFirebaseChatRepository {

    fun deleteOldMessages(config: DeleteOldChatMessagesConfig): Int

    fun getListQueryReferenceMessageNotification(): Map<String, List<QueryDocumentSnapshot>>

    fun getListMessageNotificationDocument(querySnapshots: Map<String, List<QueryDocumentSnapshot>>): Map<String, List<MessageNotificationDocument>>
}