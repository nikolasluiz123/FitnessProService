package br.com.fitnesspro.common.service.storage.result

import java.net.URI
import java.util.concurrent.TimeUnit

data class StorageUploadResult(
    val uri: URI?,
    val expiration: Long,
    val expirationUnit: TimeUnit
)
