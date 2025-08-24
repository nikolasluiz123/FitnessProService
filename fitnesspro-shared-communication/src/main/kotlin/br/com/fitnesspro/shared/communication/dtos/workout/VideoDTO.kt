package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.common.StorageModelDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IVideoDTO
import java.time.LocalDateTime

data class VideoDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var storageTransmissionDate: LocalDateTime? = null,
    override var storageUrl: String? = null,
    override var active: Boolean = true,
    override var extension: String? = null,
    override var filePath: String? = null,
    override var date: LocalDateTime? = null,
    override var kbSize: Long? = null,
    override var seconds: Long? = null,
    override var width: Int? = null,
    override var height: Int? = null
) : IVideoDTO