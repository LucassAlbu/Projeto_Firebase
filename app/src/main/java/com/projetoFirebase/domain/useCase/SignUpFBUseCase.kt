package com.projetoFirebase.domain.useCase

import com.projetoFirebase.data.models.UserDTO
import com.projetoFirebase.data.repositories.RepositoryFirebase
import com.projetoFirebase.util.DefaultListenerResponse
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SignUpFBUseCase @Inject constructor(repositoryFirebase: RepositoryFirebase) :
    BaseUseCase<RepositoryFirebase, FirebaseUser, String>(repositoryFirebase) {
    override suspend fun <T> run(
        defaultListenerResponse: DefaultListenerResponse<FirebaseUser, String>,
        vararg data: T
    ) {
        val userDTO = data.first() as UserDTO


        repository.signUpFirebase(
            userDTO,
            object : DefaultListenerResponse<FirebaseUser, String> {
                override fun onSuccess(data: FirebaseUser) {
                    defaultListenerResponse.onSuccess(data)
                }

                override fun onError(error: String) {
                    defaultListenerResponse.onError(error)
                }

            }
        )
    }
}