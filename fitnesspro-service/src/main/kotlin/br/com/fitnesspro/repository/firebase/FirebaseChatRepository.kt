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

    override fun getListQueryReferenceMessageNotification(): Map<String, List<QueryDocumentSnapshot>> {
        val result = mutableMapOf<String, List<QueryDocumentSnapshot>>()
        val personsCollection = firestore.collection("persons")
        val personsQueryDocuments = personsCollection.get().get().documents

        personsQueryDocuments.forEach { personQueryDocument ->
            val notificationsPath = getPersonNotificationPath(personQueryDocument)
            val notifications = firestore.collection(notificationsPath).get().get().documents

            if (notifications.isNotEmpty()) {
                result.put(personQueryDocument.id, notifications)
            }
        }

        return result
    }

    override fun getListMessageNotificationDocument(querySnapshots: Map<String, List<QueryDocumentSnapshot>>): Map<String, List<MessageNotificationDocument>> {
        return querySnapshots.mapValues { (_, snapshotList) ->
            snapshotList.map { snapshot ->
                snapshot.toObject(MessageNotificationDocument::class.java)
            }
        }
    }

    private fun getPersonNotificationPath(personQueryDocument: QueryDocumentSnapshot): String {
        return "persons/${personQueryDocument.id}/${MessageNotificationDocument.COLLECTION_NAME}"
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