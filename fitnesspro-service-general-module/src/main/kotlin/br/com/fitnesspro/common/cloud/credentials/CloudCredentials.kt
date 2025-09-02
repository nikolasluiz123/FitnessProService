package br.com.fitnesspro.common.cloud.credentials

import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*

object CloudCredentials {
    const val CLOUD_CREDENTIALS = "CLOUD_CREDENTIALS"
    const val SERVICE_ACCOUNT_KEY_JSON = "serviceAccountKey.json"

    fun getCredentialsInputStream(): InputStream {
        val file = File(SERVICE_ACCOUNT_KEY_JSON)

        return if (file.exists()) {
            FileInputStream(file)
        } else {
            val jsonBase64 = System.getenv(CLOUD_CREDENTIALS)
            val decoder = Base64.getDecoder()
            val decodedBytes = decoder.decode(jsonBase64)

            ByteArrayInputStream(decodedBytes)
        }
    }
}