package br.com.fitnesspro.repository.firebase

import br.com.fitnesspro.core.extensions.dateNow
import br.com.fitnesspro.manager.tasks.config.DeleteOldChatMessagesConfig
import br.com.fitnesspro.models.firestore.MessageNotificationDocument
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import com.google.firebase.cloud.FirestoreClient
import org.springframework.stereotype.Repository
import java.time.ZoneId

@Repository
class FirebaseChatRepository: IFirebaseChatRepository {

    private val firestore: Firestore = FirestoreClient.getFirestore()

    override fun deleteOldMessages(config: DeleteOldChatMessagesConfig): Int {
        val messagesToDelete = getMessages(config)

        firestore.runTransaction {
            messagesToDelete.forEach { message ->
                it.delete(message.reference)
            }
        }

        return messagesToDelete.size
    }

    override fun getListQueryReferenceMessageNotification(): List<QueryDocumentSnapshot> {
        val result = mutableListOf<QueryDocumentSnapshot>()
        val personsCollection = firestore.collection("persons")
        val personsQueryDocuments = personsCollection.get().get().documents

        personsQueryDocuments.forEach { personQueryDocument ->
            val path = getPersonNotificationPath(personQueryDocument)
            result.addAll(firestore.collection(path).get().get().documents)
        }

        return result
    }

    override fun getListMessageNotificationDocument(querySnapshots: List<QueryDocumentSnapshot>): List<MessageNotificationDocument> {
        return querySnapshots.map {
            it.toObject(MessageNotificationDocument::class.java)
        }
    }

    private fun getPersonNotificationPath(personQueryDocument: QueryDocumentSnapshot): String {
        return "persons/${personQueryDocument.id}/${MessageNotificationDocument.COLLECTION_NAME}"
    }

    override fun deleteNotifications(notifications: List<QueryDocumentSnapshot>) {
        firestore.runTransaction { transaction ->
            notifications.forEach { notification ->
                transaction.delete(notification.reference)
            }
        }
    }

    private fun getMessages(config: DeleteOldChatMessagesConfig): MutableList<QueryDocumentSnapshot> {
        val messagesToDelete = mutableListOf<QueryDocumentSnapshot>()

        val date = dateNow().minusDays(config.messageLifeTimeDays)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        val personsCollection = firestore.collection("persons")
        val personsQueryDocuments = personsCollection.get().get().documents

        personsQueryDocuments.forEach { personQueryDocument ->
            val personsChatsCollection = firestore.collection("persons/${personQueryDocument.id}/chats")
            val personsChatsQueryDocuments = personsChatsCollection.get().get().documents

            personsChatsQueryDocuments.forEach { chatQueryDocument ->
                val chatMessagesCollection = chatQueryDocument.reference.collection("messages")
                val messages = chatMessagesCollection.whereLessThanOrEqualTo("date", date).get().get().documents
                messagesToDelete.addAll(messages)
            }
        }
        return messagesToDelete
    }
}