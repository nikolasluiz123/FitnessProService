package br.com.fitnesspro.models.base

import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseModel {
    abstract val id: String?
    abstract var active: Boolean
}