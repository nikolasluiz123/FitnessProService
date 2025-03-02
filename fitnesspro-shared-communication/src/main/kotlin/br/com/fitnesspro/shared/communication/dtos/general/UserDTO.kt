package br.com.fitnesspro.shared.communication.dtos.general

import br.com.fitnesspro.models.general.enums.EnumUserType
import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class UserDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "O identificador deve ter entre 1 e 255 caracteres")
    override var id: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Usuário que criou o registro", required = false)
    override var creationUserId: String? = null,

    @Schema(description = "Usuário que atualizou o registro", required = false)
    override var updateUserId: String? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "O campo ativo é obrigatório")
    var active: Boolean = true,

    @Schema(description = "E-mail do usuário", example = "usuario@example.com", required = true)
    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Size(min = 1, max = 64, message = "O e-mail deve ter entre 1 e 64 caracteres")
    @field:Email(message = "O e-mail é inválido")
    var email: String? = null,

    @Schema(description = "Senha do usuário", example = "senha123", required = true)
    @field:NotBlank(message = "A senha é obrigatória")
    @field:Size(min = 1, max = 1024, message = "A senha deve ter entre 1 e 1024 caracteres")
    var password: String? = null,

    @Schema(description = "Tipo de usuário", required = true)
    @field:NotNull(message = "O tipo de usuário é obrigatório")
    var type: EnumUserType? = null,

    @Schema(description = "Valor booleano que representa se o usuário está autenticado", required = true, readOnly = true)
    @field:NotNull(message = "O campo autenticado é obrigatório")
    var authenticated: Boolean = false,
): AuditableDTO