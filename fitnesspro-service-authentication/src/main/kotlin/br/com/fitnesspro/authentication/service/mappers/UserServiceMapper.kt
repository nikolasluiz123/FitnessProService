package br.com.fitnesspro.authentication.service.mappers

import br.com.fitnesspro.authentication.repository.auditable.IUserRepository
import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.shared.communication.dtos.general.UserDTO
import org.springframework.stereotype.Service

@Service
class UserServiceMapper(
    private val userRepository: IUserRepository,
) {

    fun getUserDTO(model: User): UserDTO {
        return UserDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            email = model.email,
            password = model.password,
            type = model.type,
        )
    }

    fun getUser(dto: UserDTO): User {
        val user = dto.id?.let { userRepository.findById(it) }

        return when {
            dto.id == null -> {
                User(
                    email = dto.email,
                    password = dto.password,
                    type = dto.type,
                    active = dto.active,
                )
            }

            user?.isPresent == true -> {
                user.get().copy(
                    email = dto.email,
                    password = dto.password,
                    type = dto.type,
                    active = dto.active,
                )
            }

            else -> {
                User(
                    id = dto.id!!,
                    email = dto.email,
                    password = dto.password,
                    type = dto.type,
                    active = dto.active,
                )
            }
        }
    }
}