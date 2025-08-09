package br.com.fitnesspro.models.firestore

data class MessageNotificationDocument(
    val id: String = "",
    val text: String? = null,
    val date: Long? = null,
    val personReceiverId: String? = null,
    val personSenderName: String? = null,
    val chatId: String? = null
): FirestoreDocument() {

    companion object {
        const val COLLECTION_NAME = "messageNotifications"
    }
}