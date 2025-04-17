package br.com.fitnesspro.service.models.serviceauth

import br.com.fitnesspro.service.models.base.BaseModel
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "application")
data class Application(
    @Id
    override val id: String = UUID.randomUUID().toString(),
    override var active: Boolean = true,
    val name: String? = null
): BaseModel
