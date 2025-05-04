package br.com.fitnesspro.repository.firebase

import br.com.fitnesspro.manager.tasks.config.DeleteOldChatMessagesConfig
import br.com.fitnesspro.models.firestore.MessageNotificationDocument
import com.google.cloud.firestore.QueryDocumentSnapshot

interface IFirebaseChatRepository {

    fun deleteOldMessages(config: DeleteOldChatMessagesConfig): Int

    fun getListQueryReferenceMessageNotification(): List<QueryDocumentSnapshot>

    fun getListMessageNotificationDocument(querySnapshots: List<QueryDocumentSnapshot>): List<MessageNotificationDocument>

    fun deleteNotifications(notifications: List<QueryDocumentSnapshot>)
}