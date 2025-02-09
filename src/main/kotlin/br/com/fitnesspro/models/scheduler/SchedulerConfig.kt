package br.com.fitnesspro.models.scheduler

import br.com.fitnesspro.models.base.BaseModel
import br.com.fitnesspro.models.general.Person
import jakarta.persistence.*
import java.util.*


@Entity
@Table(
    name = "scheduler_config",
    indexes = [
        Index(name = "idx_scheduler_config_person_id", columnList = "person_id")
    ]
)
data class SchedulerConfig(
    @Id
    override val id: String = UUID.randomUUID().toString(),
    override var active: Boolean = true,

    var alarm: Boolean = false,

    var notification: Boolean = false,

    @Column(name = "min_schedule_density", nullable = false)
    var minScheduleDensity: Int = 1,

    @Column(name = "max_schedule_density", nullable = false)
    var maxScheduleDensity: Int = 2,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "person_id", nullable = false)
    var person: Person? = null
): BaseModel()