package br.com.fitnesspro.models.logs

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Table(
    name = "execution_log_sub_package",
    indexes = [
        Index(name = "idx_execution_log_sub_package_execution_log_package_id", columnList = "execution_log_package_id"),
    ]
)
@Entity
class ExecutionLogSubPackage(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(name = "entity_name")
    var entityName: String? = null,

    @ManyToOne
    @JoinColumn(name = "execution_log_package_id", nullable = false)
    var executionLogPackage: ExecutionLogPackage? = null,

    @Column(name = "response_body", columnDefinition = "TEXT")
    var responseBody: String? = null,

    @Column(name = "request_body", columnDefinition = "TEXT")
    var requestBody: String? = null,

    @Column(name = "inserted_items_count")
    var insertedItemsCount: Int? = null,

    @Column(name = "updated_items_count")
    var updatedItemsCount: Int? = null,

    @Column(name = "all_items_count")
    var allItemsCount: Int? = null,

    @Column(name = "kb_size")
    var kbSize: Long? = null,

    @Column(name = "last_update_date")
    var lastUpdateDate: LocalDateTime? = null

)