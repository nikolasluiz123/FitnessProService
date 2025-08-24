package br.com.fitnesspro.service.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IUserDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção de uma pessoa")
data class ValidatedPersonDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Nome", example = "João da Silva", required = true)
    @field:Size(min = 1, max = 512, message = "personDTO.name.size")
    override var name: String? = null,

    @field:Schema(description = "Data de nascimento", example = "1990-01-01", required = false)
    @field:PastOrPresent(message = "personDTO.birthDate.pastOrPresent")
    override var birthDate: LocalDate? = null,

    @field:Schema(description = "Telefone", example = "47999999999", required = false)
    @field:Size(max = 11, message = "personDTO.phone.size")
    override var phone: String? = null,

    @field:Schema(description = "Usuário da Pessoa", required = true)
    @field:NotNull(message = "personDTO.user.notNull")
    override var user: IUserDTO? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "personDTO.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "Flag que indica se o serviço deve inserir um registro da configuração com os valores padrões para a pessoa que está sendo cadastrada.")
    @field:NotNull(message = "personDTO.createDefaultSchedulerConfig.notNull")
    override var createDefaultSchedulerConfig: Boolean = false
): IPersonDTO