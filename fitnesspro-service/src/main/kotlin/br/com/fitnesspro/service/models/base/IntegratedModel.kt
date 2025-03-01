package br.com.fitnesspro.service.models.base

import java.time.LocalDateTime

interface IntegratedModel: BaseModel {
    var transmissionDate: LocalDateTime
}