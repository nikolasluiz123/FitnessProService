package br.com.fitnesspro.models.firestore

import br.com.fitnesspro.core.gson.defaultGSon
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

abstract class FirestoreDocument {

    fun toMap(): Map<String, Any?> {
        val gson = GsonBuilder().defaultGSon()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any?>>() {}.type)
    }
}