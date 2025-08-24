package br.com.fitnesspro.service.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IUserDTO
import br.com.fitnesspro.shared.communication.enums.general.EnumUserType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção de um usuário")
data class ValidatedUserDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "userDTO.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "E-mail do usuário", example = "usuario@example.com", required = true)
    @field:NotBlank(message = "userDTO.email.notBlank")
    @field:Size(min = 1, max = 64, message = "userDTO.email.size")
    @field:Email(message = "userDTO.email.email")
    override var email: String? = null,

    @field:Schema(description = "Senha do usuário", example = "senha123", required = true)
    @field:NotBlank(message = "userDTO.password.notBlank")
    @field:Size(min = 1, max = 1024, message = "userDTO.password.size")
    override var password: String? = null,

    @field:Schema(description = "Tipo de usuário", required = true)
    @field:NotNull(message = "userDTO.type.notNull")
    override var type: EnumUserType? = null,
): IUserDTO