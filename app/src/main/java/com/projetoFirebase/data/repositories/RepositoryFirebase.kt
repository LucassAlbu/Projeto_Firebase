package com.projetoFirebase.data.repositories

import com.projetoFirebase.data.models.UserDTO
import com.projetoFirebase.util.DefaultListenerResponse
import com.google.firebase.auth.FirebaseUser

interface RepositoryFirebase {

    val currentUser: FirebaseUser?

    suspend fun loginFirebase(
        email: String,
        password: String,
        listenerResponse: DefaultListenerResponse<FirebaseUser, String>
    )

    suspend fun signUpFirebase(
        userDTO: UserDTO,
        listenerResponse: DefaultListenerResponse<FirebaseUser, String>
    )

    suspend fun recoverAccountFireBase(
        email: String,
        listenerResponse: DefaultListenerResponse<FirebaseUser, String>
    )

    fun logout()

    suspend fun createUserDataFireStore(
        userDTO: UserDTO,
        listenerResponse: DefaultListenerResponse<UserDTO, String>
    )

    suspend fun getUserDataFireStore(
        listenerResponse: DefaultListenerResponse<UserDTO, String>
    )

}