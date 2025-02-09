package br.com.fitnesspro.models.general

import br.com.fitnesspro.models.base.BaseModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "academy")
data class Academy(
    @Id
    override val id: String = UUID.randomUUID().toString(),
    override var active: Boolean = true,

    @Column(nullable = false, length = 512)
    var name: String? = null,

    @Column(nullable = false, length = 512)
    var address: String? = null,

    @Column(length = 11)
    var phone: String? = null,
): BaseModel()