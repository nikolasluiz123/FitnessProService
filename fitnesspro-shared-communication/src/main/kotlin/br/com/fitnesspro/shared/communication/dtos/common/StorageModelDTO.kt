package br.com.fitnesspro.shared.communication.dtos.common

import java.time.LocalDateTime

interface StorageModelDTO {
    var storageTransmissionDate: LocalDateTime?
    var storageUrl: String?
}