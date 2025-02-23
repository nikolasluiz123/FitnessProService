package br.com.fitnesspro.service.models.scheduler

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.service.models.base.IntegratedModel
import br.com.fitnesspro.service.models.general.Person
import br.com.fitnesspro.service.models.general.User
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*


@Entity
@Table(
    name = "scheduler_config",
    indexes = [
        Index(name = "idx_scheduler_config_person_id", columnList = "person_id"),
        Index(name = "idx_scheduler_config_creation_user_id", columnList = "creation_user_id"),
        Index(name = "idx_scheduler_config_update_user_id", columnList = "update_user_id")
    ]
)
data class SchedulerConfig(
    @Id
    override val id: String = UUID.randomUUID().toString(),

    override var active: Boolean = true,

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    @Column(name = "transmission_date", nullable = false)
    override var transmissionDate: LocalDateTime = dateTimeNow(),

    @ManyToOne
    @JoinColumn(name = "creation_user_id", nullable = false)
    override var creationUser: User? = null,

    @ManyToOne
    @JoinColumn(name = "update_user_id", nullable = false)
    override var updateUser: User? = null,

    var alarm: Boolean = false,

    var notification: Boolean = false,

    @Column(name = "min_schedule_density", nullable = false)
    var minScheduleDensity: Int = 1,

    @Column(name = "max_schedule_density", nullable = false)
    var maxScheduleDensity: Int = 2,

    @OneToOne
    @JoinColumn(name = "person_id", nullable = false)
    var person: Person? = null
): IntegratedModel()