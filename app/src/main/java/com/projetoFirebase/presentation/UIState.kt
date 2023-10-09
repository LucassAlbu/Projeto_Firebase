package com.projetoFirebase.presentation

import com.projetoFirebase.data.models.UserDTO

sealed class UIState {

    object Loading : UIState()
    object AuthSuccess : UIState()
    object ErrorApi : UIState()
    object ErrorNetwork : UIState()
    object EmptyState : UIState()
    object FireBaseLogout : UIState()
    object ErrorRecoverAccount : UIState()
    data class AuthError(val error: String, val title: String) : UIState()
    data class FireStoreError(val error: String, val title: String) : UIState()
    data class FireStoreSuccess(val userDTO: UserDTO) : UIState()

}
