package com.projetoFirebase.domain.useCase

import com.projetoFirebase.data.repositories.RepositoryFirebase
import com.projetoFirebase.util.DefaultListenerResponse
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class RecoverAccountFBUseCase @Inject constructor(repositoryFirebase: RepositoryFirebase) :
    BaseUseCase<RepositoryFirebase, FirebaseUser, String>(repositoryFirebase) {
    override suspend fun <T> run(
        defaultListenerResponse: DefaultListenerResponse<FirebaseUser, String>,
        vararg data: T
    ) {
        val email = data.first() as String

        repository.recoverAccountFireBase(
            email,
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