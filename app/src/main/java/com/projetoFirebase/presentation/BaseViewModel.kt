package com.projetoFirebase.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.projetoFirebase.util.CustomMutableLiveData

open class BaseViewModel<T> : ViewModel() {

    val uiStateLoginFB: CustomMutableLiveData<T> = CustomMutableLiveData()
    fun uiStateLogin(): LiveData<T> = uiStateLoginFB

    val uiStateSignUpFB: CustomMutableLiveData<T> = CustomMutableLiveData()
    fun uiStateSignUpFB(): LiveData<T> = uiStateSignUpFB

    val uiStateRecoverAccountFB: CustomMutableLiveData<T> = CustomMutableLiveData()
    fun uiStateRecoverAccountFB(): LiveData<T> = uiStateRecoverAccountFB

    val uiStateGetUserDataFS: CustomMutableLiveData<T> = CustomMutableLiveData()
    fun uiStateGetUserDataFS(): LiveData<T> = uiStateGetUserDataFS

    fun cleanUiState() {
        uiStateLoginFB.value = null!!
        uiStateSignUpFB.value = null!!
        uiStateRecoverAccountFB.value = null!!
        uiStateGetUserDataFS.value = null!!
    }
}