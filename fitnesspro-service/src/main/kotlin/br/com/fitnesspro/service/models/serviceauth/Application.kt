package br.com.fitnesspro.service.models.serviceauth

import br.com.fitnesspro.service.models.base.BaseModel
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "application")
data class Application(
    @Id
    override val id: String? = null,
    override var active: Boolean = true,
    val name: String? = null
): BaseModel
