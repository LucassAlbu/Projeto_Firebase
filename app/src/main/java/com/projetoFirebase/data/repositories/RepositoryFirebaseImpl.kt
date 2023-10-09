package com.projetoFirebase.data.repositories

import android.content.ContentValues
import android.util.Log
import com.projetoFirebase.data.models.UserDTO
import com.projetoFirebase.util.DefaultListenerResponse
import com.projetoFirebase.util.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class RepositoryFirebaseImpl @Inject constructor(
    private val fireBaseAuth: FirebaseAuth,
    private val database: FirebaseFirestore
) : RepositoryFirebase {

    override val currentUser: FirebaseUser?
        get() = fireBaseAuth.currentUser

    override suspend fun loginFirebase(
        email: String,
        password: String,
        listenerResponse: DefaultListenerResponse<FirebaseUser, String>
    ) {
        return try {
            val result = fireBaseAuth.signInWithEmailAndPassword(
                email,
                password
            ).await()
            listenerResponse.onSuccess(result.user!!)

        } catch (e: Exception) {
            e.printStackTrace()
            listenerResponse.onError(e.localizedMessage)
        }
    }

    override suspend fun signUpFirebase(
        userDTO: UserDTO,
        listenerResponse: DefaultListenerResponse<FirebaseUser, String>
    ) {
        return try {
            val result =
                fireBaseAuth.createUserWithEmailAndPassword(userDTO.email!!, userDTO.password!!)
                    .await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(userDTO.cpf!!).build()
            )?.await()
            listenerResponse.onSuccess(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            listenerResponse.onError(e.localizedMessage)
        }
    }

    override suspend fun recoverAccountFireBase(
        email: String,
        listenerResponse: DefaultListenerResponse<FirebaseUser, String>
    ) {
        return try {
            val result = fireBaseAuth.sendPasswordResetEmail(email).await()

        } catch (e: Exception) {
            e.printStackTrace()
            listenerResponse.onError(e.localizedMessage)
        }
    }

    override fun logout() {
        fireBaseAuth.signOut()
    }

    override suspend fun createUserDataFireStore(
        userDTO: UserDTO,
        listenerResponse: DefaultListenerResponse<UserDTO, String>
    ) {
        try {
            database.collection("users").document(currentUser?.uid.toString())
                .set(userDTO)
                .addOnSuccessListener { documentReference ->
                    listenerResponse.onSuccess(userDTO)
                    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference}")
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)

                }
        } catch (e: Exception) {
            e.printStackTrace()
            listenerResponse.onError(e.localizedMessage)
        }
    }

    override suspend fun getUserDataFireStore(
        listenerResponse: DefaultListenerResponse<UserDTO, String>
    ) {
        try {

            database.collection("users").document(currentUser?.uid.toString())
                .get()
                .addOnSuccessListener {
                    if (it != null) {
                        val userData: UserDTO = it.toObject(UserDTO::class.java)!!
                        listenerResponse.onSuccess(userData)
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            listenerResponse.onError(e.localizedMessage)
        }
    }
}