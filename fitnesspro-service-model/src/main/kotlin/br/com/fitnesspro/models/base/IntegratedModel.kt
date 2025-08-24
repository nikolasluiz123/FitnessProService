package br.com.fitnesspro.models.base

import java.time.LocalDateTime

interface IntegratedModel: BaseModel {
    var transmissionDate: LocalDateTime
}