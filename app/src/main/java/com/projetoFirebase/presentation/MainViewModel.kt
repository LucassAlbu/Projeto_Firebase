package com.projetoFirebase.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.projetoFirebase.data.models.UserDTO
import com.projetoFirebase.data.repositories.RepositoryFirebase
import com.projetoFirebase.domain.useCase.CreateUserDataUseCase
import com.projetoFirebase.domain.useCase.GetUserDataFireStoreUseCase
import com.projetoFirebase.domain.useCase.LoginFBUseCase
import com.projetoFirebase.domain.useCase.RecoverAccountFBUseCase
import com.projetoFirebase.domain.useCase.SignUpFBUseCase
import com.projetoFirebase.util.DefaultListenerResponse
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repositoryFirebase: RepositoryFirebase,
    private val loginFBUseCase: LoginFBUseCase,
    private val signUpFBUseCase: SignUpFBUseCase,
    private val recoverAccountFBUseCase: RecoverAccountFBUseCase,
    private val createUserDataUseCase: CreateUserDataUseCase,
    private val getUserDataFireStoreUseCase: GetUserDataFireStoreUseCase
) : BaseViewModel<UIState>() {

    private val _loginFB = MutableLiveData<FirebaseUser>(null)
    val loginFB: LiveData<FirebaseUser> = _loginFB

    private val _signUpFB = MutableLiveData<FirebaseUser>(null)
    val signUpFB: LiveData<FirebaseUser> = _signUpFB

    private val _recoverAccountFB = MutableLiveData<FirebaseUser>(null)
    val recoverAccountFB: LiveData<FirebaseUser> = _recoverAccountFB

    private var _bottomNavigateIsVisible = MutableLiveData<Boolean>()
    val bottomNavigateIsVisible: LiveData<Boolean> = _bottomNavigateIsVisible

    private var _checkBottomNavigationItem: MutableLiveData<Boolean> = MutableLiveData()
    val checkBottomNavigationItem: LiveData<Boolean> = _checkBottomNavigationItem

    private var _toolbarIsVisible = MutableLiveData<Boolean>()
    val toolbarIsvisible: LiveData<Boolean> = _toolbarIsVisible

    private var _toolbarTitleText = MutableLiveData<String>()
    val toolbarTitleText: LiveData<String> = _toolbarTitleText

    private var _toolbarBackButtonIsVisible = MutableLiveData<Boolean>()
    val toolbarBackButtonIsVisible: LiveData<Boolean> = _toolbarBackButtonIsVisible

    private var _statusBarIsVisible = MutableLiveData<Boolean>()
    val statusBarIsVisible: LiveData<Boolean> = _statusBarIsVisible

    private var _userDTO = MutableLiveData<UserDTO>()
    val userDTO: LiveData<UserDTO> = _userDTO

    val currentUserFB: FirebaseUser?
        get() = repositoryFirebase.currentUser

    init {
        if (repositoryFirebase.currentUser != null) {
            _loginFB.value = repositoryFirebase.currentUser!!
        }
    }

    fun loginFireBase(
        email: String,
        password: String
    ) {
        uiStateLoginFB.postValue(UIState.Loading)
        viewModelScope.launch {
            loginFBUseCase.run(
                object : DefaultListenerResponse<FirebaseUser, String> {
                    override fun onSuccess(data: FirebaseUser) {
                        _loginFB.postValue(data)
                        uiStateLoginFB.postValue(UIState.AuthSuccess)
                    }

                    override fun onError(error: String) {
                        uiStateLoginFB.postValue(UIState.AuthError(error, "Erro ao fazer Login"))
                    }

                },
                email,
                password

            )
        }
    }

    fun signUpFirebase(
        userDTO: UserDTO
    ) {
        uiStateSignUpFB.postValue(UIState.Loading)
        viewModelScope.launch {
            signUpFBUseCase.run(
                object : DefaultListenerResponse<FirebaseUser, String> {
                    override fun onSuccess(data: FirebaseUser) {
                        _signUpFB.postValue(data)
                        createUserDataFireStore(userDTO)
                    }

                    override fun onError(error: String) {
                        uiStateSignUpFB.postValue(UIState.AuthError(error, "Erro ao cadastrar"))
                    }
                }, userDTO
            )
        }
    }


    fun recoverAccountFireBase(
        email: String
    ) {
        uiStateRecoverAccountFB.postValue(UIState.Loading)
        viewModelScope.launch {
            recoverAccountFBUseCase.run(
                object : DefaultListenerResponse<FirebaseUser, String> {
                    override fun onSuccess(data: FirebaseUser) {
                        _recoverAccountFB.postValue(data)
                        uiStateRecoverAccountFB.postValue(UIState.AuthSuccess)
                    }

                    override fun onError(error: String) {
                        uiStateRecoverAccountFB.postValue(UIState.ErrorRecoverAccount)

                    }
                },
                email
            )
        }
    }

    fun createUserDataFireStore(userDTO: UserDTO) {
        uiStateSignUpFB.postValue(UIState.Loading)
        viewModelScope.launch {
            createUserDataUseCase.run(
                object : DefaultListenerResponse<UserDTO, String> {
                    override fun onSuccess(data: UserDTO) {
                        uiStateSignUpFB.postValue(UIState.AuthSuccess)
                    }

                    override fun onError(error: String) {
                        uiStateSignUpFB.postValue(UIState.AuthError(error, "Erro ao cadastrar"))
                    }
                }, userDTO
            )
        }
    }

    fun updateUserDataFireStore(updateUserDTO: UserDTO) {

        val newData = UserDTO(
            name = updateUserDTO.name,
            cpf = userDTO.value?.cpf,
            phone = updateUserDTO.phone,
            email = updateUserDTO.email,
            birthDate = updateUserDTO.birthDate,
            password = updateUserDTO.password
            //password = userAla.value?.password
        )

        uiStateSignUpFB.postValue(UIState.Loading)
        viewModelScope.launch {
            createUserDataUseCase.run(
                object : DefaultListenerResponse<UserDTO, String> {
                    override fun onSuccess(data: UserDTO) {
                        uiStateSignUpFB.postValue(UIState.AuthSuccess)
                    }

                    override fun onError(error: String) {
                        uiStateSignUpFB.postValue(
                            UIState.AuthError(
                                error,
                                "Erro ao Atualizar dados"
                            )
                        )
                    }
                }, newData
            )
        }
    }

    fun updateUserPasswordFireStore(updateUserDTO: UserDTO) {

        val newData = UserDTO(
            name = userDTO.value?.name,
            cpf = userDTO.value?.cpf,
            phone = userDTO.value?.phone,
            email = userDTO.value?.email,
            birthDate = userDTO.value?.birthDate,
            password = updateUserDTO.password
        )

        uiStateSignUpFB.postValue(UIState.Loading)
        viewModelScope.launch {
            createUserDataUseCase.run(
                object : DefaultListenerResponse<UserDTO, String> {
                    override fun onSuccess(data: UserDTO) {
                        uiStateSignUpFB.postValue(UIState.AuthSuccess)
                    }

                    override fun onError(error: String) {
                        uiStateSignUpFB.postValue(
                            UIState.AuthError(
                                error,
                                "Erro ao Atualizar dados"
                            )
                        )
                    }
                }, newData
            )
        }
    }

    fun getUserDataFireStore() {
        uiStateGetUserDataFS.postValue(UIState.Loading)
        viewModelScope.launch {
            getUserDataFireStoreUseCase.run(

                object : DefaultListenerResponse<UserDTO, String> {
                    override fun onSuccess(data: UserDTO) {
                        uiStateGetUserDataFS.postValue(UIState.FireStoreSuccess(data))
                        _userDTO.postValue(data)
                    }

                    override fun onError(error: String) {
                        uiStateGetUserDataFS.postValue(
                            UIState.FireStoreError(
                                error,
                                "Erro ao carregar informações"
                            )
                        )
                    }
                }, ""
            )
        }
    }


    fun logout() {
        repositoryFirebase.logout()
        uiStateLoginFB.postValue(UIState.FireBaseLogout)
    }


    fun bottomNavigateVisibility(show: Boolean) {
        _bottomNavigateIsVisible.value = show
    }

    fun setCheckableBottomNavigationItem(check: Boolean) {
        _checkBottomNavigationItem.value = check
    }

    fun toolbarVisibility(show: Boolean) {
        _toolbarIsVisible.value = show
    }

    fun setToolbarTitleText(toolbarTitle: String) {
        _toolbarTitleText.value = toolbarTitle
    }

    fun toolbarBackButtonVisibility(show: Boolean) {
        _toolbarBackButtonIsVisible.value = show
    }

    fun showStatusBar(show: Boolean) {
        _statusBarIsVisible.postValue(show)
    }

}