package br.com.fitnesspro.service.models.logs

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Table(
    name = "execution_log_package",
    indexes = [
        Index(name = "idx_execution_log_package_execution_log_id", columnList = "execution_log_id"),
    ]
)
@Entity
class ExecutionLogPackage(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne
    @JoinColumn(name = "execution_log_id", nullable = false)
    var executionLog: ExecutionLog,

    @Column(name = "client_execution_start")
    var clientExecutionStart: LocalDateTime? = null,

    @Column(name = "client_execution_end")
    var clientExecutionEnd: LocalDateTime? = null,

    @Column(name = "service_execution_start", nullable = false)
    var serviceExecutionStart: LocalDateTime? = null,

    @Column(name = "service_execution_end")
    var serviceExecutionEnd: LocalDateTime? = null,

    @Column(name = "request_body", columnDefinition = "TEXT")
    var requestBody: String? = null,

    @Column(name = "response_body", columnDefinition = "TEXT")
    var responseBody: String? = null,

    @Column(name = "error", columnDefinition = "TEXT")
    var error: String? = null,

    @Column(name = "inserted_items_count")
    var insertedItemsCount: Int? = null,

    @Column(name = "updated_items_count")
    var updatedItemsCount: Int? = null,

    @Column(name = "all_items_count")
    var allItemsCount: Int? = null,
)