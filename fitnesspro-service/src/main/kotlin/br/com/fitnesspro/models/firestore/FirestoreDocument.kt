package br.com.fitnesspro.models.firestore

import br.com.fitnesspro.config.gson.defaultGSon
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder

abstract class FirestoreDocument {

    fun toMap(): Map<String, Any?> {
        val gson = GsonBuilder().defaultGSon()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any?>>() {}.type)
    }
}