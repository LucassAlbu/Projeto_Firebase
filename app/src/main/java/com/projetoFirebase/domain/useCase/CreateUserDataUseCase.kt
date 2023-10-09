package com.projetoFirebase.domain.useCase

import com.projetoFirebase.data.models.UserDTO
import com.projetoFirebase.data.repositories.RepositoryFirebase
import com.projetoFirebase.util.DefaultListenerResponse
import javax.inject.Inject

class CreateUserDataUseCase @Inject constructor(repositoryFirebase: RepositoryFirebase) :
    BaseUseCase<RepositoryFirebase, UserDTO, String>(repositoryFirebase) {
    override suspend fun <T> run(
        defaultListenerResponse: DefaultListenerResponse<UserDTO, String>,
        vararg data: T
    ) {

        val userDTO = data.first() as UserDTO

        repository.createUserDataFireStore(
        userDTO,

            object : DefaultListenerResponse<UserDTO, String> {
                override fun onSuccess(data: UserDTO) {
                    defaultListenerResponse.onSuccess(data)
                }

                override fun onError(error: String) {
                    defaultListenerResponse.onError(error)
                }



            }
        )
    }
}