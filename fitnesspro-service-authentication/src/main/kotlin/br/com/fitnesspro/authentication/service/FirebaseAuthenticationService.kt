package br.com.fitnesspro.authentication.service

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonDTO
import com.google.firebase.auth.AuthErrorCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import org.springframework.stereotype.Service

@Service
class FirebaseAuthenticationService {

    fun saveUser(personDTO: IPersonDTO, personExists: Boolean) {
        if (personDTO.id == null || !personExists) {
            createUser(personDTO)
        } else {
            updateUser(personDTO)
        }
    }

    fun deleteUser(personDTO: IPersonDTO) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = try {
            firebaseAuth.getUserByEmail(personDTO.user?.email!!)
        } catch (ex: FirebaseAuthException) {
            if (ex.authErrorCode == AuthErrorCode.USER_NOT_FOUND) {
                null
            } else {
                throw ex
            }
        }

        user?.uid?.let(firebaseAuth::deleteUser)
    }

    private fun createUser(personDTO: IPersonDTO) {
        val request = UserRecord.CreateRequest()
            .setEmail(personDTO.user?.email!!)
            .setPassword(personDTO.user?.password!!)
            .setDisplayName(personDTO.name)

        FirebaseAuth.getInstance().createUser(request)
    }

    private fun updateUser(personDTO: IPersonDTO) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.getUserByEmail(personDTO.user?.email!!)

        val request = UserRecord.UpdateRequest(user.uid)
            .setEmail(personDTO.user?.email!!)
            .setPassword(personDTO.user?.password!!)
            .setDisplayName(personDTO.name)

        firebaseAuth.updateUser(request)
    }
}