package br.com.fitnesspro.service.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IAcademyDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção de uma academia")
data class ValidatedAcademyDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 0, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Nome da academia", example = "Academia XYZ", required = true)
    @field:NotBlank(message = "academyDTO.name.notBlank")
    @field:Size(min = 1, max = 512, message = "academyDTO.name.size")
    override var name: String? = null,

    @field:Schema(description = "Endereço da academia", example = "Rua Exemplo, 123", required = true)
    @field:NotBlank(message = "academyDTO.address.notBlank")
    @field:Size(min = 1, max = 512, message = "academyDTO.address.size")
    override var address: String? = null,

    @field:Schema(description = "Telefone da academia", example = "1112345678", required = false)
    @field:Size(min = 0, max = 11, message = "academyDTO.phone.size")
    override var phone: String? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "academyDTO.active.notNull")
    override var active: Boolean = true
): IAcademyDTO