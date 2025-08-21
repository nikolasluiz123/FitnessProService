package br.com.fitnesspro.models.base

import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

interface StorageModel {
    var storageTransmissionDate: LocalDateTime?
    var storageUrl: String?
    var storageUrlExpiration: Long?
    var expirationUnit: TimeUnit?
}