package br.com.fitnesspro.models.base

import java.time.LocalDateTime

interface StorageModel {
    var storageTransmissionDate: LocalDateTime?
    var storageUrl: String?
}