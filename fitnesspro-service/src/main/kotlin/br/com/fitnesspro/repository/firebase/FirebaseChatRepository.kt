package br.com.fitnesspro.repository.firebase

import br.com.fitnesspro.core.extensions.dateNow
import br.com.fitnesspro.manager.tasks.config.DeleteOldChatMessagesConfig
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