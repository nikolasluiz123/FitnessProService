package br.com.fitnesspro.service.service.firebase

import br.com.fitnesspro.shared.communication.dtos.general.PersonDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import org.springframework.stereotype.Service

@Service
class FirebaseAuthenticationService {

    fun saveUser(personDTO: PersonDTO) {
        if (personDTO.id == null) {
            createUser(personDTO)
        } else {
            updateUser(personDTO)
        }
    }

    fun deleteUser(personDTO: PersonDTO) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.getUserByEmail(personDTO.user?.email!!)

        firebaseAuth.deleteUser(user.uid)
    }

    private fun createUser(personDTO: PersonDTO) {
        val request = UserRecord.CreateRequest()
            .setEmail(personDTO.user?.email!!)
            .setPassword(personDTO.user?.password!!)
            .setDisplayName(personDTO.name)

        FirebaseAuth.getInstance().createUser(request)
    }

    private fun updateUser(personDTO: PersonDTO) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.getUserByEmail(personDTO.user?.email!!)

        val request = UserRecord.UpdateRequest(user.uid)
            .setEmail(personDTO.user?.email!!)
            .setPassword(personDTO.user?.password!!)
            .setDisplayName(personDTO.name)

        firebaseAuth.updateUser(request)
    }
}